package com.example.lociapp.models

data class RecentItem(
    val id: Int,
    val title: String,
    val description: String,
    val time: String,
    val imageRes: Int,
    val isUrgent: Boolean = false
)