package com.example.lociapp.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
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
import com.example.lociapp.models.TeamMember

@Composable
fun AboutScreen(navController: NavController) {
    val teamMembers = listOf(
        TeamMember("Shaun Piconada", "🎤"),
        TeamMember("Mark James Punongbayan", "🎤"),
        TeamMember("Shan Ramos", "🎤"),
        TeamMember("Bryan Regalado", "🎤"),
        TeamMember("Roy Wilson Rebullo", "🎤")
    )

    // ✅ Removed Gradient, added transparent background
    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp)
                .windowInsetsPadding(WindowInsets.statusBars)
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = Color.Black, // ✅ Changed to Black
                    modifier = Modifier
                        .size(28.dp)
                        .clickable { navController.popBackStack() }
                )
                Spacer(modifier = Modifier.width(16.dp))
                Text(
                    text = "About Us",
                    color = Color.Black, // ✅ Changed to Black
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White.copy(alpha = 0.5f) // ✅ Made lighter for contrast
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp)
                ) {
                    Text(
                        text = "Designers",
                        color = Color.Black, // ✅ Changed to Black
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Text(
                        text = "Meet the team!",
                        color = Color.Black.copy(alpha = 0.6f), // ✅ Changed to Dark Grey
                        fontSize = 14.sp,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )

                    teamMembers.forEach { member ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(40.dp)
                                    .clip(CircleShape)
                                    .background(Color.White.copy(alpha = 0.5f)), // ✅ Made lighter
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = member.emoji,
                                    fontSize = 20.sp
                                )
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Text(
                                text = member.name,
                                color = Color.Black, // ✅ Changed to Black
                                fontSize = 16.sp
                            )
                        }
                    }
                }
            }
        }
    }
}