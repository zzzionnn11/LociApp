package com.example.lociapp.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import com.example.lociapp.auth.ui.AppBackgroundGradient
import com.example.lociapp.components.BottomNavigationBar
import com.example.lociapp.components.GlassIconButton

@Composable
fun ProfileScreen(navController: NavController) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(AppBackgroundGradient)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp)
                .windowInsetsPadding(WindowInsets.statusBars)
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            // Custom Header to match the image precisely
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                GlassIconButton(
                    icon = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    onClick = { navController.popBackStack() },
                    tint = Color.White,
                    containerColor = Color.White.copy(alpha = 0.2f)
                )
                
                GlassIconButton(
                    icon = Icons.Default.Edit,
                    contentDescription = "Edit profile",
                    onClick = { },
                    tint = Color.White,
                    containerColor = Color.White.copy(alpha = 0.2f)
                )
            }

            Spacer(modifier = Modifier.height(40.dp))

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                // Profile Image
                Image(
                    painter = painterResource(id = R.drawable.user_profile),
                    contentDescription = "Profile Picture",
                    modifier = Modifier
                        .size(200.dp)
                        .clip(CircleShape)
                        .background(Color.White.copy(alpha = 0.2f)),
                    contentScale = ContentScale.Crop
                )

                Spacer(modifier = Modifier.height(28.dp))

                // Name and Subtext in White
                Text(
                    text = "John Michael Santos",
                    color = Color.White,
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold
                )
                
                Text(
                    text = "User since July 11, 2026",
                    color = Color.Black.copy(alpha = 0.8f),
                    fontSize = 16.sp,
                    modifier = Modifier.padding(top = 4.dp)
                )

                Spacer(modifier = Modifier.height(64.dp))

                // Sign out Button (Frosted Pill)
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                        .clickable { 
                             navController.navigate("welcome") {
                                 popUpTo(0) { inclusive = true }
                             }
                        },
                    shape = RoundedCornerShape(28.dp),
                    color = Color.White.copy(alpha = 0.15f),
                    border = BorderStroke(1.dp, Color.White.copy(alpha = 0.3f))
                ) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Sign out",
                            color = Color.White,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Delete Account Link
                Text(
                    text = "Delete Account",
                    color = Color(0xFFFF5252), // Bright red to stand out on blue
                    fontSize = 16.sp,
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
