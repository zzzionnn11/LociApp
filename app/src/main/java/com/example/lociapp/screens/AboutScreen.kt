package com.example.lociapp.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.lociapp.R
import com.example.lociapp.auth.ui.AppBackgroundGradient
import com.example.lociapp.components.GlassIconButton
import com.example.lociapp.models.TeamMember

@Composable
fun AboutScreen(navController: NavController) {
    val teamMembers = listOf(
        TeamMember("Shaun Piconada", "Lead Designer", R.drawable.piconada_developer),
        TeamMember("Mark James Punongbayan", "Designer", R.drawable.punongbayan_developer),
        TeamMember("Shan Ramos", "Designer", R.drawable.ramos_developer),
        TeamMember("Roy Wilson Rebullo", "Designer", R.drawable.rebullo_developer),
        TeamMember("Bryan Regalado", "Designer", R.drawable.regalado_developer),
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(AppBackgroundGradient)
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .windowInsetsPadding(WindowInsets.statusBars),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp, vertical = 16.dp)
                ) {
                    GlassIconButton(
                        icon = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        onClick = { navController.popBackStack() },
                        containerColor = Color.LightGray.copy(alpha = 0.4f),
                        tint = Color.White
                    )
                }

                Text(
                    text = "Designers",
                    color = Color.Black,
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = "Meet the team!",
                    color = Color.Black,
                    fontSize = 18.sp,
                    modifier = Modifier.padding(top = 4.dp, bottom = 32.dp)
                )
            }

            items(teamMembers) { member ->
                TeamMemberItem(member)
                Spacer(modifier = Modifier.height(40.dp))
            }

            item {
                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }
}

@Composable
fun TeamMemberItem(member: TeamMember) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth()
    ) {
        Box(
            modifier = Modifier.size(150.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = member.role,
                color = Color.Black,
                fontSize = 12.sp,
                fontWeight = FontWeight.Black,
                modifier = Modifier
                    .align(Alignment.TopCenter)
            )

            Image(
                painter = painterResource(id = member.imageRes),
                contentDescription = member.name,
                modifier = Modifier
                    .size(110.dp)
                    .clip(CircleShape)
                    .background(Color.White),
                contentScale = ContentScale.Crop
            )
        }
        
        Text(
            text = member.name,
            color = Color.Black,
            fontSize = 22.sp,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(top = 0.dp)
        )
    }
}
