package com.example.lociapp.auth.ui

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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * Sign-up screen — faithful port of LociLogin's SignUpScreen, same
 * layout, copy, and components (AppBackgroundGradient /
 * TransparentPillButton / BackButton from AppComponents.kt).
 *
 * UI ONLY: the Sign up button and social buttons do not validate or
 * create anything.
 */
@Composable
fun SignUpScreen(
    onSignUpClick: () -> Unit,
    onGoToLogin: () -> Unit,
    onBackClick: () -> Unit
) {
    var email by remember { mutableStateOf("") }
    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

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
            colors = appTextFieldColors(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = firstName,
            onValueChange = { firstName = it },
            label = { Text("First name") },
            colors = appTextFieldColors(),
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp)
        )

        OutlinedTextField(
            value = lastName,
            onValueChange = { lastName = it },
            label = { Text("Last name") },
            colors = appTextFieldColors(),
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp)
        )

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            colors = appTextFieldColors(),
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp)
        )

        Text(
            text = "Make sure to use a combination of letters and symbols",
            fontSize = 12.sp,
            modifier = Modifier.padding(top = 4.dp)
        )

        Text(
            text = "or",
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(top = 20.dp)
        )

        Column(
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(top = 8.dp)
        ) {
            SocialSignInRow(
                onFacebookClick = { /* TODO: Implement Facebook sign-up */ },
                onGoogleClick = { /* TODO: Implement Google sign-up */ },
                onAppleClick = { /* TODO: Implement Apple sign-up */ }
            )
        }

        TransparentPillButton(
            text = "Sign up",
            onClick = {
                // TODO: Implement sign-up logic (validation + account creation)
                onSignUpClick()
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
