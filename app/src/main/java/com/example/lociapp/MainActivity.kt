package com.example.lociapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.lociapp.auth.ui.ForgotPasswordScreen
import com.example.lociapp.auth.ui.LoginScreen
import com.example.lociapp.auth.ui.SignUpScreen
import com.example.lociapp.auth.ui.StartUpScreen
import com.example.lociapp.auth.ui.WelcomeScreen
import com.example.lociapp.screens.*
import com.example.lociapp.ui.theme.LociAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LociAppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Color.White
                ) {
                    AppNavigation()
                }
            }
        }
    }
}

// Route names for the auth flow, kept in one place so they're never
// typed as a raw string more than once.
private object AuthRoutes {
    const val WELCOME = "welcome"
    const val LOGIN = "login"
    const val SIGN_UP = "signup"

    const val START_UP = "startup"
    const val FORGOT_PASSWORD = "forgot_password"
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        // Welcome is the first screen shown when the app opens.
        startDestination = AuthRoutes.WELCOME
    ) {
        // --- Auth screens (UI only, see com.example.lociapp.auth.ui) ---
        composable(AuthRoutes.WELCOME) {
            WelcomeScreen(
                onSignUpClick = { navController.navigate(AuthRoutes.SIGN_UP) },
                onSignInClick = { navController.navigate(AuthRoutes.LOGIN) }
            )
        }

        composable(AuthRoutes.LOGIN) {
            LoginScreen(
                onLoginClick = {
                    // TODO: replace with real post-login navigation once auth exists.
                    navController.navigate(AuthRoutes.START_UP) {
                        popUpTo(AuthRoutes.WELCOME) { inclusive = true }
                    }
                },
                onGoToSignUp = {
                    navController.navigate(AuthRoutes.SIGN_UP) {
                        popUpTo(AuthRoutes.WELCOME)
                    }
                },
                onBackClick = { navController.popBackStack() },
                onForgotPasswordClick = { navController.navigate(AuthRoutes.FORGOT_PASSWORD) }
            )
        }

        composable(AuthRoutes.SIGN_UP) {
            SignUpScreen(
                onSignUpClick = {
                    // TODO: replace with real post-sign-up navigation once auth exists.
                    navController.navigate(AuthRoutes.START_UP) {
                        popUpTo(AuthRoutes.WELCOME) { inclusive = true }
                    }
                },
                onGoToLogin = {
                    navController.navigate(AuthRoutes.LOGIN) {
                        popUpTo(AuthRoutes.WELCOME)
                    }
                },
                onBackClick = { navController.popBackStack() }
            )
        }

        composable(AuthRoutes.FORGOT_PASSWORD) {
            ForgotPasswordScreen(
                onBackClick = { navController.popBackStack() }
            )
        }

        composable(AuthRoutes.START_UP) {
            StartUpScreen(navController = navController)
        }

        // --- Existing LociApp screens (unchanged) ---
        composable("home") { HomeScreen(navController = navController) }
        composable("list") { ListScreen(navController = navController) }
        composable("settings") { SettingsScreen(navController = navController) }
        composable("reminders") { RemindersScreen(navController = navController) }
        composable("add_reminder") { AddReminderScreen(navController = navController) }
        composable("profile") { ProfileScreen(navController = navController) }
        composable("about") { AboutScreen(navController = navController) }
        composable("faq") { FAQScreen(navController = navController) }
        composable("privacy") { PrivacyPolicyScreen(navController = navController) }
        composable("terms") { TermsScreen(navController = navController) }

        composable(
            "item_detail/{itemId}",
            arguments = listOf(navArgument("itemId") { type = NavType.IntType })
        ) { backStackEntry ->
            val itemId = backStackEntry.arguments?.getInt("itemId") ?: 1
            ItemDetailScreen(navController = navController, itemId = itemId)
        }

        composable(
            "full_image/{itemId}",
            arguments = listOf(navArgument("itemId") { type = NavType.IntType })
        ) { backStackEntry ->
            val itemId = backStackEntry.arguments?.getInt("itemId") ?: 1
            FullImageScreen(navController = navController, itemId = itemId)
        }
    }
}
