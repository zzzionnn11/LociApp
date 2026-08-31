package com.example.lociapp.auth.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

// Faithful port of LociLogin's AppComponents.kt — same gradient, same
// pill button, same back button, just repackaged under
// com.example.lociapp.auth.ui — so the auth screens look pixel-for-pixel
// identical to the original mock. Everything auth screens style off of
// lives in this one file, so when the Figma design is ready, restyling
// is a single-file change instead of touching every screen.

/** Shared light-blue-to-white gradient, used as the background on every auth screen. */
val AppBackgroundGradient = Brush.verticalGradient(
    colorStops = arrayOf(
        0f to Color(0xFFAFD3F7),
        0.6f to Color(0xFFD9ECFB),
        1f to Color.White
    )
)

@Composable
fun TransparentPillButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onClick,
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(50),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.White.copy(alpha = 0.5f),
            contentColor = Color.Black
        ),
        border = BorderStroke(1.dp, Color.Black.copy(alpha = 0.1f)),
        elevation = ButtonDefaults.buttonElevation(defaultElevation = 0.dp)
    ) {
        Text(text)
    }
}

/** The round gray back-chevron button in the top-left of Sign up / Sign in / Forgot Password. */
@Composable
fun BackButton(onClick: () -> Unit) {
    IconButton(
        onClick = onClick,
        modifier = Modifier
            .size(40.dp)
            .background(Color.White.copy(alpha = 0.5f), CircleShape)
    ) {
        Icon(
            imageVector = Icons.Default.ArrowBack,
            contentDescription = "Back"
        )
    }
}

@Composable
fun appTextFieldColors() = OutlinedTextFieldDefaults.colors(
    focusedLabelColor = Color(0xFF1A1A1A),
    unfocusedLabelColor = Color(0xFF333333),
    focusedTextColor = Color(0xFF1A1A1A),
    unfocusedTextColor = Color(0xFF1A1A1A)
)
