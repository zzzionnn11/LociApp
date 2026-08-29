package com.example.lociapp.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun BottomNavigationBar(
    currentScreen: String,
    onHomeClick: () -> Unit,
    onListClick: () -> Unit,
    onSettingsClick: () -> Unit,
    onCameraClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .padding(bottom = 16.dp)
            .height(70.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Home / List / Settings pill
        Surface(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight(),
            shape = RoundedCornerShape(24.dp),
            color = Color.White.copy(alpha = 0.8f),
            shadowElevation = 12.dp
        ) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 8.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                val tabModifier = Modifier.weight(1f).fillMaxHeight()

                NavTab(
                    modifier = tabModifier,
                    icon = Icons.Filled.Home,
                    label = "Home",
                    selected = currentScreen == "home",
                    onClick = onHomeClick
                )
                NavTab(
                    modifier = tabModifier,
                    icon = Icons.AutoMirrored.Filled.List,
                    label = "List",
                    selected = currentScreen == "list",
                    onClick = onListClick
                )
                NavTab(
                    modifier = tabModifier,
                    icon = Icons.Filled.Settings,
                    label = "Settings",
                    selected = currentScreen == "settings",
                    onClick = onSettingsClick
                )
            }
        }

        // Separate floating camera button, as shown in every prototype screen
        Surface(
            modifier = Modifier
                .size(70.dp)
                .clickable { onCameraClick() },
            shape = CircleShape,
            color = Color.White.copy(alpha = 0.8f),
            shadowElevation = 12.dp
        ) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Icon(
                    imageVector = Icons.Filled.CameraAlt,
                    contentDescription = "Camera",
                    tint = Color.Black,
                    modifier = Modifier.size(26.dp)
                )
            }
        }
    }
}

@Composable
private fun NavTab(
    modifier: Modifier,
    icon: ImageVector,
    label: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    Box(modifier = modifier.clickable { onClick() }, contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            if (selected) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .background(Color.Black.copy(alpha = 0.1f), RoundedCornerShape(12.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(icon, label, tint = Color.Black, modifier = Modifier.size(22.dp))
                }
            } else {
                Icon(icon, label, tint = Color.Black.copy(alpha = 0.5f), modifier = Modifier.size(22.dp))
            }
            Text(
                text = label,
                color = if (selected) Color.Black else Color.Black.copy(alpha = 0.5f),
                fontSize = 10.sp
            )
        }
    }
}