package com.example.lociapp.screens

import android.Manifest
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraSelector
import androidx.camera.view.LifecycleCameraController
import androidx.camera.view.PreviewView
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.FlashOff
import androidx.compose.material.icons.filled.FlashOn
import androidx.compose.material.icons.filled.FlipCameraAndroid
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.navigation.NavController
import coil3.compose.AsyncImage
import com.example.lociapp.ai.generateDescription
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import java.io.File
import java.util.Calendar
import androidx.compose.foundation.horizontalScroll
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.lociapp.viewmodel.ItemsViewModel
import com.example.lociapp.models.ItemEntity
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/** Keeps the photo + details so every page of the camera flow can use them. */
object CapturedPhoto {
    var uri by mutableStateOf<Uri?>(null)
    var description by mutableStateOf("")
    var reminderTime by mutableStateOf("")
    var reminderDate by mutableStateOf("")

    fun clear() {
        uri = null; description = ""; reminderTime = ""; reminderDate = ""
    }
}

private val AccentBlue = Color(0xFF58B8E8)
private val DeepBlue = Color(0xFF2F4BBF)

private fun createPhotoUri(context: Context): Uri {
    val folder = File(context.filesDir, "photos").apply { mkdirs() }
    val file = File(folder, "loci_${System.currentTimeMillis()}.jpg")
    return FileProvider.getUriForFile(context, "${context.packageName}.fileprovider", file)
}

// ===============================================================
// 1) CAMERA PAGE
// ===============================================================
@Composable
fun CameraScreen(navController: NavController) {
    val context = LocalContext.current
    var pendingUri by rememberSaveable { mutableStateOf<Uri?>(null) }

    // Result of the phone camera app
    val takePictureLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.TakePicture()
    ) { success ->
        if (success && pendingUri != null) {
            CapturedPhoto.clear()
            CapturedPhoto.uri = pendingUri
            navController.navigate("photo_confirm")
        } else {
            navController.popBackStack() // user cancelled -> go back
        }
    }

    // Opens the phone camera app
    fun openCamera() {
        val uri = createPhotoUri(context)
        pendingUri = uri
        takePictureLauncher.launch(uri)
    }

    // The "Allow camera?" pop-up
    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted ->
        if (granted) {
            openCamera()
        } else {
            Toast.makeText(context, "Camera permission is needed to take photos.", Toast.LENGTH_SHORT).show()
            navController.popBackStack()
        }
    }

    // As soon as this page opens: check permission, then open the camera
    LaunchedEffect(Unit) {
        val allowed = ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) ==
                PackageManager.PERMISSION_GRANTED
        if (allowed) openCamera() else permissionLauncher.launch(Manifest.permission.CAMERA)
    }

    // Plain black screen while the camera app opens
    Box(modifier = Modifier.fillMaxSize().background(Color.Black))
}

// ===============================================================
// 2) RETAKE PHOTO?
// ===============================================================
@Composable
fun PhotoConfirmScreen(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(listOf(Color(0xFF2A2A2D), Color(0xFF2A2A2D), Color(0xFF1B2347))))
            .windowInsetsPadding(WindowInsets.systemBars)
            .padding(horizontal = 24.dp, vertical = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Retake photo?", color = Color.White, fontSize = 28.sp, fontWeight = FontWeight.ExtraBold)

        Spacer(modifier = Modifier.height(24.dp))

        AsyncImage(
            model = CapturedPhoto.uri,
            contentDescription = "Captured photo",
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .clip(RoundedCornerShape(32.dp)),
            contentScale = ContentScale.Crop
        )

        Spacer(modifier = Modifier.height(28.dp))

        GlassButton("RETAKE", dark = true, modifier = Modifier.fillMaxWidth(0.62f)) {
            navController.popBackStack()
        }
        Spacer(modifier = Modifier.height(12.dp))
        GlassButton("PROCEED", dark = true, modifier = Modifier.fillMaxWidth(0.62f)) {
            navController.navigate("edit_photo")
        }
        Spacer(modifier = Modifier.height(8.dp))
    }
}

// ===============================================================
// 3) EDIT PHOTO
// ===============================================================
@Composable
fun EditPhotoScreen(navController: NavController) {
    val context = LocalContext.current
    val itemsVm: ItemsViewModel = viewModel()

    Box(modifier = Modifier.fillMaxSize().background(editGradient)) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .windowInsetsPadding(WindowInsets.systemBars)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp, vertical = 12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(modifier = Modifier.fillMaxWidth()) {
                RoundIconButton(Icons.AutoMirrored.Filled.ArrowBack, "Back", dark = false, size = 34.dp) {
                    navController.popBackStack()
                }
            }

            Text("Edit Photo", color = Color.Black, fontSize = 30.sp, fontWeight = FontWeight.ExtraBold)
            Text("Provide details on your photo.", color = Color.Black.copy(alpha = 0.55f), fontSize = 12.sp)

            Spacer(modifier = Modifier.height(18.dp))

            AsyncImage(
                model = CapturedPhoto.uri,
                contentDescription = "Captured photo",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(280.dp)
                    .clip(RoundedCornerShape(28.dp)),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                "ADD DESCRIPTION (OPTIONAL)",
                color = Color.White, fontSize = 10.sp, fontWeight = FontWeight.Bold,
                letterSpacing = 0.8.sp, modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(6.dp))
            OutlinedTextField(
                value = CapturedPhoto.description,
                onValueChange = { CapturedPhoto.description = it },
                placeholder = { Text("Describe your photo...", fontSize = 13.sp) },
                modifier = Modifier.fillMaxWidth().height(100.dp),
                shape = RoundedCornerShape(16.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = Color.Black,
                    unfocusedTextColor = Color.Black,
                    focusedBorderColor = Color.Transparent,
                    unfocusedBorderColor = Color.Transparent,
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White
                )
            )

            Spacer(modifier = Modifier.height(10.dp))
            Text(
                "QUICK PICK",
                color = Color.White, fontSize = 10.sp, fontWeight = FontWeight.Bold,
                letterSpacing = 0.8.sp, modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(6.dp))
            QuickPickRow()

            Spacer(modifier = Modifier.height(14.dp))

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(14.dp)) {
                Column(modifier = Modifier.weight(1f)) {
                    Text("Reminder time", color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(4.dp))
                    PickerField(CapturedPhoto.reminderTime, "Optional", null) {
                        val now = Calendar.getInstance()
                        TimePickerDialog(context, { _, h, m ->
                            val h12 = if (h % 12 == 0) 12 else h % 12
                            CapturedPhoto.reminderTime = "%d:%02d %s".format(h12, m, if (h < 12) "AM" else "PM")
                        }, now.get(Calendar.HOUR_OF_DAY), now.get(Calendar.MINUTE), false).show()
                    }
                }
                Column(modifier = Modifier.weight(1f)) {
                    Text("Reminder date", color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(4.dp))
                    PickerField(CapturedPhoto.reminderDate, "Optional", Icons.Default.CalendarMonth) {
                        val now = Calendar.getInstance()
                        DatePickerDialog(context, { _, y, m, d ->
                            CapturedPhoto.reminderDate = "%02d/%02d/%d".format(m + 1, d, y)
                        }, now.get(Calendar.YEAR), now.get(Calendar.MONTH), now.get(Calendar.DAY_OF_MONTH)).show()
                    }
                }
            }

            Spacer(modifier = Modifier.height(28.dp))

            GlassButton("SAVE PHOTO", modifier = Modifier.fillMaxWidth(0.8f)) {
                val currentDesc = CapturedPhoto.description.trim()
                val title = if (currentDesc.isNotEmpty()) {
                    val words = currentDesc.split("\\s+".toRegex()).filter { it.isNotBlank() }
                    if (words.size >= 2) "${words[0]} ${words[1]}" else words.firstOrNull() ?: "Captured Item"
                } else {
                    "Captured Item"
                }
                val nowFormatted = SimpleDateFormat("MMM dd 'at' h:mm a", Locale.getDefault()).format(Date())

                val newItem = ItemEntity(
                    title = title,
                    description = if (currentDesc.isNotEmpty()) currentDesc else "No description provided",
                    reminderTime = CapturedPhoto.reminderTime,
                    reminderDate = CapturedPhoto.reminderDate,
                    imageUri = CapturedPhoto.uri?.toString(),
                    imageRes = 0,
                    addedTime = nowFormatted,
                    isUrgent = CapturedPhoto.reminderTime.isNotEmpty()
                )
                itemsVm.addItem(newItem)
                navController.navigate("photo_saved")
            }
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
private fun PickerField(value: String, placeholder: String, icon: ImageVector?, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(40.dp)
            .clip(RoundedCornerShape(10.dp))
            .background(Color.White)
            .clickable(onClick = onClick)
            .padding(horizontal = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            if (value.isEmpty()) placeholder else value,
            color = if (value.isEmpty()) Color.Gray else Color.Black,
            fontSize = 12.sp
        )
        if (icon != null) {
            androidx.compose.material3.Icon(icon, null, tint = Color.Black, modifier = Modifier.size(16.dp))
        }
    }
}

// ===============================================================
// 4) AI GENERATING PAGE
// ===============================================================
@Composable
fun AiGeneratingScreen(navController: NavController) {
    val context = LocalContext.current
    val steps = listOf(
        "Understanding your request",
        "Processing with AI model",
        "Generating your output",
        "Finalizing results"
    )
    var done by remember { mutableIntStateOf(0) } // how many steps are finished

    // The real work: send the photo to Claude while the steps tick along
    LaunchedEffect(Unit) {
        val photo = CapturedPhoto.uri
        if (photo == null) {
            navController.popBackStack()
            return@LaunchedEffect
        }
        val request = async { generateDescription(context, photo) }
        delay(800); done = 1
        delay(1000); done = 2
        val result = request.await()
        result.onSuccess { CapturedPhoto.description = it }
        result.onFailure { Toast.makeText(context, it.message ?: "AI failed", Toast.LENGTH_LONG).show() }
        done = 3; delay(500)
        done = 4; delay(500)
        navController.popBackStack()
    }

    val progress by animateFloatAsState(done / 4f, label = "progress")

    Box(modifier = Modifier.fillMaxSize().background(editGradient)) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .windowInsetsPadding(WindowInsets.systemBars)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp, vertical = 12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(modifier = Modifier.fillMaxWidth()) {
                RoundIconButton(Icons.AutoMirrored.Filled.ArrowBack, "Back", dark = false, size = 34.dp) {
                    navController.popBackStack()
                }
            }
            Text("Edit Photo", color = Color.Black, fontSize = 30.sp, fontWeight = FontWeight.ExtraBold)
            Text("Provide details on your photo.", color = Color.Black.copy(alpha = 0.55f), fontSize = 12.sp)

            Spacer(modifier = Modifier.height(18.dp))

            // Glass card
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(30.dp))
                    .background(Color.White.copy(alpha = 0.18f))
                    .border(BorderStroke(1.dp, Color.White.copy(alpha = 0.5f)), RoundedCornerShape(30.dp))
                    .padding(18.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    "PROCESSING",
                    color = Color.White.copy(alpha = 0.9f), fontSize = 9.sp, fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp,
                    modifier = Modifier
                        .clip(RoundedCornerShape(14.dp))
                        .border(BorderStroke(1.dp, Color.White.copy(alpha = 0.5f)), RoundedCornerShape(14.dp))
                        .padding(horizontal = 26.dp, vertical = 5.dp)
                )

                Spacer(modifier = Modifier.height(18.dp))

                Box(contentAlignment = Alignment.Center, modifier = Modifier.size(96.dp)) {
                    CircularProgressIndicator(
                        modifier = Modifier.fillMaxSize(),
                        color = Color.White, trackColor = Color.White.copy(alpha = 0.3f), strokeWidth = 3.dp
                    )
                    Box(
                        modifier = Modifier.size(62.dp).clip(CircleShape).background(Color(0xFF3A3F55)),
                        contentAlignment = Alignment.Center
                    ) {
                        androidx.compose.material3.Icon(
                            Icons.Default.CameraAlt, null, tint = Color.White, modifier = Modifier.size(26.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))
                Text("Please Wait", color = Color.White, fontSize = 17.sp, fontWeight = FontWeight.Bold)
                Text("Almost there...", color = Color.White.copy(alpha = 0.7f), fontSize = 10.sp)

                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    "PROGRESS", color = Color.White.copy(alpha = 0.7f), fontSize = 8.sp,
                    fontWeight = FontWeight.Bold, letterSpacing = 1.sp,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(4.dp))
                LinearProgressIndicator(
                    progress = { progress },
                    modifier = Modifier.fillMaxWidth().height(4.dp).clip(CircleShape),
                    color = Color.White,
                    trackColor = Color.White.copy(alpha = 0.3f)
                )

                Spacer(modifier = Modifier.height(14.dp))

                // Checklist
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(18.dp))
                        .background(Color.White.copy(alpha = 0.15f))
                        .border(BorderStroke(1.dp, Color.White.copy(alpha = 0.4f)), RoundedCornerShape(18.dp))
                        .padding(14.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    steps.forEachIndexed { index, label ->
                        val finished = done > index
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            if (finished) {
                                androidx.compose.material3.Icon(
                                    Icons.Default.CheckCircle, null,
                                    tint = Color.White, modifier = Modifier.size(18.dp)
                                )
                            } else {
                                Box(
                                    modifier = Modifier.size(18.dp).clip(CircleShape)
                                        .background(Color.White.copy(alpha = 0.25f))
                                )
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(
                                label,
                                color = Color.White.copy(alpha = if (finished) 1f else 0.6f),
                                fontSize = 11.sp, fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    repeat(3) {
                        Box(modifier = Modifier.size(5.dp).clip(CircleShape).background(Color.White.copy(alpha = 0.7f)))
                    }
                }
            }

            Spacer(modifier = Modifier.height(22.dp))

            GlassButton("Cancel Generation", modifier = Modifier.fillMaxWidth(0.7f)) {
                navController.popBackStack()
            }
            Spacer(modifier = Modifier.height(10.dp))
            GlassButton("Leave Edit Page", modifier = Modifier.fillMaxWidth(0.7f)) {
                navController.navigate("home") { popUpTo("home") { inclusive = true } }
            }
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                "Your photo is safe if you leave.",
                color = Color.White.copy(alpha = 0.8f), fontSize = 9.sp
            )
        }
    }
}

// ===============================================================
// 5) PHOTO SAVED
// ===============================================================
@Composable
fun PhotoSavedScreen(navController: NavController) {
    LaunchedEffect(Unit) {
        delay(1800)
        CapturedPhoto.clear()
        navController.navigate("home") { popUpTo("home") { inclusive = true } }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    0f to Color.White,
                    0.3f to Color(0xFFE3E8F8),
                    0.55f to Color(0xFF8FA3E6),
                    1f to DeepBlue
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text("Photo saved!", color = Color.White, fontSize = 30.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                "check out the lists page for the photo",
                color = Color.White.copy(alpha = 0.9f), fontSize = 10.sp
            )
        }
    }
}

// ===============================================================
// Shared pieces
// ===============================================================
private val editGradient = Brush.verticalGradient(
    0f to Color.White,
    0.35f to Color(0xFFDCE3F8),
    0.7f to Color(0xFF5D77D6),
    1f to DeepBlue
)

@Composable
private fun RoundIconButton(
    icon: ImageVector,
    description: String,
    dark: Boolean,
    size: Dp = 38.dp,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .size(size)
            .clip(CircleShape)
            .background(if (dark) Color.Black.copy(alpha = 0.35f) else Color(0xFFB9BDC9))
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        androidx.compose.material3.Icon(icon, description, tint = Color.White, modifier = Modifier.size(size * 0.5f))
    }
}

@Composable
private fun GlassButton(
    text: String,
    modifier: Modifier = Modifier,
    dark: Boolean = false,
    onClick: () -> Unit
) {
    Box(
        modifier = modifier
            .height(48.dp)
            .clip(RoundedCornerShape(24.dp))
            .background(Color.White.copy(alpha = if (dark) 0.08f else 0.18f))
            .border(
                BorderStroke(1.dp, Color.White.copy(alpha = if (dark) 0.3f else 0.55f)),
                RoundedCornerShape(24.dp)
            )
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Text(text, color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.Bold, letterSpacing = 0.8.sp)
    }
}

private val quickPicks = listOf(
    "Wallet", "Keys", "Phone", "ID Card", "Glasses",
    "Charger", "Headphones", "Laptop", "Umbrella", "Bag"
)

@Composable
private fun QuickPickRow() {
    Row(
        modifier = Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        quickPicks.forEach { item ->
            val selected = CapturedPhoto.description == item
            Text(
                item,
                color = if (selected) Color.White else Color.Black,
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier
                    .clip(RoundedCornerShape(20.dp))
                    .background(if (selected) AccentBlue else Color.White)
                    .clickable { CapturedPhoto.description = item }
                    .padding(horizontal = 14.dp, vertical = 8.dp)
            )
        }
    }
}