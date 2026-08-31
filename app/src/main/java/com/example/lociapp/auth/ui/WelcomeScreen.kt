package com.example.lociapp.auth.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.lociapp.R



/**
 * First screen shown when the app opens — faithful port of LociLogin's
 * WelcomeScreen (same background gradient, same button style, same
 * tagline).
 *
 * UI ONLY: both buttons just navigate, no auth/session logic here.
 */
@Composable
fun WelcomeScreen(
    onSignUpClick: () -> Unit,
    onSignInClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(AppBackgroundGradient)
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // TODO: replace with Image(painter = painterResource(R.drawable.logo), ...)
        // once the real logo asset is provided.
        Image(
            painter = painterResource(id = R.drawable.logo),
            contentDescription = "Loci logo",
            modifier = Modifier.fillMaxWidth(0.6f)
        )

        Text(
            text = "Snap it. Stow it. Never lose it.",
            fontSize = 14.sp,
            modifier = Modifier.padding(top = 8.dp, bottom = 64.dp)
        )

        TransparentPillButton(
            text = "Sign up",
            onClick = onSignUpClick,
            modifier = Modifier.padding(bottom = 12.dp)
        )

        TransparentPillButton(
            text = "Sign in",
            onClick = onSignInClick
        )
    }
}
