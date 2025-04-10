package com.example.csiappcompose.pages


import android.content.Intent
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip

import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.painterResource

import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.example.csiappcompose.dataModelsResponse.Domain
import com.example.csiappcompose.pages.Chat.ProfileImage
import com.example.csiappcompose.viewModels.AuthViewModel

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape

import androidx.compose.foundation.verticalScroll

import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Button

import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.livedata.observeAsState

import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.focusModifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.csiappcompose.R
import com.example.csiappcompose.pages.Chat.ProfileImage
import com.example.csiappcompose.viewModels.HomePageViewModel
import com.example.csiappcompose.viewModels.HomePageViewModelFactory
import com.example.csiappcompose.viewModels.NetWorkResponse


@Composable
fun ProfilePage() {


    val navController = rememberNavController()
    val authViewModel: AuthViewModel = viewModel()
    val context = LocalContext.current
    val logoutState = authViewModel.logoutState.collectAsState()



    LaunchedEffect(logoutState) {
        if (logoutState.value) {
            navController.navigate("login") {
                popUpTo("home") { inclusive = true }
            }
        }
    }

     Column (
        modifier = Modifier.fillMaxSize()
            .background(Color(color = 0xFF3BA90B)),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Text("Welcome!")

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = {
            context.let { ctx -> // ✅ Ensuring context isn't null
                            val intent = Intent(ctx, FillYourDetail()::class.java).apply {
                            }
                          ctx.startActivity(intent)
                        }
        }
        )
            {
            Text("Logout")
        }
    }

}




@Preview
@Composable
fun profileui(
    name: String = "John Doe",
    photo: String? = null,
    branch: String = "Computer Science",
    year: String = "2nd Year",
    domain: Domain?= Domain(1,"App dev"),
    dob: String = "01/01/2000",
    linkedin: String = "https://linkedin.com/in/johndoe",
    github: String = "https://github.com/johndoe",
    bio: String = "Lorem ipsum dolor sit amet, consectetur adipiscing elit.",
    achievements: String? = "1. Achievement 1\n2. Achievement 2\n3. Achievement 3",
    modifier: Modifier = Modifier
        .padding(horizontal = 20.dp)
        .fillMaxWidth()
        .height(200.dp)
) {
    val scrollState = rememberScrollState()
    val context= LocalContext.current
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top=20.dp)
            .background(Color(0xFFF5F5F5))
            .verticalScroll(scrollState),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Spacer(modifier=Modifier.height(70.dp))


        Column(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .background(Color.White, RoundedCornerShape(16.dp))
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box {
                Card(
                    modifier = Modifier
                        .size(150.dp),
                    shape = CircleShape,
                ) {
                    CompositionLocalProvider(LocalInspectionMode provides false) {
                        if (photo != null) {
                            val imgUrl = photo?.let {
                                if (it.toString().startsWith("http://")) {
                                    it.toString().replace("http://", "https://")
                                } else it
                            } ?: ""

                            Log.d("ROOM_AVATAR", "Final Image URL: $imgUrl")

                            ProfileImage(imgUrl.toString()) {
                                //  selected.value=imgUrl.toString()
                            }


                            //Log.d("ROOM_AVATAR", "groupListItem: ${groupListItem.room_avatar}")
                        } else {
                            Image(
                                painter = painterResource(id = R.drawable.profile_icon),
                                contentDescription = "Profile Image",
                                modifier = Modifier
                                    .fillMaxSize()
                                    .clip(CircleShape),
                                contentScale = ContentScale.Crop
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))
            Text(name, fontWeight = FontWeight.Bold, fontSize = 20.sp)
            Text("2nd Year", fontSize = 14.sp)

            Spacer(modifier = Modifier.height(16.dp))
            InfoCard(label = "Branch", value = branch)
            InfoCard(label = "Domain", value = if(domain!=null) domain.name.toString() else  "fill your domain")
            InfoCard(label = "D.O.B", value = dob)

            Spacer(modifier = Modifier.height(8.dp))

//            InfoCard(label = "Linkedin URL", value = linkedin)
//            InfoCard(label = "Git hub URL", value = github)

            Spacer(modifier = Modifier.height(16.dp))
            if(bio!=null) {
                SectionHeader("Bio")

                InfoText(bio)
            }

            Spacer(modifier = Modifier.height(16.dp))
            if (achievements != null) {
                if(achievements.isNotEmpty()) {
                    SectionHeader("Achievements")

                    InfoText(achievements)
                }
            }
            Spacer(modifier = Modifier.height(16.dp))


            Row(modifier=Modifier.fillMaxWidth()) {  }


            Row(Modifier.width(100.dp).wrapContentHeight()) {


/*
                IconButton(onClick = {
                    val intent = Intent(context, WebViewActivity::class.java)
                    intent.putExtra("url", github)
                    context.startActivity(intent)
                }) {
                    Icon(
                        painter = painterResource(id = R.drawable.github_png),
                        contentDescription = "GitHub",
                        tint = Color.Unspecified
                    )
                }




                IconButton(onClick = {
                    val intent = Intent(context, WebViewActivity::class.java)
                    intent.putExtra("url", linkedin)
                    context.startActivity(intent)
                }) {
                    Icon(
                        painter = painterResource(id = R.drawable.linkedin_png),
                        contentDescription = "LinkedIn",
                        tint = Color.Unspecified
                    )
                }
                */

            }






            Spacer(modifier = Modifier.height(24.dp))




            Button(
                onClick = { /* Handle settings */ },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF005EFF))
            ) {
                Icon(Icons.Default.Settings, contentDescription = "Settings", tint = Color.White)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Settings", color = Color.White)
            }
        }


        Spacer(modifier=Modifier.height(90.dp))
    }
}

@Composable
fun InfoCard(label: String, value: String) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = label,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.weight(1f),
                color = Color.Black
            )
            Text(
                text = value,
                color = Color.Gray
            )
        }
    }
}

@Composable
fun SectionHeader(text: String) {
    Text(text, fontSize = 16.sp, fontWeight = FontWeight.Bold, modifier = Modifier)
}

@Composable
fun InfoText(text: String) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Text(
            text = text,
            modifier = Modifier.padding(12.dp),
            fontSize = 14.sp,
            color = Color.DarkGray
        )
    }
}


