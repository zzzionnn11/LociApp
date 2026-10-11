package com.example.lociapp.screens

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil3.compose.AsyncImage
import com.example.lociapp.R
import com.example.lociapp.components.BottomNavigationBar
import com.example.lociapp.components.GlassIconButton
import com.example.lociapp.components.ScreenHeader
import com.example.lociapp.models.ItemEntity
import com.example.lociapp.models.ReminderEntity
import com.example.lociapp.viewmodel.RemindersViewModel

@Composable
fun RemindersScreen(navController: NavController) {
    val context = LocalContext.current
    val viewModel: RemindersViewModel = viewModel()
    val allReminders = viewModel.reminders.collectAsStateWithLifecycle().value
    val itemsList = viewModel.items.collectAsStateWithLifecycle().value

    // Deduplicate reminders so each item/title appears exactly once.
    // Prefers manually added reminders (!fromItem) or the newest reminder.
    val reminders = remember(allReminders) {
        allReminders
            .sortedWith(compareBy<ReminderEntity> { it.fromItem }.thenByDescending { it.id })
            .distinctBy { reminder ->
                if (reminder.itemId != 0) "item_${reminder.itemId}" else "title_${reminder.title.trim().lowercase()}"
            }
    }

    // Split into sections. Recomputed whenever the list changes.
    val now = System.currentTimeMillis()
    val endOfToday = remember(reminders) {
        java.util.Calendar.getInstance().apply {
            set(java.util.Calendar.HOUR_OF_DAY, 23); set(java.util.Calendar.MINUTE, 59)
            set(java.util.Calendar.SECOND, 59); set(java.util.Calendar.MILLISECOND, 999)
        }.timeInMillis
    }
    val earlier = reminders.filter { it.triggerAtMillis < now }.sortedByDescending { it.triggerAtMillis }
    val today = reminders.filter { it.triggerAtMillis in now..endOfToday }
    val upcoming = reminders.filter { it.triggerAtMillis > endOfToday }

    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(id = R.drawable.add_reminders_bg),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
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

            if (reminders.isEmpty()) {
                Text(
                    "No reminders yet. Tap + to add one.",
                    color = Color.Black.copy(alpha = 0.6f),
                    fontSize = 14.sp
                )
            }

            LazyColumn(
                modifier = Modifier.weight(1f).padding(bottom = 80.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                fun section(title: String, list: List<ReminderEntity>) {
                    if (list.isEmpty()) return
                    item(key = "header_$title") {
                        Text(
                            title,
                            color = Color.Black.copy(alpha = 0.6f),
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium,
                            modifier = Modifier.padding(top = 4.dp, bottom = 4.dp)
                        )
                    }
                    items(list, key = { it.id }) { reminder ->
                        SwipeToDeleteReminder(
                            reminder = reminder,
                            item = itemsList.firstOrNull { it.id == reminder.itemId },
                            onDelete = {
                                viewModel.delete(reminder.id)
                                Toast.makeText(context, "Reminder deleted", Toast.LENGTH_SHORT).show()
                            }
                        )
                    }
                }
                section("Today", today)
                section("Upcoming", upcoming)
                section("Earlier", earlier)

                if (reminders.isNotEmpty()) {
                    item(key = "hint") {
                        Text(
                            "Swipe a reminder left to delete it.",
                            color = Color.Black.copy(alpha = 0.4f),
                            fontSize = 11.sp,
                            modifier = Modifier.padding(top = 8.dp)
                        )
                    }
                }
            }
        }

        Box(modifier = Modifier.fillMaxWidth().align(Alignment.BottomCenter)) {
            BottomNavigationBar(
                currentScreen = "reminders",
                onHomeClick = { navController.navigate("home") },
                onListClick = { navController.navigate("list") },
                onSettingsClick = { navController.navigate("settings") },
                onCameraClick = { navController.navigate("camera") }
            )
        }
    }
}

@Composable
private fun SwipeToDeleteReminder(
    reminder: ReminderEntity,
    item: ItemEntity?,
    onDelete: () -> Unit
) {
    val dismissState = rememberSwipeToDismissBoxState(
        confirmValueChange = { value ->
            if (value == SwipeToDismissBoxValue.EndToStart) {
                onDelete()
                true
            } else {
                false
            }
        }
    )

    SwipeToDismissBox(
        state = dismissState,
        enableDismissFromStartToEnd = false,
        backgroundContent = {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(20.dp))
                    .background(Color(0xFFFF4444).copy(alpha = 0.85f))
                    .padding(end = 24.dp),
                contentAlignment = Alignment.CenterEnd
            ) {
            }
        }
    ) {
        ReminderCard(reminder = reminder, item = item, onDelete = onDelete)
    }
}

@Composable
fun ReminderCard(
    reminder: ReminderEntity,
    item: ItemEntity?,
    onDelete: () -> Unit = {}
) {
    val urgent = reminder.isUrgent()
    val subtitle = when {
        urgent -> "URGENT NEEDED AT ${reminder.time.uppercase()}!"
        reminder.description.isNotBlank() -> reminder.description
        else -> listOf(reminder.date, reminder.time).filter { it.isNotBlank() }.joinToString(" at ")
    }

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(84.dp),
        shape = RoundedCornerShape(20.dp),
        color = if (urgent) Color.White.copy(alpha = 0.15f) else Color.White.copy(alpha = 0.4f),
        border = BorderStroke(
            width = if (urgent) 1.5.dp else 1.dp,
            color = Color.White.copy(alpha = 0.5f)
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            ReminderThumbnail(item, reminder.title)

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.Center) {
                if (urgent) {
                    Text("TIME SENSITIVE", color = Color.Black, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                }
                Text(reminder.title, color = Color.Black, fontSize = 16.sp, fontWeight = FontWeight.Bold, maxLines = 1)
                Text(
                    subtitle,
                    color = if (urgent) Color.Black else Color.Black.copy(alpha = 0.6f),
                    fontSize = 13.sp,
                    maxLines = 1,
                    fontWeight = if (urgent) FontWeight.SemiBold else FontWeight.Normal
                )
            }

            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .clickable { onDelete() },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Delete reminder",
                    tint = Color.White.copy(alpha = 0.8f),
                    modifier = Modifier.size(20.dp)
                )
            }
        }    }
}
@Composable
private fun ReminderThumbnail(item: ItemEntity?, title: String) {
    val modifier = Modifier
        .size(60.dp)
        .clip(RoundedCornerShape(14.dp))

    when {
        item != null && !item.imageUri.isNullOrEmpty() -> AsyncImage(
            model = item.imageUri,
            contentDescription = title,
            modifier = modifier,
            contentScale = ContentScale.Crop
        )
        item != null && item.imageRes != 0 -> Image(
            painter = painterResource(id = item.imageRes),
            contentDescription = title,
            modifier = modifier,
            contentScale = ContentScale.Crop
        )
        else -> Box(
            modifier = modifier.background(Color.White.copy(alpha = 0.6f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(Icons.Default.Notifications, null, tint = Color.Black.copy(alpha = 0.6f))
        }
    }
}
