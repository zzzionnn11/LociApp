package com.example.lociapp.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.lociapp.db.ItemRepository
import com.example.lociapp.db.ReminderRepository
import com.example.lociapp.models.ItemEntity
import com.example.lociapp.models.ReminderEntity
import com.example.lociapp.notifications.ReminderScheduler
import com.example.lociapp.notifications.ReminderTime
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

sealed interface SaveResult {
    /** [exactAlarms] is false when the user turned off "Alarms & reminders" (may fire a bit late). */
    data class Saved(val exactAlarms: Boolean) : SaveResult
    data class Invalid(val message: String) : SaveResult
}

class RemindersViewModel(app: Application) : AndroidViewModel(app) {
    val reminders: StateFlow<List<ReminderEntity>> = ReminderRepository.reminders
    val items: StateFlow<List<ItemEntity>> = ItemRepository.items

    fun save(
        name: String,
        time: String,
        date: String,
        item: ItemEntity?,
        note: String,
        onResult: (SaveResult) -> Unit
    ) {
        val title = name.trim().ifBlank { item?.title.orEmpty() }
        if (title.isBlank()) return onResult(SaveResult.Invalid("Please enter a reminder name."))
        if (time.isBlank()) return onResult(SaveResult.Invalid("Please pick a reminder time."))

        val trigger = ReminderTime.parse(time, date)
            ?: return onResult(SaveResult.Invalid("That time or date isn't valid."))
        if (trigger <= System.currentTimeMillis()) {
            return onResult(SaveResult.Invalid("Pick a time in the future."))
        }

        val reminder = ReminderEntity(
            title = title,
            description = note.trim(),
            time = time,
            date = date,
            triggerAtMillis = trigger,
            itemId = item?.id ?: 0,
            fromItem = false
        )

        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val existing = if (item != null && item.id != 0) {
                    ReminderRepository.reminders.value.firstOrNull { it.itemId == item.id }
                } else {
                    ReminderRepository.reminders.value.firstOrNull { it.title.equals(title, ignoreCase = true) }
                }

                if (existing != null) {
                    ReminderRepository.update(
                        existing.copy(
                            title = title,
                            description = note.trim(),
                            time = time,
                            date = date,
                            triggerAtMillis = trigger,
                            itemId = item?.id ?: existing.itemId,
                            fromItem = false
                        )
                    )
                } else {
                    ReminderRepository.add(reminder)
                }

                val targetItem = item ?: ItemRepository.items.value.firstOrNull {
                    it.title.equals(title, ignoreCase = true)
                }

                if (targetItem != null) {
                    ItemRepository.updateItem(
                        targetItem.copy(
                            reminderTime = time,
                            reminderDate = date,
                            isUrgent = reminder.isUrgent()
                        )
                    )
                }
            }
            onResult(SaveResult.Saved(ReminderScheduler.canScheduleExact(getApplication())))
        }
    }

    fun delete(id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val reminder = ReminderRepository.reminders.value.firstOrNull { it.id == id }
            if (reminder != null) {
                val targetItem = if (reminder.itemId != 0) {
                    ItemRepository.getItemById(reminder.itemId)
                } else {
                    ItemRepository.items.value.firstOrNull {
                        it.title.equals(reminder.title, ignoreCase = true)
                    }
                }

                if (targetItem != null) {
                    ItemRepository.updateItem(
                        targetItem.copy(
                            reminderTime = "",
                            reminderDate = "",
                            isUrgent = false
                        )
                    )
                }
            }
            ReminderRepository.delete(id)
        }
    }
}
