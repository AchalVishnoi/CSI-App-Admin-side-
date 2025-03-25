package com.example.csiappcompose

import HomePage
import TaskPage
import android.os.Build
import androidx.annotation.RequiresExtension
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import coil.compose.AsyncImage
import com.example.csiappcompose.pages.Chat.ChatRoomScreen

import com.example.csiappcompose.pages.ChatPage
import com.example.csiappcompose.pages.LoginPage
import com.example.csiappcompose.pages.ProfilePage
import com.example.csiappcompose.pages.SplashScreen
import com.example.csiappcompose.viewModels.AuthViewModel
import com.example.csiappcompose.viewModels.ChatViewModel

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable

import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.rememberAnimatedNavController

import androidx.compose.animation.*
import androidx.navigation.NavBackStackEntry


@OptIn(ExperimentalAnimationApi::class)

@RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
@Composable

fun Main(modifier: Modifier = Modifier,   chatViewModel: ChatViewModel) {


    val navController = rememberAnimatedNavController()

    val authViewModel: AuthViewModel = viewModel()
    var selected = remember { mutableStateOf<String?>(null) }

    // Get the current route
    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route

    // Define screens where bottom nav should be hidden
    val hideBottomNavRoutes = listOf("splash", "login", "chat/{roomId}/{token}/{roomName}/{profilePic}/{unreadCnt}/")



    val navItemListitems = listOf(
        NavItem("Home", R.drawable.home_icon),
        NavItem("Task", R.drawable.task_icon),
        NavItem("Profile", R.drawable.profile_icon),
        NavItem("Chat", R.drawable.chat_icon),
    )

    var selectedIndex by rememberSaveable { mutableStateOf(0) }

    LaunchedEffect(navController) {
        navController.currentBackStackEntryFlow.collect { backStackEntry ->
            selectedIndex = when (backStackEntry.destination.route) {
                "home"->0
                "home_page" -> 0
                "task" -> 1
                "profile" -> 2
                "chat_room" -> 3
                else -> selectedIndex
            }
        }
    }




    //to drag selected img




    Scaffold(
        modifier = Modifier.fillMaxSize().padding(top = 0.dp),
        topBar = {
            if (currentRoute !in hideBottomNavRoutes) {
                TopBar()
            }
        },
        bottomBar = {
            if (currentRoute !in hideBottomNavRoutes) {

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 10.dp, end = 10.dp, bottom = 20.dp)
                    .shadow(
                        elevation = 24.dp, // Increased shadow elevation
                        shape = RoundedCornerShape(14.dp),
                        ambientColor = Color.Black.copy(alpha = 0.3f), // Darker shadow color (ambient)
                        spotColor = Color.Black.copy(alpha = 0.5f) // Darker shadow for a more elevated effect (spot)
                    )
                    .background(
                        Color(0xFFF7F7F7),
                        shape = RoundedCornerShape(14.dp)
                    ) // Apply white background with rounded corners
            )
            {
                NavigationBar(
                    containerColor = Color.White,
                    modifier = Modifier.fillMaxWidth().padding(top = 1.dp, bottom = 0.dp)
                        .height(80.dp)

                ) {
                    navItemListitems.forEachIndexed { index, navItem ->
                        NavigationBarItem(
                            modifier = Modifier.padding(bottom = 1.dp, top = 2.dp),
                            selected = selectedIndex == index,
                            onClick = {
                                selectedIndex = index
                                val route = when (index) {
                                    0 -> "home_page"
                                    1 -> "task"
                                    2 -> "profile"
                                    3 -> "chat_room"
                                    else -> "home_page"
                                }
                                navController.navigate(route) {
                                    popUpTo("home_screen") { inclusive = false }
                                    launchSingleTop = true
                                }

                            },
                            icon = {
                                Box(
                                    modifier = Modifier
                                        .size(60.dp)
                                        .clip(RoundedCornerShape(12.dp)) // Rounded corners for the background
                                        .background(
                                            color = if (selectedIndex == index) Color.Blue else Color.Transparent // Background color when selected
                                        )
                                        .padding(8.dp) // Padding inside the box

                                ) {
                                    Column(
                                        horizontalAlignment = Alignment.CenterHorizontally,
                                        verticalArrangement = Arrangement.Center,
                                        modifier = Modifier.fillMaxSize() // Make the column take up the full size of the Box
                                    ) {
                                        // Icon
                                        Icon(
                                            painter = painterResource(id = navItem.icon),
                                            contentDescription = null,
                                            modifier = Modifier.size(22.dp), // Icon size
                                            tint = if (selectedIndex == index) Color.White else Color.Black // Icon color
                                        )

                                        // Label
                                        Text(
                                            text = navItem.label,
                                            color = if (selectedIndex == index) Color.White else Color.Black,
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.Medium,
                                            modifier = Modifier.padding(top = 4.dp)
                                        )
                                    }
                                }
                            },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = Color.White,
                                unselectedIconColor = Color.Black,
                                unselectedTextColor = Color.Black,
                                indicatorColor = Color.Transparent
                            )
                        )
                    }
                }
            }
        }
        }
    ) { innerPadding ->
        ContentScreen(modifier = Modifier.fillMaxSize(),selectedIndex=selectedIndex,chatViewModel=chatViewModel, navController = navController,selected)
        var previousIndex by rememberSaveable { mutableStateOf(0) }

        AnimatedNavHost(
            navController = navController,
            startDestination = "splash",
            enterTransition = {
                when {
                    // Zoom In effect when navigating away from Splash
                    initialState.destination.route =="splash" -> fadeIn(animationSpec = tween(500))


                    selectedIndex > previousIndex -> slideInHorizontally(
                        initialOffsetX = { it },
                        animationSpec = tween(300)
                    )


                    selectedIndex < previousIndex -> slideInHorizontally(
                        initialOffsetX = { -it },
                        animationSpec = tween(300)
                    )

                    else -> fadeIn(animationSpec = tween(300)) // Default fallback
                }
            },
            exitTransition = {
                when {
                    // Zoom Out effect when leaving Splash
                    targetState.destination.route == "splash" -> fadeOut(animationSpec = tween(500))

                    // Slide Out to Left when moving to a higher index
                    selectedIndex > previousIndex -> slideOutHorizontally(
                        targetOffsetX = { -it },
                        animationSpec = tween(300)
                    )

                    // Slide Out to Right when moving to a lower index
                    selectedIndex < previousIndex -> slideOutHorizontally(
                        targetOffsetX = { it },
                        animationSpec = tween(300)
                    )

                    else -> fadeOut(animationSpec = tween(300)) // Default fallback
                }
            },
            popEnterTransition = {
                slideInHorizontally(initialOffsetX = { -it }, animationSpec = tween(300))
            },
            popExitTransition = {
                slideOutHorizontally(targetOffsetX = { it }, animationSpec = tween(300))
            }
        ) {
            composable("splash") { SplashScreen(navController, authViewModel) }
            composable("login") { LoginPage(navController, authViewModel) }
            composable("home") { HomePage() }
            composable("home_page") { HomePage() }
            composable("task") { TaskPage() }
            composable("profile") { ProfilePage() }
            composable("chat_room") { ChatPage(chatViewModel = chatViewModel, navController = navController, selected = selected) }

            composable(
                route = "chat/{roomId}/{token}/{roomName}/{profilePic}/{unreadCnt}/",
                arguments = listOf(
                    navArgument("roomId") { type = NavType.IntType },
                    navArgument("token") { type = NavType.StringType },
                    navArgument("roomName") { type = NavType.StringType },
                    navArgument("profilePic") { type = NavType.StringType },
                    navArgument("unreadCnt") { type = NavType.IntType }
                )
            ) { backStackEntry ->
                val roomId = backStackEntry.arguments?.getInt("roomId") ?: 0
                val unreadCnt = backStackEntry.arguments?.getInt("unreadCnt") ?:0
                val token = backStackEntry.arguments?.getString("token") ?: ""
                val roomName = backStackEntry.arguments?.getString("roomName") ?: ""
                val profilePic = backStackEntry.arguments?.getString("profilePic") ?: ""

                ChatRoomScreen(
                    roomId = roomId,
                    token = token,
                    RoomName = roomName,
                    profilePic = profilePic,
                    navController = navController,
                    unreadCnt=unreadCnt
                )
            }
        }


        LaunchedEffect(selectedIndex) {
            previousIndex = selectedIndex
        }

    }


//to show profile
    selected?.value?.let{ image->
        displayInFullScreen(image = image) {
            selected.value = null
        }

    }
}

@Composable
fun TopBar() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFF0A3D91), shape = RoundedCornerShape(bottomStart = 12.dp, bottomEnd = 12.dp)) // Blue background with rounded bottom corners
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Left Icon
        Icon(
            painter = painterResource(id = R.drawable.csi_logo), // Replace with actual icon resource
            contentDescription = "Left Icon",
            tint = Color.White
        )

        // Right Profile Icon
        Icon(
            painter = painterResource(id = R.drawable.profile_icon), // Replace with actual icon resource
            contentDescription = "Profile Icon",
            tint = Color.White
        )
    }
}

@Composable
fun ContentScreen(modifier: Modifier,selectedIndex: Int,chatViewModel: ChatViewModel,navController: NavHostController,selected: MutableState<String?>) {
    when(selectedIndex){
        0 -> HomePage(modifier)
        1 -> TaskPage(modifier)
        2 -> ProfilePage( )
        3 -> ChatPage(modifier, chatViewModel = chatViewModel, navController = navController,selected )
    }
}







@Composable
fun displayInFullScreen(image:String, dismiss:()-> Unit){
    var offsety by remember{mutableStateOf(0f)}

    Box(modifier = Modifier.fillMaxSize()
        .background(color = Color.Black).
        pointerInput(Unit) {
            detectVerticalDragGestures(
                onDragEnd = {if(offsety<-250) dismiss()
                else offsety=0f},
                onVerticalDrag = {_,dragAmount->offsety+=dragAmount}
            )
        },
        contentAlignment = Alignment.Center
    )
    {

        AsyncImage(
            model = image,
            contentDescription = "full size image",
            modifier = Modifier.fillMaxSize().graphicsLayer { translationY=offsety }

            ,contentScale = ContentScale.Fit
        )





    }

}