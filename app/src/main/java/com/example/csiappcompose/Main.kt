package com.example.csiappcompose

import HomePage
import TaskPage
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.csiappcompose.pages.ChatPage
import com.example.csiappcompose.pages.ProfilePage

@Composable

@Preview
fun Main(modifier: Modifier = Modifier) {

    val navItemListitems = listOf(
        NavItem("Home", R.drawable.home_icon),
        NavItem("Task", R.drawable.task_icon),
        NavItem("Profile", R.drawable.profile_icon),
        NavItem("Chat", R.drawable.chat_icon),
    )
    var selectedIndex by remember { mutableStateOf(0) }

    Scaffold(
        modifier = Modifier.fillMaxSize().padding(top = 0.dp),
        topBar = {
            TopBar()
        },
        bottomBar = {

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
                    .background(Color(0xF7F7F7), shape = RoundedCornerShape(14.dp)) // Apply white background with rounded corners
            )
            {
                NavigationBar(
                    containerColor = Color.White,
                    modifier = Modifier.fillMaxWidth().padding(top = 1.dp, bottom = 0.dp).height(80.dp)

                ) {
                    navItemListitems.forEachIndexed { index, navItem ->
                        NavigationBarItem(
                            modifier = Modifier.padding(bottom = 1.dp, top = 2.dp),
                            selected = selectedIndex == index,
                            onClick = { selectedIndex = index },
                            icon = {
                                Box(
                                    modifier = Modifier
                                        .size(60.dp)
                                        .clip(RoundedCornerShape(12.dp)) // Rounded corners for the background
                                        .background(
                                            color = if (selectedIndex == index) Color.Blue else Color.Transparent // Background color when selected
                                        )
                                        .padding(top = 8.dp) // Padding inside the box

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
                                            color = if (selectedIndex == index) Color.White else Color.Black, // Text color when selected
                                            fontSize = 12.sp, // Font size for the label
                                            fontWeight = FontWeight.Medium,
                                            modifier = Modifier.padding(top = 4.dp) // Padding between icon and label
                                        )
                                    }
                                }
                            },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = Color.White, // Color when selected
                                unselectedIconColor = Color.Black, // Color when not selected
                                unselectedTextColor = Color.Black, // Text color when not selected
                               indicatorColor = Color.Transparent
                            )
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        ContentScreen(
            modifier = Modifier.fillMaxSize(),
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