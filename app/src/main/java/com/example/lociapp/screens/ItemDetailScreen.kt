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
import com.example.lociapp.components.GlassIconButton
import com.example.lociapp.components.ScreenHeader

@Composable
fun ItemDetailScreen(navController: NavController, itemId: Int) {
    data class ItemInfo(
        val title: String,
        val imageRes: Int,
        val addedTime: String,
        val needByTime: String,
        val description: String
    )

    val item = when (itemId) {
        1 -> ItemInfo(
            "Tech Book",
            R.drawable.tech_book,
            "July 11 at 5:00 PM",
            "July 12 at 10:00 AM",
            "A closed book is resting on a round wooden table next to a sofa, with a green plant in the background."
        )
        2 -> ItemInfo(
            "Camera in bag",
            R.drawable.camera,
            "July 10 at 2:00 PM",
            "Not Set",
            "Bring camera to the function. The camera at my bedroom, inside the camera bag."
        )
        3 -> ItemInfo(
            "Headphones",
            R.drawable.headphones,
            "July 11 at 9:00 AM",
            "Not Set",
            "Noise-canceling over-ear headphones sitting on the computer desk, ready for the afternoon meeting."
        )
        4 -> ItemInfo(
            "Wallet",
            R.drawable.wallet,
            "July 09 at 6:30 PM",
            "July 12 at 8:00 AM",
            "Brown leather wallet located in the top drawer of the nightstand."
        )
        5 -> ItemInfo(
            "Keys",
            R.drawable.keys,
            "July 09 at 4:45 PM",
            "July 12 at 7:30 AM",
            "Set of house and car keys hanging on the wooden key rack by the front door."
        )
        6 -> ItemInfo(
            "Laptop",
            R.drawable.laptop,
            "July 09 at 11:30 PM",
            "July 12 at 9:00 AM",
            "Silver laptop closed and charging on the dining room table."
        )
        else -> ItemInfo(
            "Item $itemId",
            R.drawable.ic_launcher_foreground,
            "Unknown date",
            "Not set",
            "No description available for this item."
        )
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
                        onClick = { /* Editing is out of scope for this UI pass */ }
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
                Image(
                    painter = painterResource(id = item.imageRes),
                    contentDescription = item.title,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
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

            Text(item.title, color = Color.Black, fontSize = 20.sp, fontWeight = FontWeight.Bold)

            Spacer(modifier = Modifier.height(10.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.AccessTime, "Added", tint = Color.Black.copy(alpha = 0.6f), modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text("Added · ${item.addedTime}", color = Color.Black.copy(alpha = 0.6f), fontSize = 13.sp)
            }

            Spacer(modifier = Modifier.height(6.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Notifications, "Need by", tint = Color(0xFFE53935), modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text("Need by : ", color = Color.Black.copy(alpha = 0.6f), fontSize = 13.sp)
                Text(item.needByTime, color = Color(0xFFE53935), fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
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
                    item.description,
                    color = Color.Black.copy(alpha = 0.8f),
                    fontSize = 13.sp,
                    lineHeight = 20.sp,
                    modifier = Modifier.padding(16.dp)
                )
            }
        }
    }
}
