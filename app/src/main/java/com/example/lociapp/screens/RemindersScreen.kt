package com.example.lociapp.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.lociapp.R
import com.example.lociapp.components.BottomNavigationBar
import com.example.lociapp.components.GlassIconButton
import com.example.lociapp.components.ScreenHeader
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

            ScreenHeader(
                onBack = { navController.popBackStack() },
                title = "Reminders",
                trailing = {
                    GlassIconButton(
                        icon = Icons.Default.Add,
                        contentDescription = "Add Reminder",
                        onClick = { navController.navigate("add_reminder") }
                    )
                }
            )

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
        modifier = Modifier.fillMaxWidth().height(84.dp),
        shape = RoundedCornerShape(16.dp),
        color = if (reminder.isUrgent) Color(0xFFFF4444).copy(alpha = 0.15f) else Color.White.copy(alpha = 0.5f),
        shadowElevation = 4.dp
    ) {
        Row(modifier = Modifier.fillMaxSize().padding(horizontal = 12.dp), verticalAlignment = Alignment.CenterVertically) {
            Image(
                painter = painterResource(id = R.drawable.ic_launcher_foreground),
                contentDescription = reminder.title,
                modifier = Modifier.size(52.dp).clip(RoundedCornerShape(12.dp)),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.Center) {
                if (reminder.isUrgent) {
                    Text("TIME SENSITIVE", color = Color(0xFFE53935), fontSize = 10.sp, fontWeight = FontWeight.Bold)
                }
                Text(reminder.title, color = Color.Black, fontSize = 15.sp, fontWeight = FontWeight.Bold)
                Text(
                    reminder.description,
                    color = if (reminder.isUrgent) Color(0xFFE53935) else Color.Black.copy(alpha = 0.6f),
                    fontSize = 12.sp,
                    maxLines = 1,
                    fontWeight = if (reminder.isUrgent) FontWeight.SemiBold else FontWeight.Normal
                )
            }
        }
    }
}