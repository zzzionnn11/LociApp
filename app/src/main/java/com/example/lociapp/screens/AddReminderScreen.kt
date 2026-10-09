package com.example.lociapp.screens

import android.Manifest
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Build
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.lociapp.R
import com.example.lociapp.components.ScreenHeader
import com.example.lociapp.models.ItemEntity
import com.example.lociapp.notifications.ReminderTime
import com.example.lociapp.viewmodel.RemindersViewModel
import com.example.lociapp.viewmodel.SaveResult
import java.util.Calendar

@Composable
fun AddReminderScreen(navController: NavController) {
    val context = LocalContext.current
    val viewModel: RemindersViewModel = viewModel()
    val items = viewModel.items.collectAsStateWithLifecycle().value

    var reminderName by remember { mutableStateOf("") }
    var reminderTime by remember { mutableStateOf("") }
    var reminderDate by remember { mutableStateOf("") }
    var chosenItem by remember { mutableStateOf<ItemEntity?>(null) }
    var note by remember { mutableStateOf("") }
    var itemMenuOpen by remember { mutableStateOf(false) }

    fun doSave() {
        viewModel.save(reminderName, reminderTime, reminderDate, chosenItem, note) { result ->
            when (result) {
                is SaveResult.Invalid ->
                    Toast.makeText(context, result.message, Toast.LENGTH_SHORT).show()
                is SaveResult.Saved -> {
                    val message = if (result.exactAlarms) {
                        "Reminder saved"
                    } else {
                        "Reminder saved. Allow \"Alarms & reminders\" for Loci in Settings for exact timing."
                    }
                    Toast.makeText(context, message, Toast.LENGTH_LONG).show()
                    navController.popBackStack()
                }
            }
        }
    }

    // Android 13+ needs the user's OK before any notification can show.
    // We save either way; without permission the reminder simply won't pop up.
    val notificationPermission = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted ->
        if (!granted) {
            Toast.makeText(context, "Notifications are off, so this reminder won't alert you.", Toast.LENGTH_LONG).show()
        }
        doSave()
    }

    fun attemptSave() {
        val needsAsk = Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
                ContextCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) !=
                android.content.pm.PackageManager.PERMISSION_GRANTED
        if (needsAsk) notificationPermission.launch(Manifest.permission.POST_NOTIFICATIONS) else doSave()
    }

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
                .windowInsetsPadding(WindowInsets.systemBars)
                .imePadding()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp)
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            ScreenHeader(onBack = { navController.popBackStack() }, title = "Add Reminder")

            Spacer(modifier = Modifier.height(32.dp))

            val fieldColors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = Color.Black,
                unfocusedTextColor = Color.Black,
                focusedBorderColor = Color.LightGray,
                unfocusedBorderColor = Color.LightGray,
                focusedContainerColor = Color.White.copy(alpha = 0.8f),
                unfocusedContainerColor = Color.White.copy(alpha = 0.8f)
            )

            Text("Reminder name", color = Color.Black.copy(alpha = 0.6f), fontSize = 12.sp)
            Spacer(modifier = Modifier.height(4.dp))
            OutlinedTextField(
                value = reminderName,
                onValueChange = { reminderName = it },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = fieldColors
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Column(modifier = Modifier.weight(1f)) {
                    Text("Reminder time", color = Color.Black.copy(alpha = 0.6f), fontSize = 12.sp)
                    Spacer(modifier = Modifier.height(4.dp))
                    PickerBox(value = reminderTime, placeholder = "Pick time", icon = null) {
                        val now = Calendar.getInstance()
                        TimePickerDialog(context, { _, hour, minute ->
                            reminderTime = ReminderTime.formatTime(hour, minute)
                        }, now.get(Calendar.HOUR_OF_DAY), now.get(Calendar.MINUTE), false).show()
                    }
                }

                Column(modifier = Modifier.weight(1f)) {
                    Text("Reminder date", color = Color.Black.copy(alpha = 0.6f), fontSize = 12.sp)
                    Spacer(modifier = Modifier.height(4.dp))
                    PickerBox(value = reminderDate, placeholder = "Pick date", icon = Icons.Default.CalendarMonth) {
                        val now = Calendar.getInstance()
                        DatePickerDialog(context, { _, year, month, day ->
                            reminderDate = ReminderTime.formatDate(year, month, day)
                        }, now.get(Calendar.YEAR), now.get(Calendar.MONTH), now.get(Calendar.DAY_OF_MONTH)).apply {
                            datePicker.minDate = System.currentTimeMillis() - 1000
                        }.show()
                    }
                }
            }
            Text(
                "No date picked = the next time it is that time.",
                color = Color.Black.copy(alpha = 0.45f),
                fontSize = 10.sp,
                modifier = Modifier.padding(top = 4.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text("Choose item", color = Color.Black.copy(alpha = 0.6f), fontSize = 12.sp)
            Spacer(modifier = Modifier.height(4.dp))
            Box {
                PickerBox(
                    value = chosenItem?.title.orEmpty(),
                    placeholder = "Optional",
                    icon = Icons.Default.ArrowDropDown
                ) { itemMenuOpen = true }

                DropdownMenu(expanded = itemMenuOpen, onDismissRequest = { itemMenuOpen = false }) {
                    DropdownMenuItem(
                        text = { Text("None") },
                        onClick = { chosenItem = null; itemMenuOpen = false }
                    )
                    items.forEach { item ->
                        DropdownMenuItem(
                            text = { Text(item.title) },
                            onClick = { chosenItem = item; itemMenuOpen = false }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text("Note - Not Required", color = Color.Black.copy(alpha = 0.5f), fontSize = 12.sp)
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = note,
                onValueChange = { note = it },
                modifier = Modifier.fillMaxWidth().height(120.dp),
                shape = RoundedCornerShape(12.dp),
                colors = fieldColors
            )

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = { attemptSave() },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color.White.copy(alpha = 0.6f))
            ) {
                Text("Save reminder", color = Color.Black, fontSize = 16.sp, fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

/** A white, tappable box that looks like the text fields but opens a picker. */
@Composable
private fun PickerBox(
    value: String,
    placeholder: String,
    icon: ImageVector?,
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(12.dp),
        color = Color.White.copy(alpha = 0.8f),
        border = BorderStroke(1.dp, Color.LightGray)
    ) {
        Row(
            modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                if (value.isEmpty()) placeholder else value,
                color = if (value.isEmpty()) Color.Gray else Color.Black,
                fontSize = 16.sp,
                maxLines = 1,
                modifier = Modifier.weight(1f)
            )
            if (icon != null) {
                Icon(icon, null, tint = Color.Black.copy(alpha = 0.6f))
            }
        }
    }
}
