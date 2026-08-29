package com.example.lociapp.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@Composable
fun TermsScreen(navController: NavController) {
    // ✅ Removed Gradient
    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp)
                .windowInsetsPadding(WindowInsets.statusBars)
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = Color.Black, // ✅ Changed to Black
                    modifier = Modifier
                        .size(28.dp)
                        .clickable { navController.popBackStack() }
                )
                Spacer(modifier = Modifier.width(16.dp))
                Text(
                    text = "Terms & Conditions",
                    color = Color.Black, // ✅ Changed to Black
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .verticalScroll(rememberScrollState()),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White.copy(alpha = 0.5f) // ✅ Made lighter
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp)
                ) {
                    Text(
                        text = "Terms & Conditions",
                        color = Color.Black, // ✅ Changed to Black
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Text(
                        text = "Last Updated: July 2023",
                        color = Color.Black.copy(alpha = 0.6f), // ✅ Changed to Dark Grey
                        fontSize = 14.sp,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )

                    TermsSection(
                        title = "1. Acceptance of Terms",
                        content = "By using the Loci app, you agree to be bound by these Terms & Conditions. If you do not agree to these terms, please do not use the app."
                    )

                    TermsSection(
                        title = "2. User Accounts",
                        content = "You are responsible for maintaining the confidentiality of your account credentials. You agree to notify us immediately of any unauthorized use of your account."
                    )

                    TermsSection(
                        title = "3. Use of the App",
                        content = "You agree to use the app only for lawful purposes and in a way that does not infringe the rights of others. You may not use the app to store or share illegal content."
                    )

                    TermsSection(
                        title = "4. Intellectual Property",
                        content = "All content, features, and functionality of the app are owned by Loci and are protected by copyright, trademark, and other intellectual property laws."
                    )

                    TermsSection(
                        title = "5. Privacy",
                        content = "Your privacy is important to us. Please review our Privacy Policy to understand how we collect, use, and protect your personal information."
                    )

                    TermsSection(
                        title = "6. Limitation of Liability",
                        content = "Loci is provided 'as is' without warranties of any kind. We are not liable for any damages arising from the use of the app."
                    )

                    TermsSection(
                        title = "7. Changes to Terms",
                        content = "We may update these terms from time to time. We will notify you of any changes by posting the new terms on this page."
                    )

                    TermsSection(
                        title = "8. Contact Us",
                        content = "If you have any questions about these Terms & Conditions, please contact us at info@hi-fi.com."
                    )
                }
            }
        }
    }
}

@Composable
fun TermsSection(
    title: String,
    content: String
) {
    Column(
        modifier = Modifier.padding(vertical = 8.dp)
    ) {
        Text(
            text = title,
            color = Color.Black, // ✅ Changed to Black
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = content,
            color = Color.Black.copy(alpha = 0.6f), // ✅ Changed to Dark Grey
            fontSize = 14.sp,
            lineHeight = 20.sp
        )
        Spacer(modifier = Modifier.height(8.dp))
        HorizontalDivider(
            color = Color.Black.copy(alpha = 0.1f) // ✅ Changed to Dark Grey
        )
    }
}