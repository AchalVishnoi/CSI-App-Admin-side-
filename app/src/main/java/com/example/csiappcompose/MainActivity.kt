package com.example.csiappcompose

import HomePage
import TaskPage
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresExtension
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument

import coil.util.DebugLogger
import com.example.csiappcompose.pages.LoginPage
import com.example.csiappcompose.pages.SplashScreen
import com.example.csiappcompose.ui.theme.CSIAppComposeTheme

import com.example.csiappcompose.viewModels.AuthViewModel
import com.example.csiappcompose.viewModels.ChatViewModel
import coil.ImageLoader
import com.example.csiappcompose.pages.Chat.AiChat
import com.example.csiappcompose.pages.Chat.ChatRoomScreen
import com.example.csiappcompose.pages.Chat.addedGroups
import com.example.csiappcompose.pages.ChatPage
import com.example.csiappcompose.pages.ProfilePage
import com.example.csiappcompose.viewModels.AiChatViewModel
import com.google.firebase.messaging.FirebaseMessaging
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getSystemService
import com.google.firebase.FirebaseApp

import android.Manifest
import android.annotation.SuppressLint
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme


class MainActivity : ComponentActivity() {

    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            if (isGranted) {
                Log.d("Permission", "POST_NOTIFICATIONS permission granted.")
            } else {
                Log.d("Permission", "POST_NOTIFICATIONS permission denied.")
            }
        }

    @RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        // Initialize Firebase
        FirebaseApp.initializeApp(this)

        // Create Notification Channel
        createNotificationChannel()

        // Request Notification Permission (Android 13+)
        requestNotificationPermission()

        // Retrieve FCM Token
        retrieveFCMToken()


        val chatViewModel: ChatViewModel = ViewModelProvider(
            this,
            ViewModelFactory(applicationContext)
        )[ChatViewModel::class.java]

        val aiChatViewModel: AiChatViewModel = ViewModelProvider(this)[AiChatViewModel::class.java]

        setContent {
            // ImageLoader Initialization (if required for Compose UI)
            val imageLoader = ImageLoader.Builder(this).build()

            CSIAppComposeTheme {
                MyApp(chatViewModel)
            }
        }
    }



    @SuppressLint("NewApi")
    @Composable
    fun MyApp(chatViewModel: ChatViewModel) {
        MaterialTheme(
            colorScheme = lightColorScheme(),

            content = {
                Main(chatViewModel = chatViewModel)                // Replace with your composable screen
            }
        )
    }




    // Function to retrieve FCM token
    private fun retrieveFCMToken() {
        FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w("FCM", "Fetching FCM registration token failed", task.exception)
                return@addOnCompleteListener
            }
            // Get and log the FCM token
            val token = task.result
            Log.d("FCM", "FCM Token: $token")
        }
    }

    // Function to request notification permissions for Android 13+
    private fun requestNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
            ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
        }
    }

    // Function to create notification channel (for Android O+)
    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                "default_channel_id",
                getString(R.string.default_channel_name),
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = getString(R.string.default_channel_description)
            }
            val manager = getSystemService(NotificationManager::class.java)
            manager?.createNotificationChannel(channel)
        }
    }
}

// ViewModel Factory for providing application context
class ViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ChatViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ChatViewModel(context.applicationContext) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
