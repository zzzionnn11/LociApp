package com.example.lociapp.models

import androidx.compose.ui.graphics.vector.ImageVector

data class RecentItem(
    val id: Int,
    val title: String,
    val description: String,
    val time: String,
    val icon: ImageVector
)