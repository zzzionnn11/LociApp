package com.example.lociapp.models

import androidx.compose.ui.graphics.vector.ImageVector

data class SettingsSection(
    val title: String,
    val items: List<SettingsItem>
)

data class SettingsItem(
    val title: String,
    val icon: ImageVector,
    val navigationRoute: String? = null
)