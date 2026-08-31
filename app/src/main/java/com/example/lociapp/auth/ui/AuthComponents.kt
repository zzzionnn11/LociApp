package com.example.lociapp.auth.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

// LociApp's existing brand colors (see ui/theme/Theme.kt LightColorScheme:
// primary = 0xFF667eea, secondary = 0xFF764ba2). Auth screens reuse the
// same gradient rather than the auth mock's original light-blue palette,
// so the login/sign-up flow matches the rest of the app.
private val AuthButtonGradient = Brush.horizontalGradient(
    colors = listOf(Color(0xFF667eea), Color(0xFF764ba2))
)

/**
 * Primary pill-shaped call-to-action button for the auth flow (Login /
 * Create Account), styled with LociApp's brand gradient.
 */
@Composable
fun AuthPillButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .background(AuthButtonGradient, RoundedCornerShape(50)),
        shape = RoundedCornerShape(50),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Transparent,
            contentColor = Color.White
        ),
        elevation = ButtonDefaults.buttonElevation(defaultElevation = 0.dp)
    ) {
        Text(text)
    }
}

/** Text field color scheme for auth screens, tinted with LociApp's primary color. */
@Composable
fun authTextFieldColors() = OutlinedTextFieldDefaults.colors(
    focusedBorderColor = Color(0xFF667eea),
    focusedLabelColor = Color(0xFF667eea),
    unfocusedLabelColor = Color(0xFF333333),
    focusedTextColor = Color(0xFF1A1A1A),
    unfocusedTextColor = Color(0xFF1A1A1A)
)
