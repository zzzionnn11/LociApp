package com.example.lociapp.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.lociapp.components.BottomNavigationBar
import com.example.lociapp.components.GlassIconButton
import com.example.lociapp.components.ScreenHeader

@Composable
fun ProfileScreen(navController: NavController) {
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
                trailing = {
                    GlassIconButton(icon = Icons.Default.Edit, contentDescription = "Edit profile", onClick = { })
                }
            )

            Spacer(modifier = Modifier.height(32.dp))

            Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
                Box(contentAlignment = Alignment.BottomEnd) {
                    Box(
                        modifier = Modifier
                            .size(100.dp)
                            .clip(CircleShape)
                            .background(Color.White.copy(alpha = 0.5f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.Person, "Profile", tint = Color.Black, modifier = Modifier.size(60.dp))
                    }
                    GlassIconButton(
                        icon = Icons.Default.Edit,
                        contentDescription = "Change photo",
                        onClick = { },
                        size = 28.dp,
                        iconSize = 14.dp
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text("John Michael Santos", color = Color.Black, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                Text("User since July 11, 2026", color = Color.Black.copy(alpha = 0.6f), fontSize = 14.sp)

                Spacer(modifier = Modifier.height(32.dp))

                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp)
                        .clickable { /* TODO: Sign out */ },
                    shape = RoundedCornerShape(26.dp),
                    color = Color.White.copy(alpha = 0.6f)
                ) {
                    Row(
                        modifier = Modifier.fillMaxSize(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.AutoMirrored.Filled.Logout, "Sign Out", tint = Color.Black, modifier = Modifier.size(20.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Sign out", color = Color.Black, fontSize = 15.sp, fontWeight = FontWeight.Medium)
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Delete Account",
                    color = Color(0xFFE53935),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.clickable { /* TODO: Delete account */ }
                )
            }
        }

        Box(modifier = Modifier.fillMaxWidth().align(Alignment.BottomCenter)) {
            BottomNavigationBar(
                currentScreen = "profile",
                onHomeClick = { navController.navigate("home") },
                onListClick = { navController.navigate("list") },
                onSettingsClick = { navController.navigate("settings") },
                onCameraClick = { println("Camera opened from Profile!") }
            )
        }
    }
}