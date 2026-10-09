package com.example.lociapp.db

import android.content.Context
import com.example.lociapp.models.ItemEntity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * Single source of truth for items. Whenever an item is added, edited or
 * deleted, its reminder is kept in sync (see ReminderRepository), so the
 * notification always matches what the item says.
 */
object ItemRepository {
    private var dbHelper: DatabaseHelper? = null

    private val _items = MutableStateFlow<List<ItemEntity>>(emptyList())
    val items: StateFlow<List<ItemEntity>> = _items.asStateFlow()

    fun init(context: Context) {
        if (dbHelper == null) {
            dbHelper = DatabaseHelper.getInstance(context)
            refresh()
        }
    }

    fun refresh() {
        val helper = dbHelper ?: return
        _items.value = helper.getAllItems()
    }

    fun addItem(item: ItemEntity): Long {
        val helper = dbHelper ?: return -1
        val id = helper.insertItem(item)
        refresh()
        if (id > 0 && item.reminderTime.isNotBlank()) {
            ReminderRepository.syncForItem(item.copy(id = id.toInt()))
        }
        return id
    }

    fun getItemById(id: Int): ItemEntity? {
        val helper = dbHelper ?: return null
        return helper.getItemById(id)
    }

    fun updateItem(item: ItemEntity) {
        val helper = dbHelper ?: return
        helper.updateItem(item)
        refresh()
        ReminderRepository.syncForItem(item)
    }

    fun deleteItem(id: Int) {
        val helper = dbHelper ?: return
        helper.deleteItem(id)
        refresh()
        ReminderRepository.deleteForItem(id)
    }

    /** Used by "Delete Account". */
    fun clearAll() {
        val helper = dbHelper ?: return
        helper.deleteAllItems()
        refresh()
    }
}
