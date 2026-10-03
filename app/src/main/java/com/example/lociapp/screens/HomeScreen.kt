package com.example.lociapp.screens

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.view.LifecycleCameraController
import androidx.camera.view.PreviewView
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
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import coil3.compose.AsyncImage
import com.example.lociapp.R
import com.example.lociapp.components.BottomNavigationBar
import com.example.lociapp.components.GlassIconButton
import com.example.lociapp.db.ItemRepository
import com.example.lociapp.models.ItemEntity
import java.io.File
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties

private var cameraPermissionAsked = false

@Composable
fun HomeScreen(navController: NavController) {
    val itemsList by ItemRepository.items.collectAsState()
    val recentItems = remember(itemsList) { itemsList.take(3) }

    val context = LocalContext.current
    val activity = context as ComponentActivity

    var hasPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) ==
                    PackageManager.PERMISSION_GRANTED
        )
    }
    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted -> hasPermission = granted }

    // Ask for camera permission the first time Home opens after login
    LaunchedEffect(Unit) {
        if (!hasPermission && !cameraPermissionAsked) {
            cameraPermissionAsked = true
            permissionLauncher.launch(Manifest.permission.CAMERA)
        }
    }

    // Live camera
    val cameraController = remember { LifecycleCameraController(context) }
    DisposableEffect(Unit) { onDispose { cameraController.unbind() } }

    // Take the photo, then go to the confirmation page
    fun takePhoto() {
        val folder = File(context.filesDir, "photos").apply { mkdirs() }
        val file = File(folder, "loci_${System.currentTimeMillis()}.jpg")
        val options = ImageCapture.OutputFileOptions.Builder(file).build()
        cameraController.takePicture(
            options,
            ContextCompat.getMainExecutor(context),
            object : ImageCapture.OnImageSavedCallback {
                override fun onImageSaved(result: ImageCapture.OutputFileResults) {
                    CapturedPhoto.clear()
                    CapturedPhoto.uri = Uri.fromFile(file)
                    navController.navigate("photo_confirm")
                }

                override fun onError(exception: ImageCaptureException) {
                    Toast.makeText(context, "Could not take photo", Toast.LENGTH_SHORT).show()
                }
            }
        )
    }

    val onCameraClick: () -> Unit = {
        if (hasPermission) takePhoto() else permissionLauncher.launch(Manifest.permission.CAMERA)
    }

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
                if (hasPermission) {
                    AndroidView(
                        modifier = Modifier.fillMaxSize(),
                        factory = { ctx ->
                            PreviewView(ctx).apply {
                                implementationMode = PreviewView.ImplementationMode.COMPATIBLE
                                scaleType = PreviewView.ScaleType.FILL_CENTER
                                controller = cameraController
                                cameraController.bindToLifecycle(activity)
                            }
                        }
                    )
                } else {
                    Image(
                        painter = painterResource(id = R.drawable.glasses),
                        contentDescription = "Mini camera glasses",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                }

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
                items(recentItems, key = { it.id }) { item ->
                    RecentItemCard(
                        item = item,
                        onEditClick = { navController.navigate("edit_item/${item.id}") },
                        onDeleteClick = {
                            ItemRepository.deleteItem(item.id)
                            Toast.makeText(context, "Item deleted", Toast.LENGTH_SHORT).show()
                        }
                    )
                }
            }
        }

        Box(modifier = Modifier.fillMaxWidth().align(Alignment.BottomCenter)) {
            BottomNavigationBar(
                currentScreen = "home",
                onHomeClick = { },
                onListClick = { navController.navigate("list") },
                onSettingsClick = { navController.navigate("settings") },
                onCameraClick = { navController.navigate("camera") }
            )
        }
    }
}

@Composable
fun RecentItemCard(
    item: ItemEntity,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    var showMenu by remember { mutableStateOf(false) }

    val displayTime = when {
        item.reminderTime.isNotEmpty() -> item.reminderTime
        item.addedTime.isNotEmpty() -> item.addedTime
        else -> "Recently"
    }

    Box(modifier = Modifier.fillMaxWidth()) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .height(84.dp),
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
                if (!item.imageUri.isNullOrEmpty()) {
                    AsyncImage(
                        model = item.imageUri,
                        contentDescription = item.title,
                        modifier = Modifier
                            .size(56.dp)
                            .clip(RoundedCornerShape(14.dp)),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    val imgRes =
                        if (item.imageRes != 0) item.imageRes else R.drawable.ic_launcher_foreground
                    Image(
                        painter = painterResource(id = imgRes),
                        contentDescription = item.title,
                        modifier = Modifier
                            .size(56.dp)
                            .clip(RoundedCornerShape(14.dp)),
                        contentScale = ContentScale.Crop
                    )
                }

                Spacer(modifier = Modifier.width(16.dp))

                Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.Center) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            item.title,
                            color = Color.White,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            "  •  $displayTime",
                            color = if (item.isUrgent) Color(0xFFE53935) else Color.White.copy(alpha = 0.7f),
                            fontSize = 13.sp,
                            fontWeight = if (item.isUrgent) FontWeight.SemiBold else FontWeight.Normal
                        )
                    }
                    Text(
                        item.description,
                        color = Color.White.copy(alpha = 0.7f),
                        fontSize = 13.sp,
                        maxLines = 1
                    )
                }

                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .clip(CircleShape)
                        .background(Color.White.copy(alpha = 0.2f))
                        .clickable { showMenu = !showMenu },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.MoreVert,
                        contentDescription = "Options",
                        tint = Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }

        if (showMenu) {
            Popup(
                alignment = Alignment.CenterEnd,
                onDismissRequest = { showMenu = false },
                properties = PopupProperties(focusable = true)
            ) {
                Surface(
                    modifier = Modifier
                        .padding(end = 12.dp)
                        .clip(RoundedCornerShape(20.dp)),
                    color = Color.White.copy(alpha = 0.35f),
                    border = BorderStroke(1.dp, Color.White.copy(alpha = 0.6f)),
                    shape = RoundedCornerShape(20.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Row(
                            modifier = Modifier.clickable {
                                showMenu = false
                                onEditClick()
                            },
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Edit,
                                contentDescription = "Edit",
                                tint = Color.Black,
                                modifier = Modifier.size(16.dp)
                            )
                            Text(
                                text = "Edit",
                                color = Color.White,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Row(
                            modifier = Modifier.clickable {
                                showMenu = false
                                onDeleteClick()
                            },
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = "Delete",
                                tint = Color(0xFFE53935),
                                modifier = Modifier.size(16.dp)
                            )
                            Text(
                                text = "Delete",
                                color = Color.White,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }
    }
}
