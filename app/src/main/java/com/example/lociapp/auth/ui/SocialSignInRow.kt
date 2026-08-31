package com.example.lociapp.auth.ui

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.lociapp.R

/**
 * Row of social sign-in icon buttons (Facebook / Google / Apple).
 * UI only — each onClick is a TODO stub, wire real sign-in flows in later.
 */
@Composable
fun SocialSignInRow(
    onFacebookClick: () -> Unit,
    onGoogleClick: () -> Unit,
    onAppleClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(modifier = modifier) {
        IconButton(onClick = onFacebookClick) {
            Icon(
                painter = painterResource(id = R.drawable.ic_facebook),
                contentDescription = "Continue with Facebook",
                tint = Color.Unspecified,
                modifier = Modifier.size(40.dp)
            )
        }

        IconButton(
            onClick = onGoogleClick,
            modifier = Modifier.padding(start = 8.dp)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_google),
                contentDescription = "Continue with Google",
                tint = Color.Unspecified,
                modifier = Modifier.size(40.dp)
            )
        }

        IconButton(
            onClick = onAppleClick,
            modifier = Modifier.padding(start = 8.dp)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.apple),
                contentDescription = "Continue with Apple",
                tint = Color.Unspecified,
                modifier = Modifier.size(40.dp)
            )
        }
    }
}
