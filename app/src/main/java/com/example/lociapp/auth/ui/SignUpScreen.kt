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
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * Sign-up screen. Saves the new account in the local database on the phone.
 */
@Composable
fun SignUpScreen(
    onSignUpClick: () -> Unit,
    onGoToLogin: () -> Unit,
    onBackClick: () -> Unit
) {
    val context = LocalContext.current
    var email by remember { mutableStateOf("") }
    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val db = remember { UserDatabase(context) }

    fun toast(message: String) = Toast.makeText(context, message, Toast.LENGTH_SHORT).show()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(AppBackgroundGradient)
            .verticalScroll(rememberScrollState())
            .padding(24.dp)
    ) {
        BackButton(onBackClick)

        Text(
            text = "Sign up",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(top = 16.dp, bottom = 24.dp)
        )

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            singleLine = true,
            colors = appTextFieldColors(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = firstName,
            onValueChange = { firstName = it },
            label = { Text("First name") },
            singleLine = true,
            colors = appTextFieldColors(),
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp)
        )

        OutlinedTextField(
            value = lastName,
            onValueChange = { lastName = it },
            label = { Text("Last name") },
            singleLine = true,
            colors = appTextFieldColors(),
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp)
        )

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            singleLine = true,
            colors = appTextFieldColors(),
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp)
        )

        Text(
            text = "Use at least 6 characters, with a mix of letters and symbols",
            fontSize = 12.sp,
            modifier = Modifier.padding(top = 4.dp)
        )


        TransparentPillButton(
            text = "Sign up",
            onClick = {
                val cleanEmail = email.trim()
                val first = firstName.trim()
                val last = lastName.trim()
                when {
                    first.isEmpty() || last.isEmpty() -> toast("Please enter your first and last name")
                    !Patterns.EMAIL_ADDRESS.matcher(cleanEmail).matches() -> toast("Please enter a valid email")
                    password.length < 6 -> toast("Password must be at least 6 characters")
                    !db.addUser(first, last, cleanEmail, password) ->
                        toast("This email is already registered. Try signing in.")
                    else -> {
                        // Account saved -> log the new user in
                        val user = db.login(cleanEmail, password)
                        if (user != null) Session.login(user, true)
                        onSignUpClick()
                    }
                }
            },
            modifier = Modifier.padding(top = 24.dp)
        )

        TextButton(
            onClick = onGoToLogin,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text("Already have an account? Sign in here")
        }
    }
}
