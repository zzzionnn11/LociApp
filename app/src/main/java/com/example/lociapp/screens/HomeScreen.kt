package com.example.lociapp.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.Icon
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
import com.example.lociapp.models.RecentItem

@Composable
fun HomeScreen(navController: NavController) {
    val recentItems = remember {
        listOf(
            RecentItem(1, "Tech Book", "Tech Book at living room's table", "10:00 am", R.drawable.tech_book, isUrgent = true),
            RecentItem(2, "Camera in bag", "Bring camera to the function", "12:00 pm", R.drawable.camera),
            RecentItem(3, "Headphones", "Don't forget the headphones for...", "1:00 pm", R.drawable.headphones)
        )
    }

    val onCameraClick: () -> Unit = { println("Camera opened from Home!") }

    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(id = R.drawable.main_homescreen_bg),
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

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Spacer(modifier = Modifier.size(40.dp))
                Text("Loci", color = Color.Black, fontSize = 28.sp, fontWeight = FontWeight.Bold)
                GlassIconButton(
                    icon = Icons.Default.Notifications,
                    contentDescription = "Reminders",
                    onClick = { navController.navigate("reminders") }
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Search bar - super thin outline
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .clickable { navController.navigate("list") },
                shape = RoundedCornerShape(16.dp),
                color = Color.White.copy(alpha = 0.8f),
                border = BorderStroke(0.5.dp, Color.LightGray)
            ) {
                Row(modifier = Modifier.fillMaxSize().padding(horizontal = 12.dp), verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Search, "Search", tint = Color.Black, modifier = Modifier.size(20.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Search here", color = Color.Black.copy(alpha = 0.6f), fontSize = 14.sp)
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Hero photo card: most recent snap, with gallery badge + shutter button
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(220.dp)
                    .clip(RoundedCornerShape(24.dp))
            ) {
                Image(
                    painter = painterResource(id = R.drawable.glasses),
                    contentDescription = "Mini camera glasses",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )

                Box(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(12.dp)
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(Color.Black.copy(alpha = 0.3f))
                        .clickable { navController.navigate("list") },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Default.Collections, "View list", tint = Color.White, modifier = Modifier.size(18.dp))
                }

                Box(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(bottom = 14.dp)
                        .size(54.dp)
                        .border(BorderStroke(3.dp, Color.White), CircleShape)
                        .clickable { onCameraClick() },
                    contentAlignment = Alignment.Center
                ) {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(Color.White)
                    )
                }
            }

            Spacer(modifier = Modifier.height(28.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Recently Added", color = Color.Black, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                Surface(
                    modifier = Modifier.clickable { navController.navigate("list") },
                    shape = RoundedCornerShape(50),
                    color = Color.White.copy(alpha = 0.6f)
                ) {
                    Text(
                        "View All",
                        color = Color.Black.copy(alpha = 0.7f),
                        fontSize = 13.sp,
                        modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            LazyColumn(modifier = Modifier.weight(1f).padding(bottom = 80.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                items(recentItems) { item ->
                    RecentItemCard(item = item, onClick = { navController.navigate("item_detail/${item.id}") })
                }
            }
        }

        Box(modifier = Modifier.fillMaxWidth().align(Alignment.BottomCenter)) {
            BottomNavigationBar(
                currentScreen = "home",
                onHomeClick = { },
                onListClick = { navController.navigate("list") },
                onSettingsClick = { navController.navigate("settings") },
                onCameraClick = onCameraClick
            )
        }
    }
}

@Composable
fun RecentItemCard(item: RecentItem, onClick: () -> Unit) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(84.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(20.dp),
        color = Color.White.copy(alpha = 0.4f),
        border = BorderStroke(1.dp, Color.White.copy(alpha = 0.5f))
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = item.imageRes),
                contentDescription = item.title,
                modifier = Modifier
                    .size(56.dp)
                    .clip(RoundedCornerShape(14.dp)),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.Center) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(item.title, color = Color.Black, fontSize = 15.sp, fontWeight = FontWeight.Bold)
                    Text(
                        "  •  ${item.time}",
                        color = if (item.isUrgent) Color(0xFFE53935) else Color.Black.copy(alpha = 0.6f),
                        fontSize = 13.sp,
                        fontWeight = if (item.isUrgent) FontWeight.SemiBold else FontWeight.Normal
                    )
                }
                Text(item.description, color = Color.Black.copy(alpha = 0.6f), fontSize = 13.sp, maxLines = 1)
            }

            Icon(
                imageVector = Icons.Default.MoreVert,
                contentDescription = "Options",
                tint = Color.Black.copy(alpha = 0.4f),
                modifier = Modifier.size(22.dp)
            )
        }
    }
}
