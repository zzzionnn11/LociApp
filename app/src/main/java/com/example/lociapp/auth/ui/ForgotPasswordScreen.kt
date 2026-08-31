package com.example.lociapp.auth.ui

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * Forgot-password screen — faithful port of LociLogin's
 * ForgotPasswordScreen, reached from Login's "Forgot Password" link.
 *
 * UI ONLY: "Send Reset Link" shows a placeholder Toast and navigates
 * back — no email is actually sent.
 */
@Composable
fun ForgotPasswordScreen(onBackClick: () -> Unit) {
    val context = LocalContext.current
    var email by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(AppBackgroundGradient)
            .verticalScroll(rememberScrollState())
            .padding(24.dp)
    ) {
        BackButton(onBackClick)

        Text(
            text = "Forgot Password",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(top = 16.dp, bottom = 8.dp)
        )

        Text(
            text = "Enter your email and we'll send you a link to reset your password.",
            fontSize = 14.sp,
            modifier = Modifier.padding(bottom = 24.dp)
        )

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            colors = appTextFieldColors(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            modifier = Modifier.fillMaxWidth()
        )

        TransparentPillButton(
            text = "Send Reset Link",
            onClick = {
                // TODO: Implement real password-reset request.
                if (email.isBlank()) {
                    Toast.makeText(context, "Please enter your email", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(context, "Reset link sent!", Toast.LENGTH_SHORT).show()
                    onBackClick()
                }
            },
            modifier = Modifier.padding(top = 24.dp)
        )
    }
}
