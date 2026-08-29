package com.example.lociapp.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Help
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.lociapp.components.BottomNavigationBar
import com.example.lociapp.models.SettingsItem
import com.example.lociapp.models.SettingsSection

@Composable
fun SettingsScreen(navController: NavController) {
    val settingsSections = listOf(
        SettingsSection("Account", listOf(SettingsItem("Profile", Icons.Default.Person, "profile"))),
        SettingsSection("Social", listOf(SettingsItem("About us", Icons.Default.Info, "about"))),
        SettingsSection("Support", listOf(
            SettingsItem("FAQ", Icons.AutoMirrored.Filled.Help, "faq"),
            SettingsItem("Language", Icons.Default.Language, null)
        )),
        SettingsSection("Legal", listOf(
            SettingsItem("Privacy Policy", Icons.Default.Lock, "privacy"),
            SettingsItem("Terms", Icons.Default.DocumentScanner, "terms")
        ))
    )

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp)
                .windowInsetsPadding(WindowInsets.statusBars)
        ) {
            Spacer(modifier = Modifier.height(16.dp))
            Text("Settings", color = Color.Black, fontSize = 28.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(24.dp))

            LazyColumn(
                modifier = Modifier.weight(1f).padding(bottom = 80.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(settingsSections) { section ->
                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Text(section.title, color = Color.Black.copy(alpha = 0.6f), fontSize = 12.sp, fontWeight = FontWeight.Medium, modifier = Modifier.padding(start = 4.dp, bottom = 4.dp))
                        section.items.forEach { item ->
                            SettingsCard(
                                icon = item.icon,
                                title = item.title,
                                onClick = { item.navigationRoute?.let { route -> navController.navigate(route) } }
                            )
                        }
                    }
                }
            }
        }

        Box(modifier = Modifier.fillMaxWidth().align(Alignment.BottomCenter)) {
            BottomNavigationBar(
                currentScreen = "settings",
                onHomeClick = { navController.navigate("home") },
                onListClick = { navController.navigate("list") },
                onSettingsClick = { },
                onCameraClick = { println("Camera opened from Settings!") }
            )
        }
    }
}

@Composable
fun SettingsCard(icon: ImageVector, title: String, onClick: () -> Unit) {
    Surface(
        modifier = Modifier.fillMaxWidth().height(56.dp).clickable { onClick() },
        shape = RoundedCornerShape(28.dp),
        color = Color.White.copy(alpha = 0.5f),
        shadowElevation = 2.dp
    ) {
        Row(modifier = Modifier.fillMaxSize().padding(horizontal = 20.dp), verticalAlignment = Alignment.CenterVertically) {
            Icon(icon, title, tint = Color.Black, modifier = Modifier.size(22.dp))
            Spacer(modifier = Modifier.width(16.dp))
            Text(title, color = Color.Black, fontSize = 16.sp, modifier = Modifier.weight(1f))
            Icon(Icons.Default.ChevronRight, "Navigate", tint = Color.Black.copy(alpha = 0.5f), modifier = Modifier.size(22.dp))
        }
    }
}