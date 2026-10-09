package com.example.lociapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lociapp.db.ItemRepository
import com.example.lociapp.models.ItemEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

/** Screens read [items] and call these functions; they never touch the database. */
class ItemsViewModel : ViewModel() {
    val items: StateFlow<List<ItemEntity>> = ItemRepository.items

    fun getItem(id: Int): ItemEntity? = items.value.firstOrNull { it.id == id }

    fun addItem(item: ItemEntity) {
        viewModelScope.launch(Dispatchers.IO) { ItemRepository.addItem(item) }
    }

    fun updateItem(item: ItemEntity) {
        viewModelScope.launch(Dispatchers.IO) { ItemRepository.updateItem(item) }
    }

    fun deleteItem(id: Int) {
        viewModelScope.launch(Dispatchers.IO) { ItemRepository.deleteItem(id) }
    }
}
