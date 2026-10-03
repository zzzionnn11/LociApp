package com.example.lociapp.screens

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil3.compose.AsyncImage
import com.example.lociapp.db.ItemRepository
import java.util.Calendar

private val AccentBlue = Color(0xFF58B8E8)
private val DeepBlue = Color(0xFF2F4BBF)
private val editGradient = Brush.verticalGradient(
    0f to Color.White,
    0.35f to Color(0xFFDCE3F8),
    0.7f to Color(0xFF5D77D6),
    1f to DeepBlue
)

private val quickPicks = listOf(
    "Wallet", "Keys", "Phone", "ID Card", "Glasses",
    "Charger", "Headphones", "Laptop", "Umbrella", "Bag"
)

@Composable
fun EditItemScreen(navController: NavController, itemId: Int) {
    val context = LocalContext.current
    val item = remember(itemId) { ItemRepository.getItemById(itemId) }

    if (item == null) {
        LaunchedEffect(Unit) {
            Toast.makeText(context, "Item not found", Toast.LENGTH_SHORT).show()
            navController.popBackStack()
        }
        return
    }

    var title by remember { mutableStateOf(item.title) }
    var description by remember { mutableStateOf(item.description) }
    var reminderTime by remember { mutableStateOf(item.reminderTime) }
    var reminderDate by remember { mutableStateOf(item.reminderDate) }

    Box(modifier = Modifier.fillMaxSize().background(editGradient)) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .windowInsetsPadding(WindowInsets.systemBars)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp, vertical = 12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                RoundIconButton(Icons.AutoMirrored.Filled.ArrowBack, "Back", dark = false, size = 34.dp) {
                    navController.popBackStack()
                }

                RoundIconButton(Icons.Default.Delete, "Delete", dark = true, size = 34.dp) {
                    ItemRepository.deleteItem(itemId)
                    Toast.makeText(context, "Item deleted", Toast.LENGTH_SHORT).show()
                    navController.navigate("list") {
                        popUpTo("list") { inclusive = true }
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text("Edit Item", color = Color.Black, fontSize = 30.sp, fontWeight = FontWeight.ExtraBold)
            Text("Update description, time needed, or title.", color = Color.Black.copy(alpha = 0.55f), fontSize = 12.sp)

            Spacer(modifier = Modifier.height(18.dp))

            if (!item.imageUri.isNullOrEmpty()) {
                AsyncImage(
                    model = item.imageUri,
                    contentDescription = title,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(240.dp)
                        .clip(RoundedCornerShape(28.dp)),
                    contentScale = ContentScale.Crop
                )
            } else if (item.imageRes != 0) {
                Image(
                    painter = painterResource(id = item.imageRes),
                    contentDescription = title,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(240.dp)
                        .clip(RoundedCornerShape(28.dp)),
                    contentScale = ContentScale.Crop
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                "TITLE",
                color = Color.White, fontSize = 10.sp, fontWeight = FontWeight.Bold,
                letterSpacing = 0.8.sp, modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(6.dp))
            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                placeholder = { Text("Item title...", fontSize = 13.sp) },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
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

            Spacer(modifier = Modifier.height(14.dp))

            Text(
                "DESCRIPTION",
                color = Color.White, fontSize = 10.sp, fontWeight = FontWeight.Bold,
                letterSpacing = 0.8.sp, modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(6.dp))
            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                placeholder = { Text("Describe your item...", fontSize = 13.sp) },
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

            Spacer(modifier = Modifier.height(14.dp))

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(14.dp)) {
                Column(modifier = Modifier.weight(1f)) {
                    Text("Reminder time", color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(4.dp))
                    PickerField(reminderTime, "Optional", null) {
                        val now = Calendar.getInstance()
                        TimePickerDialog(context, { _, h, m ->
                            val h12 = if (h % 12 == 0) 12 else h % 12
                            reminderTime = "%d:%02d %s".format(h12, m, if (h < 12) "AM" else "PM")
                        }, now.get(Calendar.HOUR_OF_DAY), now.get(Calendar.MINUTE), false).show()
                    }
                }
                Column(modifier = Modifier.weight(1f)) {
                    Text("Reminder date", color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(4.dp))
                    PickerField(reminderDate, "Optional", Icons.Default.CalendarMonth) {
                        val now = Calendar.getInstance()
                        DatePickerDialog(context, { _, y, m, d ->
                            reminderDate = "%02d/%02d/%d".format(m + 1, d, y)
                        }, now.get(Calendar.YEAR), now.get(Calendar.MONTH), now.get(Calendar.DAY_OF_MONTH)).show()
                    }
                }
            }

            Spacer(modifier = Modifier.height(28.dp))

            GlassButton("SAVE CHANGES", modifier = Modifier.fillMaxWidth(0.8f)) {
                val oldTitle = item.title.trim()
                val newTitle = title.trim().ifEmpty { "Item $itemId" }
                var newDesc = description.trim().ifEmpty { "No description" }

                if (oldTitle.isNotBlank() && oldTitle != newTitle && newDesc.contains(oldTitle, ignoreCase = true)) {
                    newDesc = newDesc.replace(Regex(Regex.escape(oldTitle), RegexOption.IGNORE_CASE), newTitle)
                }

                val updatedItem = item.copy(
                    title = newTitle,
                    description = newDesc,
                    reminderTime = reminderTime,
                    reminderDate = reminderDate,
                    isUrgent = reminderTime.isNotEmpty()
                )
                ItemRepository.updateItem(updatedItem)
                Toast.makeText(context, "Item updated", Toast.LENGTH_SHORT).show()
                navController.popBackStack()
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
            Icon(icon, null, tint = Color.Black, modifier = Modifier.size(16.dp))
        }
    }
}

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
        Icon(icon, description, tint = Color.White, modifier = Modifier.size(size * 0.5f))
    }
}

@Composable
private fun GlassButton(
    text: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Box(
        modifier = modifier
            .height(48.dp)
            .clip(RoundedCornerShape(24.dp))
            .background(Color.White.copy(alpha = 0.18f))
            .border(
                BorderStroke(1.dp, Color.White.copy(alpha = 0.55f)),
                RoundedCornerShape(24.dp)
            )
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Text(text, color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.Bold, letterSpacing = 0.8.sp)
    }
}
