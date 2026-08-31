package com.example.lociapp.auth.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Checkbox
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * Login screen — faithful port of LociLogin's SignInScreen, same layout,
 * copy, and components (AppBackgroundGradient / TransparentPillButton /
 * BackButton from AppComponents.kt).
 *
 * UI ONLY: the Sign in button, social buttons, and Forgot Password link
 * do not validate or authenticate anything.
 */
@Composable
fun LoginScreen(
    onLoginClick: () -> Unit,
    onGoToSignUp: () -> Unit,
    onBackClick: () -> Unit,
    onForgotPasswordClick: () -> Unit
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var rememberMe by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(AppBackgroundGradient)
            .verticalScroll(rememberScrollState())
            .padding(24.dp)
    ) {
        BackButton(onBackClick)

        Text(
            text = "Sign in",
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
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            colors = appTextFieldColors(),
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp)
        )

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 4.dp)
        ) {
            Checkbox(checked = rememberMe, onCheckedChange = { rememberMe = it })
            Text("Remember me")

            Text(
                text = "Forgot Password",
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 16.dp)
                    .clickable { onForgotPasswordClick() },
                textAlign = TextAlign.End
            )
        }

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
                onFacebookClick = { /* TODO: Implement Facebook sign-in */ },
                onGoogleClick = { /* TODO: Implement Google sign-in */ },
                onAppleClick = { /* TODO: Implement Apple sign-in */ }
            )
        }

        TransparentPillButton(
            text = "Sign in",
            onClick = {
                // TODO: Implement login logic (validation + authentication)
                onLoginClick()
            },
            modifier = Modifier.padding(top = 24.dp)
        )

        TextButton(
            onClick = onGoToSignUp,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text("Are you new? Sign up here")
        }
    }
}
