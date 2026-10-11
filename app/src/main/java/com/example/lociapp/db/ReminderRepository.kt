package com.example.lociapp.db

import android.content.Context
import com.example.lociapp.models.ItemEntity
import com.example.lociapp.models.ReminderEntity
import com.example.lociapp.notifications.ReminderScheduler
import com.example.lociapp.notifications.ReminderTime
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/** Stores reminders in SQLite and keeps their alarms scheduled. */
object ReminderRepository {
    private var dbHelper: DatabaseHelper? = null
    private var appContext: Context? = null

    private val _reminders = MutableStateFlow<List<ReminderEntity>>(emptyList())
    val reminders: StateFlow<List<ReminderEntity>> = _reminders.asStateFlow()

    fun init(context: Context) {
        if (dbHelper == null) {
            appContext = context.applicationContext
            dbHelper = DatabaseHelper.getInstance(context)
            refresh()
        }
    }

    fun refresh() {
        val helper = dbHelper ?: return
        _reminders.value = helper.getAllReminders()
    }

    /** Saves the reminder and schedules its notification. Returns the new id. */
    fun add(reminder: ReminderEntity): Int {
        val helper = dbHelper ?: return -1
        val ctx = appContext ?: return -1

        // Deduplicate: if a reminder already exists for this item/title, update it instead of adding a second
        val existing = _reminders.value.firstOrNull {
            (reminder.itemId != 0 && it.itemId == reminder.itemId) ||
                    (it.title.trim().equals(reminder.title.trim(), ignoreCase = true))
        }

        if (existing != null) {
            val updated = reminder.copy(id = existing.id)
            update(updated)
            return existing.id
        }

        val id = helper.insertReminder(reminder).toInt()
        if (id > 0) ReminderScheduler.schedule(ctx, reminder.copy(id = id))
        refresh()
        return id
    }

    fun update(reminder: ReminderEntity) {
        val helper = dbHelper ?: return
        val ctx = appContext ?: return
        helper.updateReminder(reminder)
        ReminderScheduler.cancel(ctx, reminder.id)
        ReminderScheduler.schedule(ctx, reminder)
        refresh()
    }

    fun delete(id: Int) {
        val helper = dbHelper ?: return
        val ctx = appContext ?: return
        ReminderScheduler.cancel(ctx, id)
        helper.deleteReminder(id)
        refresh()
    }

    /**
     * Makes the reminder tied to [item] match the item's reminder time/date:
     * creates it, updates it, or removes it if the item no longer has a time.
     */
    fun syncForItem(item: ItemEntity) {
        val existingList = _reminders.value.filter {
            (it.itemId != 0 && it.itemId == item.id) ||
                    (it.itemId == 0 && it.title.trim().equals(item.title.trim(), ignoreCase = true))
        }
        val trigger = ReminderTime.parse(item.reminderTime, item.reminderDate)

        if (trigger == null) {
            existingList.forEach { delete(it.id) }
            return
        }

        // Keep the primary existing reminder and cancel any accidental duplicates
        val primary = existingList.firstOrNull()
        existingList.drop(1).forEach { delete(it.id) }

        val reminder = ReminderEntity(
            id = primary?.id ?: 0,
            title = item.title,
            description = primary?.description.orEmpty(),
            time = item.reminderTime,
            date = item.reminderDate,
            triggerAtMillis = trigger,
            itemId = item.id,
            fromItem = primary?.fromItem ?: true
        )
        if (primary == null) add(reminder) else update(reminder)
    }

    /** Removes every reminder that points at a deleted item. */
    fun deleteForItem(itemId: Int) {
        _reminders.value.filter { it.itemId == itemId }.forEach { delete(it.id) }
    }

    /** Used by "Delete Account": cancels every alarm and empties the table. */
    fun clearAll() {
        val helper = dbHelper ?: return
        val ctx = appContext ?: return
        _reminders.value.forEach { ReminderScheduler.cancel(ctx, it.id) }
        helper.deleteAllReminders()
        refresh()
    }
}
