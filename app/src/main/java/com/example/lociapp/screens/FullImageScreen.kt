package com.example.lociapp.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@Composable
fun FullImageScreen(navController: NavController, itemId: Int) {
    val itemDetails = when (itemId) {
        1 -> "Tech Book" to Icons.Default.Book
        2 -> "Camera in bag" to Icons.Default.PhotoCamera
        3 -> "Headphones" to Icons.Default.Headphones
        else -> "Item $itemId" to Icons.Default.Book
    }

    val (itemTitle, itemIcon) = itemDetails

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.linearGradient(
                    colors = listOf(
                        Color(0xFF1A1A2E),
                        Color(0xFF16213E)
                    )
                )
            )
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .clickable { navController.popBackStack() },
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    imageVector = itemIcon,
                    contentDescription = itemTitle,
                    tint = Color.White.copy(alpha = 0.3f),
                    modifier = Modifier.size(120.dp)
                )
                Text(
                    text = "Full Image View",
                    color = Color.White.copy(alpha = 0.5f),
                    fontSize = 18.sp,
                    modifier = Modifier.padding(top = 16.dp)
                )
                Text(
                    text = "Tap anywhere to go back",
                    color = Color.White.copy(alpha = 0.3f),
                    fontSize = 12.sp
                )
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp)
                .padding(top = 40.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Back",
                tint = Color.White,
                modifier = Modifier
                    .size(28.dp)
                    .clickable { navController.popBackStack() }
            )
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = itemTitle,
                color = Color.White,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}