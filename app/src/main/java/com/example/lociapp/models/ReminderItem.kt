package com.example.lociapp.models

data class ReminderItem(
    val id: Int,
    val title: String,
    val description: String,
    val time: String,
    val isUrgent: Boolean = false
)
