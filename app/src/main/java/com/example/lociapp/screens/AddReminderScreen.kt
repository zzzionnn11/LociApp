package com.example.lociapp.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.lociapp.R
import com.example.lociapp.components.ScreenHeader

@Composable
fun AddReminderScreen(navController: NavController) {
    var reminderName by remember { mutableStateOf("") }
    var reminderTime by remember { mutableStateOf("") }
    var reminderDate by remember { mutableStateOf("") }
    var chosenItem by remember { mutableStateOf("") }

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

            ScreenHeader(onBack = { navController.popBackStack() }, title = "Add Reminder")

            Spacer(modifier = Modifier.height(32.dp))

            val fieldColors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = Color.Black,
                unfocusedTextColor = Color.Black,
                focusedBorderColor = Color.LightGray,
                unfocusedBorderColor = Color.LightGray,
                focusedContainerColor = Color.White.copy(alpha = 0.8f),
                unfocusedContainerColor = Color.White.copy(alpha = 0.8f),
                focusedLabelColor = Color.Black.copy(alpha = 0.7f),
                unfocusedLabelColor = Color.Black.copy(alpha = 0.5f)
            )

            Text("Reminder name", color = Color.Black.copy(alpha = 0.6f), fontSize = 12.sp)
            Spacer(modifier = Modifier.height(4.dp))
            OutlinedTextField(
                value = reminderName,
                onValueChange = { reminderName = it },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = fieldColors
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Column(modifier = Modifier.weight(1f)) {
                    Text("Reminder time", color = Color.Black.copy(alpha = 0.6f), fontSize = 12.sp)
                    Spacer(modifier = Modifier.height(4.dp))
                    OutlinedTextField(
                        value = reminderTime,
                        onValueChange = { reminderTime = it },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = fieldColors
                    )
                }

                Column(modifier = Modifier.weight(1f)) {
                    Text("Reminder date", color = Color.Black.copy(alpha = 0.6f), fontSize = 12.sp)
                    Spacer(modifier = Modifier.height(4.dp))
                    OutlinedTextField(
                        value = reminderDate,
                        onValueChange = { reminderDate = it },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = fieldColors,
                        trailingIcon = { Icon(Icons.Default.CalendarMonth, "Date", tint = Color.Black.copy(alpha = 0.5f)) }
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text("Choose item", color = Color.Black.copy(alpha = 0.6f), fontSize = 12.sp)
            Spacer(modifier = Modifier.height(4.dp))
            OutlinedTextField(
                value = chosenItem,
                onValueChange = { chosenItem = it },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = fieldColors,
                trailingIcon = { Icon(Icons.Default.ArrowDropDown, "Dropdown", tint = Color.Black.copy(alpha = 0.5f)) }
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text("Note - Not Required", color = Color.Black.copy(alpha = 0.5f), fontSize = 12.sp)
            Spacer(modifier = Modifier.height(8.dp))

            Surface(
                modifier = Modifier.fillMaxWidth().height(120.dp),
                shape = RoundedCornerShape(12.dp),
                color = Color.White.copy(alpha = 0.8f),
                border = BorderStroke(0.5.dp, Color.LightGray)
            ) {}

            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = { navController.popBackStack() },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .padding(bottom = 16.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color.White.copy(alpha = 0.6f))
            ) {
                Text("Save reminder", color = Color.Black, fontSize = 16.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}