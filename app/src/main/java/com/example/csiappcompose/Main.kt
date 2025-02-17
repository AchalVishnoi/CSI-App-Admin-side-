package com.example.csiappcompose

import HomePage
import TaskPage
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.csiappcompose.pages.ChatPage
import com.example.csiappcompose.pages.ProfilePage

@Composable
fun Main(modifier: Modifier = Modifier) {

    val navItemListitems = listOf(
        NavItem("Home", R.drawable.home_icon),
        NavItem("Task", R.drawable.task_icon),
        NavItem("Profile", R.drawable.profile_icon),
        NavItem("Chat", R.drawable.chat_icon),
    )
    var selectedIndex by remember {
        mutableStateOf(0)
    }
    Scaffold(
        modifier = Modifier.fillMaxSize().padding(top = 0.dp),
        topBar = {
            TopBar()
        },
        bottomBar = {
            NavigationBar(
                containerColor = Color(0xFFFAFBFC) // Background color of Bottom Navigation
            ){
                navItemListitems.forEachIndexed { index, navItem ->
                    NavigationBarItem(
                        selected = selectedIndex == index,
                        onClick = { selectedIndex = index },
                        icon = {
                            Icon(
                                painter = painterResource(id = navItem.icon),
                                contentDescription = null,
                                tint = if (selectedIndex == index) Color.White else Color.Black // Change icon color
                            )
                        },
                        label = {
                            Text(
                                text = navItem.label,
                                color = Color.Black // Change text color
                            )
                        },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = Color.White, // Color when selected
                            unselectedIconColor = Color.Black, // Color when not selected
                            unselectedTextColor = Color.Black // Text color when not selected
                        )
                    )
                }
            }
        }
    ) { innerPadding ->
        ContentScreen(
            modifier = Modifier.padding(innerPadding),
            selectedIndex
        )
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
fun ContentScreen(modifier: Modifier,selectedIndex: Int) {
    when(selectedIndex){
        0 -> HomePage(modifier)
        1 -> TaskPage(modifier)
        2 -> ProfilePage(modifier)
        3 -> ChatPage(modifier)
    }
}