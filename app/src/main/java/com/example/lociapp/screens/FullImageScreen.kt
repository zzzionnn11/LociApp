package com.example.lociapp.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil3.compose.AsyncImage
import com.example.lociapp.R
import com.example.lociapp.components.GlassIconButton
import com.example.lociapp.db.ItemRepository

@Composable
fun FullImageScreen(navController: NavController, itemId: Int) {
    val dbItem = remember(itemId) { ItemRepository.getItemById(itemId) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.linearGradient(
                    colors = listOf(Color(0xFF1A1A2E), Color(0xFF16213E))
                )
            )
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp)
                .padding(top = 64.dp)
                .clip(RoundedCornerShape(20.dp))
                .clickable { navController.popBackStack() }
        ) {
            if (dbItem != null && !dbItem.imageUri.isNullOrEmpty()) {
                AsyncImage(
                    model = dbItem.imageUri,
                    contentDescription = dbItem.title,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            } else {
                val imageRes = dbItem?.imageRes?.takeIf { it != 0 } ?: R.drawable.ic_launcher_foreground
                Image(
                    painter = painterResource(id = imageRes),
                    contentDescription = dbItem?.title ?: "Full item image",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            }
        }

        GlassIconButton(
            icon = Icons.AutoMirrored.Filled.ArrowBack,
            contentDescription = "Back",
            onClick = { navController.popBackStack() },
            tint = Color.White,
            containerColor = Color.White.copy(alpha = 0.2f),
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(start = 24.dp, top = 40.dp)
        )
    }
}
