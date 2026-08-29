package com.example.lociapp.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.lociapp.components.BottomNavigationBar
import com.example.lociapp.models.ReminderItem

@Composable
fun RemindersScreen(navController: NavController) {
    val reminders = remember {
        listOf(
            ReminderItem(1, "Tech Book", "URGENT NEEDED AT 10:00 AM!", "10:00 AM", isUrgent = true),
            ReminderItem(2, "Camera in bag", "Bring camera to the function", "12:00 PM", isUrgent = false),
            ReminderItem(3, "Headphones", "Don't forget the headphones for work", "1:00 PM", isUrgent = false)
        )
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp)
                .windowInsetsPadding(WindowInsets.statusBars)
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = Color.Black,
                    modifier = Modifier.size(28.dp).clickable { navController.popBackStack() }
                )
                Text("Reminders", color = Color.Black, fontSize = 24.sp, fontWeight = FontWeight.Bold)
                Surface(
                    modifier = Modifier.clickable { navController.navigate("add_reminder") },
                    shape = RoundedCornerShape(12.dp),
                    color = Color.White.copy(alpha = 0.5f)
                ) {
                    Row(modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp), verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Add, "Add Reminder", tint = Color.Black, modifier = Modifier.size(20.dp))
                        Text("Add Reminder", color = Color.Black, fontSize = 14.sp)
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text("Today", color = Color.Black.copy(alpha = 0.6f), fontSize = 14.sp, fontWeight = FontWeight.Medium)
            Spacer(modifier = Modifier.height(12.dp))

            LazyColumn(modifier = Modifier.weight(1f).padding(bottom = 80.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                items(reminders) { reminder ->
                    ReminderCard(reminder = reminder)
                }
            }
        }

        Box(modifier = Modifier.fillMaxWidth().align(Alignment.BottomCenter)) {
            BottomNavigationBar(
                currentScreen = "reminders",
                onHomeClick = { navController.navigate("home") },
                onListClick = { navController.navigate("list") },
                onSettingsClick = { navController.navigate("settings") },
                onCameraClick = { println("Camera opened from Reminders!") }
            )
        }
    }
}

@Composable
fun ReminderCard(reminder: ReminderItem) {
    Surface(
        modifier = Modifier.fillMaxWidth().height(80.dp),
        shape = RoundedCornerShape(16.dp),
        color = if (reminder.isUrgent) Color(0xFFFF4444).copy(alpha = 0.3f) else Color.White.copy(alpha = 0.5f),
        shadowElevation = 4.dp
    ) {
        Row(modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp), verticalAlignment = Alignment.CenterVertically) {
            Surface(
                modifier = Modifier.size(48.dp),
                shape = RoundedCornerShape(12.dp),
                color = if (reminder.isUrgent) Color(0xFFFF4444).copy(alpha = 0.4f) else Color.White.copy(alpha = 0.4f)
            ) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = if (reminder.isUrgent) Icons.Default.Warning else Icons.Default.Notifications,
                        contentDescription = reminder.title,
                        tint = if (reminder.isUrgent) Color.Black else Color.Black,
                        modifier = Modifier.size(28.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.Center) {
                Text(reminder.title, color = Color.Black, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                Text(
                    reminder.description,
                    color = if (reminder.isUrgent) Color.Red else Color.Black.copy(alpha = 0.6f),
                    fontSize = 12.sp,
                    maxLines = 1
                )
            }

            Text(reminder.time, color = Color.Black.copy(alpha = 0.6f), fontSize = 12.sp)
        }
    }
}