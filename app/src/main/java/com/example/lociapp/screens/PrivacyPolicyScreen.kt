package com.example.lociapp.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.lociapp.auth.ui.AppBackgroundGradient
import com.example.lociapp.components.ScreenHeader

@Composable
fun PrivacyPolicyScreen(navController: NavController) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(AppBackgroundGradient)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp)
                .windowInsetsPadding(WindowInsets.statusBars)
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            ScreenHeader(onBack = { navController.popBackStack() }, title = "Privacy Policy")

            Spacer(modifier = Modifier.height(24.dp))

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .verticalScroll(rememberScrollState()),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White.copy(alpha = 0.5f)
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp)
                ) {
                    Text(
                        text = "Privacy Policy",
                        color = Color.Black,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Text(
                        text = "July 2023",
                        color = Color.Black.copy(alpha = 0.6f),
                        fontSize = 14.sp,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )

                    Text(
                        text = "1. Information We Collect",
                        color = Color.Black,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    PolicyItem(
                        question = "Why do you use this app?",
                        answer = "To help us understand how we can improve our services."
                    )
                    PolicyItem(
                        question = "How do we collect it?",
                        answer = "Through your usage of the app."
                    )
                    PolicyItem(
                        question = "What information do we collect?",
                        answer = "Your name, email address, and phone number."
                    )
                    PolicyItem(
                        question = "For what purpose?",
                        answer = "To provide you with a better experience."
                    )
                    PolicyItem(
                        question = "How is it used?",
                        answer = "To improve our services."
                    )
                    PolicyItem(
                        question = "Who has access to it?",
                        answer = "Only employees who need it for their job responsibilities."
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "2. How We Use Your Information",
                        color = Color.Black,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "We use the collected data strictly to fulfill our obligations under the terms of our contract.",
                        color = Color.Black.copy(alpha = 0.6f),
                        fontSize = 14.sp,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )

                    PolicyItem(
                        question = "To process your requests",
                        answer = "To ensure that your request is processed correctly."
                    )
                    PolicyItem(
                        question = "To comply with legal requirements",
                        answer = "To comply with laws and regulations."
                    )
                    PolicyItem(
                        question = "To improve our services",
                        answer = "To enhance your experience."
                    )
                    PolicyItem(
                        question = "To prevent fraud",
                        answer = "To detect and prevent fraudulent activities."
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "3. How Your Data Is Shared",
                        color = Color.Black,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "We share your data with third parties only when necessary to fulfill our contractual obligations or as required by law.",
                        color = Color.Black.copy(alpha = 0.6f),
                        fontSize = 14.sp,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )

                    PolicyItem(
                        question = "Service providers",
                        answer = "To assist us in providing our services."
                    )
                    PolicyItem(
                        question = "Third parties",
                        answer = "To perform services on our behalf."
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "4. Your Control and Permissions",
                        color = Color.Black,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "You have the right to:",
                        color = Color.Black.copy(alpha = 0.6f),
                        fontSize = 14.sp,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )

                    PolicyItem(
                        question = "Access",
                        answer = "To access your personal data held by us."
                    )
                    PolicyItem(
                        question = "Rectification",
                        answer = "To request correction of inaccurate data."
                    )
                    PolicyItem(
                        question = "Deletion",
                        answer = "To request deletion of your data."
                    )
                    PolicyItem(
                        question = "Restriction",
                        answer = "To restrict processing of your data."
                    )
                    PolicyItem(
                        question = "Data portability",
                        answer = "To receive your data in a structured, commonly used format."
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "5. Security",
                        color = Color.Black,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "We take reasonable measures to protect your data from unauthorized access, loss, or damage.",
                        color = Color.Black.copy(alpha = 0.6f),
                        fontSize = 14.sp
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "6. Contact Us",
                        color = Color.Black,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "If you have any questions, concerns, or feedback regarding this Privacy Policy or your data, please contact us at:",
                        color = Color.Black.copy(alpha = 0.6f),
                        fontSize = 14.sp
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "📧 Email: info@hi-fi.com",
                        color = Color.Black.copy(alpha = 0.6f),
                        fontSize = 14.sp
                    )
                    Text(
                        text = "🌐 Website: www.hi-fi.com",
                        color = Color.Black.copy(alpha = 0.6f),
                        fontSize = 14.sp
                    )
                }
            }
        }
    }
}

@Composable
fun PolicyItem(
    question: String,
    answer: String
) {
    Column(
        modifier = Modifier.padding(vertical = 2.dp)
    ) {
        Text(
            text = "• $question:",
            color = Color.Black,
            fontSize = 13.sp,
            fontWeight = FontWeight.Medium
        )
        Text(
            text = "  $answer",
            color = Color.Black.copy(alpha = 0.6f),
            fontSize = 13.sp,
            modifier = Modifier.padding(start = 8.dp)
        )
    }
}