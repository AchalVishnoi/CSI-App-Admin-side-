package com.example.csiappcompose

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
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
import com.example.csiappcompose.viewModels.AiChatViewModel


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)








        val chatViewModel: ChatViewModel = ViewModelProvider(
            this,
            ViewModelFactory(applicationContext)
        )[ChatViewModel::class.java]


        val aiChatViewModel: AiChatViewModel = ViewModelProvider(this)[AiChatViewModel::class.java]

        setContent {

            val imageLoader = ImageLoader.Builder(this)
                .build()

            CSIAppComposeTheme {
            MyApp(chatViewModel)

            }
        }
    }
}

// ViewModel Factory to provide Context
class ViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ChatViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ChatViewModel(context.applicationContext) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

// Navigation Graph
@Composable

fun MyApp(chatViewModel: ChatViewModel) {
    val navController = rememberNavController()
    val authViewModel: AuthViewModel = viewModel()

    NavHost(navController = navController, startDestination = "splash") {
        composable("splash") { SplashScreen(navController, authViewModel) }
        composable("login") { LoginPage(navController, authViewModel) }
        composable("home") { Main(navController=navController, authViewModel = authViewModel, chatViewModel=chatViewModel) }

        composable(
            route = "chat/{roomId}/{token}/{roomName}/{profilePic}",
            arguments = listOf(
                navArgument("roomId") { type = NavType.IntType },
                navArgument("token") { type = NavType.StringType },
                navArgument("roomName") { type = NavType.StringType },// Add this argument
                navArgument("profilePic") { type = NavType.StringType } // Add this argument
            )
        ) { backStackEntry ->
            val roomId = backStackEntry.arguments?.getInt("roomId") ?: 0
            val token = backStackEntry.arguments?.getString("token") ?: ""
            val roomName = backStackEntry.arguments?.getString("roomName") ?: ""
            val profilePic = backStackEntry.arguments?.getString("profilePic") ?: ""

            ChatRoomScreen(roomId = roomId, token = token, RoomName = roomName,profilePic=profilePic)
        }

    }
}
