package com.example.lociapp.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.NavController
import coil3.compose.AsyncImage
import com.example.lociapp.R
import com.example.lociapp.components.GlassIconButton
import com.example.lociapp.components.ScreenHeader
import com.example.lociapp.db.ItemRepository

@Composable
fun ItemDetailScreen(navController: NavController, itemId: Int) {
    val itemsList by ItemRepository.items.collectAsState()
    val dbItem = remember(itemsList, itemId) { ItemRepository.getItemById(itemId) }

    val title = dbItem?.title ?: "Item $itemId"
    val description = dbItem?.description ?: "No description available."
    val addedTime = dbItem?.addedTime?.ifEmpty { "Recently" } ?: "Recently"
    val needByTime = when {
        dbItem == null -> "Not Set"
        dbItem.reminderDate.isNotEmpty() && dbItem.reminderTime.isNotEmpty() -> "${dbItem.reminderDate} at ${dbItem.reminderTime}"
        dbItem.reminderTime.isNotEmpty() -> dbItem.reminderTime
        dbItem.reminderDate.isNotEmpty() -> dbItem.reminderDate
        else -> "Not Set"
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(id = R.drawable.item_page_bg),
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
                trailing = {
                    GlassIconButton(
                        icon = Icons.Default.Edit,
                        contentDescription = "Edit item",
                        onClick = { navController.navigate("edit_item/$itemId") }
                    )
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(220.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .clickable { navController.navigate("full_image/$itemId") }
            ) {
                if (dbItem != null && !dbItem.imageUri.isNullOrEmpty()) {
                    AsyncImage(
                        model = dbItem.imageUri,
                        contentDescription = title,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    val imageRes = dbItem?.imageRes?.takeIf { it != 0 } ?: R.drawable.ic_launcher_foreground
                    Image(
                        painter = painterResource(id = imageRes),
                        contentDescription = title,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                }

                Box(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(10.dp)
                        .size(32.dp)
                        .clip(CircleShape)
                        .background(Color.Black.copy(alpha = 0.3f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Default.Collections, "View full image", tint = Color.White, modifier = Modifier.size(16.dp))
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(title, color = Color.Black, fontSize = 20.sp, fontWeight = FontWeight.Bold)

            Spacer(modifier = Modifier.height(10.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.AccessTime, "Added", tint = Color.Black.copy(alpha = 0.6f), modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text("Added · $addedTime", color = Color.Black.copy(alpha = 0.6f), fontSize = 13.sp)
            }

            Spacer(modifier = Modifier.height(6.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Notifications, "Need by", tint = Color(0xFFE53935), modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text("Need by : ", color = Color.Black.copy(alpha = 0.6f), fontSize = 13.sp)
                Text(needByTime, color = Color(0xFFE53935), fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Spacer(modifier = Modifier.width(6.dp))
                Text("Description", color = Color.Black, fontSize = 14.sp, fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.height(8.dp))

            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                color = Color.White.copy(alpha = 0.5f)
            ) {
                Text(
                    description,
                    color = Color.Black.copy(alpha = 0.8f),
                    fontSize = 13.sp,
                    lineHeight = 20.sp,
                    modifier = Modifier.padding(16.dp)
                )
            }
        }
    }
}
