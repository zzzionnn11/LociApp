package com.example.lociapp.models

data class ItemEntity(
    val id: Int = 0,
    val title: String,
    val description: String,
    val reminderTime: String = "",
    val reminderDate: String = "",
    val imageUri: String? = null,
    val imageRes: Int = 0,
    val addedTime: String = "",
    val isUrgent: Boolean = false
)
