package com.example.lociapp.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .padding(bottom = 16.dp)
            .height(70.dp),
        shape = RoundedCornerShape(24.dp),
        color = Color.White.copy(alpha = 0.8f), // Light glass background
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

            // Home
            Box(modifier = tabModifier.clickable { onHomeClick() }, contentAlignment = Alignment.Center) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    if (currentScreen == "home") {
                        Box(
                            modifier = Modifier.size(44.dp).background(Color.Black.copy(alpha = 0.1f), RoundedCornerShape(12.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Filled.Home, "Home", tint = Color.Black, modifier = Modifier.size(24.dp))
                        }
                    } else {
                        Icon(Icons.Filled.Home, "Home", tint = Color.Black.copy(alpha = 0.5f), modifier = Modifier.size(24.dp))
                    }
                    Text("Home", color = if (currentScreen == "home") Color.Black else Color.Black.copy(alpha = 0.5f), fontSize = 10.sp)
                }
            }

            // List
            Box(modifier = tabModifier.clickable { onListClick() }, contentAlignment = Alignment.Center) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    if (currentScreen == "list") {
                        Box(
                            modifier = Modifier.size(44.dp).background(Color.Black.copy(alpha = 0.1f), RoundedCornerShape(12.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.AutoMirrored.Filled.List, "List", tint = Color.Black, modifier = Modifier.size(24.dp))
                        }
                    } else {
                        Icon(Icons.AutoMirrored.Filled.List, "List", tint = Color.Black.copy(alpha = 0.5f), modifier = Modifier.size(24.dp))
                    }
                    Text("List", color = if (currentScreen == "list") Color.Black else Color.Black.copy(alpha = 0.5f), fontSize = 10.sp)
                }
            }

            // Settings
            Box(modifier = tabModifier.clickable { onSettingsClick() }, contentAlignment = Alignment.Center) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    if (currentScreen == "settings") {
                        Box(
                            modifier = Modifier.size(44.dp).background(Color.Black.copy(alpha = 0.1f), RoundedCornerShape(12.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Filled.Settings, "Settings", tint = Color.Black, modifier = Modifier.size(24.dp))
                        }
                    } else {
                        Icon(Icons.Filled.Settings, "Settings", tint = Color.Black.copy(alpha = 0.5f), modifier = Modifier.size(24.dp))
                    }
                    Text("Settings", color = if (currentScreen == "settings") Color.Black else Color.Black.copy(alpha = 0.5f), fontSize = 10.sp)
                }
            }

            // Camera
            Box(modifier = tabModifier.clickable { onCameraClick() }, contentAlignment = Alignment.Center) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(Icons.Filled.CameraAlt, "Camera", tint = if (currentScreen == "camera") Color.Black else Color.Black.copy(alpha = 0.5f), modifier = Modifier.size(24.dp))
                    Text("Camera", color = if (currentScreen == "camera") Color.Black else Color.Black.copy(alpha = 0.5f), fontSize = 10.sp)
                }
            }
        }
    }
}