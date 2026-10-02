package com.example.lociapp.auth.ui

import android.util.Patterns
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
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * Local app: no email can be sent, so the user types their email and a
 * new password, and the password is updated in the phone's database.
 */
@Composable
fun ForgotPasswordScreen(onBackClick: () -> Unit) {
    val context = LocalContext.current
    var email by remember { mutableStateOf("") }
    var newPassword by remember { mutableStateOf("") }
    val db = remember { UserDatabase(context) }

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
            text = "Enter your email and choose a new password.",
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

        OutlinedTextField(
            value = newPassword,
            onValueChange = { newPassword = it },
            label = { Text("New password") },
            singleLine = true,
            colors = appTextFieldColors(),
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp)
        )

        TransparentPillButton(
            text = "Reset Password",
            onClick = {
                val cleanEmail = email.trim()
                when {
                    !Patterns.EMAIL_ADDRESS.matcher(cleanEmail).matches() ->
                        Toast.makeText(context, "Please enter a valid email", Toast.LENGTH_SHORT).show()
                    newPassword.length < 6 ->
                        Toast.makeText(context, "Password must be at least 6 characters", Toast.LENGTH_SHORT).show()
                    db.resetPassword(cleanEmail, newPassword) -> {
                        Toast.makeText(context, "Password updated. You can sign in now.", Toast.LENGTH_LONG).show()
                        onBackClick()
                    }
                    else ->
                        Toast.makeText(context, "No account found with this email", Toast.LENGTH_SHORT).show()
                }
            },
            modifier = Modifier.padding(top = 24.dp)
        )
    }
}
