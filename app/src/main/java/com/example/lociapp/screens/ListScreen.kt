package com.example.lociapp.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
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
import com.example.lociapp.models.RecentItem

@Composable
fun ListScreen(navController: NavController) {
    val items = remember {
        listOf(
            RecentItem(1, "Tech Book", "Tech Book at living room's table", "10:00 am", R.drawable.ic_launcher_foreground, isUrgent = true),
            RecentItem(2, "Camera in bag", "Bring camera to the function", "12:00 pm", R.drawable.ic_launcher_foreground),
            RecentItem(3, "Headphones", "Don't forget the headphones for...", "1:00 pm", R.drawable.ic_launcher_foreground),
            RecentItem(4, "Wallet", "Wallet in the drawer", "2:00 pm", R.drawable.ic_launcher_foreground),
            RecentItem(5, "Keys", "Keys on the hook", "3:00 pm", R.drawable.ic_launcher_foreground),
            RecentItem(6, "Laptop", "Laptop on the desk", "4:00 pm", R.drawable.ic_launcher_foreground)
        )
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp)
                .windowInsetsPadding(WindowInsets.statusBars)
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            Text("List", color = Color.Black, fontSize = 28.sp, fontWeight = FontWeight.Bold)

            Spacer(modifier = Modifier.height(16.dp))

            // Search bar - super thin outline
            Surface(
                modifier = Modifier.fillMaxWidth().height(48.dp),
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

            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier.weight(1f).padding(bottom = 80.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(items) { item ->
                    ListPhotoTile(item = item, onClick = { navController.navigate("item_detail/${item.id}") })
                }
            }
        }

        Box(modifier = Modifier.fillMaxWidth().align(Alignment.BottomCenter)) {
            BottomNavigationBar(
                currentScreen = "list",
                onHomeClick = { navController.navigate("home") },
                onListClick = { },
                onSettingsClick = { navController.navigate("settings") },
                onCameraClick = { println("Camera opened from List!") }
            )
        }
    }
}

@Composable
fun ListPhotoTile(item: RecentItem, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1f)
            .clip(RoundedCornerShape(16.dp))
            .clickable { onClick() }
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
                .padding(8.dp)
                .size(28.dp)
                .clip(CircleShape)
                .background(Color.Black.copy(alpha = 0.35f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(Icons.Default.MoreVert, "Options", tint = Color.White, modifier = Modifier.size(16.dp))
        }
    }
}