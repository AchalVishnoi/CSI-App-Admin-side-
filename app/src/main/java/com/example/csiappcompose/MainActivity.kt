package com.example.csiappcompose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.csiappcompose.pages.LoginPage
import com.example.csiappcompose.pages.SplashScreen
import com.example.csiappcompose.ui.theme.CSIAppComposeTheme


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            CSIAppComposeTheme {
                MyApp() // Call MyApp instead of duplicating setup
            }
        }
    }
}

// Navigation Graph
@Composable

fun MyApp() {
    val navController = rememberNavController()
    val authViewModel: AuthViewModel = viewModel()

    NavHost(navController = navController, startDestination = "splash") {
        composable("splash") { SplashScreen(navController, authViewModel) }
        composable("login") { LoginPage(navController, authViewModel) }
        composable("home") { Main(navController=navController, authViewModel = authViewModel) }
        // Ensure this exists
    }
}
