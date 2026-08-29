package com.example.lociapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.lociapp.screens.*
import com.example.lociapp.ui.theme.LociAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LociAppTheme {
                // ✅ Background Image added here - covers entire app
                Box(
                    modifier = Modifier.fillMaxSize()
                ) {
                    // Background Image
                    Image(
                        painter = painterResource(id = R.drawable.mainhomescreenbg),
                        contentDescription = "App Background",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )

                    // App Content (semi-transparent overlay to make content readable)
                    Surface(
                        modifier = Modifier.fillMaxSize(),
                        color = MaterialTheme.colorScheme.background.copy(alpha = 0.85f) // 85% opacity
                    ) {
                        AppNavigation()
                    }
                }
            }
        }
    }
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "home"
    ) {
        composable("home") {
            HomeScreen(navController = navController)
        }
        composable("list") {
            ListScreen(navController = navController)
        }
        composable("settings") {
            SettingsScreen(navController = navController)
        }
        composable("reminders") {
            RemindersScreen(navController = navController)
        }
        composable("add_reminder") {
            AddReminderScreen(navController = navController)
        }
        composable("profile") {
            ProfileScreen(navController = navController)
        }
        composable("about") {
            AboutScreen(navController = navController)
        }
        composable("faq") {
            FAQScreen(navController = navController)
        }
        composable("privacy") {
            PrivacyPolicyScreen(navController = navController)
        }
        composable("terms") {
            TermsScreen(navController = navController)
        }
        composable(
            "item_detail/{itemId}",
            arguments = listOf(navArgument("itemId") { type = NavType.IntType })
        ) { backStackEntry ->
            val itemId = backStackEntry.arguments?.getInt("itemId") ?: 1
            ItemDetailScreen(
                navController = navController,
                itemId = itemId
            )
        }
        composable("full_image/{itemId}") { backStackEntry ->
            val itemId = backStackEntry.arguments?.getInt("itemId") ?: 1
            FullImageScreen(
                navController = navController,
                itemId = itemId
            )
        }
    }
}