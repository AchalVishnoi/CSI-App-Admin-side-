package com.example.csiappcompose.pages


import android.content.Intent
import android.util.Log
import androidx.compose.foundation.Image
import coil.compose.AsyncImage
import androidx.compose.foundation.background
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
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
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
import com.example.csiappcompose.dataModelsResponse.DomainX
import com.example.csiappcompose.dataModelsResponse.profileData
import com.example.csiappcompose.pages.Chat.ProfileImage
import com.example.csiappcompose.viewModels.NetWorkResponse
import com.example.csiappcompose.viewModels.ProfilePageViewModel
import com.example.csiappcompose.viewModels.ProfilePageViewModelFactory

@Preview
@Composable
fun ProfilePage() {
    val context = LocalContext.current
    val viewModel: ProfilePageViewModel = viewModel(factory = ProfilePageViewModelFactory(context))

    val tokenState = viewModel.token.collectAsState(initial = "")
    val token = tokenState.value

    var profile = viewModel.profilePage.observeAsState()
    var branch = viewModel.branch.observeAsState()
    var year = viewModel.year.observeAsState()
    var domain = viewModel.domain.observeAsState()
    var dob = viewModel.dob.observeAsState()
    var github = viewModel.github.observeAsState()
    var linkedin = viewModel.linkedin.observeAsState()
    var bio = viewModel.bio.observeAsState()
    var achievements = viewModel.achievements.observeAsState()
    var photo = viewModel.photo.observeAsState()
    val name = viewModel.name.observeAsState()

    LaunchedEffect(Unit) {
        viewModel.fetchToken()
    }
            val scrollState = rememberScrollState()
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
                            when (val response = photo.value) {
                                is NetWorkResponse.Error -> {
                                    Box(
                                        modifier = Modifier.fillMaxSize(),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = "Failed to load data: ${response.message}",
                                            color = Color.Red
                                        )
                                    }
                                }

                                is NetWorkResponse.Loading -> {
                                    ShimmerEffect(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(16.dp))
                                            .background(Color.Gray)
                                    )
                                }

                                is NetWorkResponse.Success -> {
                                    CompositionLocalProvider(LocalInspectionMode provides false) {
                                        if (response.data.photo != null) {
                                            val imgUrl = response.data.photo?.let {
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
                                else ->{
                                    Text("Unexpected state", color = Color.Gray)
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))
                    when (val nameResponse = name.value) {
                        is NetWorkResponse.Loading -> {
                            ShimmerEffect(
                                modifier = Modifier
                                    .padding(horizontal = 20.dp)
                                    .width(100.dp)
                                    .height(25.dp)
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(Color.Gray)
                            )

                        }
                        is NetWorkResponse.Success -> {
                            Text(nameResponse.data.full_name, fontWeight = FontWeight.Bold, fontSize = 20.sp)
                        }
                        is NetWorkResponse.Error -> {
                            Text("Error: ${nameResponse.message}", color = Color.Red)
                        }
                        else -> {
                            Text("Unexpected state", color = Color.Gray)
                        }
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    when (val yearResponse = year.value) {
                        is NetWorkResponse.Loading -> {
                            ShimmerEffect(
                                modifier = Modifier
                                    .padding(horizontal = 20.dp)
                                    .width(50.dp)
                                    .height(25.dp)
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(Color.Gray)
                            )
                        }
                        is NetWorkResponse.Success -> {
                            Text(yearResponse.data.year, fontWeight = FontWeight.Bold, fontSize = 20.sp)
                        }
                        is NetWorkResponse.Error -> {
                            Text("Error: ${yearResponse.message}", color = Color.Red)
                        }
                        else -> {
                            Text("Unexpected state", color = Color.Gray)
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))
                    when (val branchResponse = branch.value) {
                        is NetWorkResponse.Loading -> {
                            ShimmerEffect(
                                modifier = Modifier
                                    .padding(horizontal = 20.dp)
                                    .fillMaxWidth()
                                    .height(35.dp)
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(Color.Gray)
                            )
                        }
                        is NetWorkResponse.Success -> {
                            InfoCard(label = "Branch", value = branchResponse.data.branch)
                        }
                        is NetWorkResponse.Error -> {
                            Text("Error: ${branchResponse.message}", color = Color.Red)
                        }
                        else -> {
                            Text("Unexpected state", color = Color.Gray)
                        }
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    when (val domainResponse = domain.value) {
                        is NetWorkResponse.Loading -> {
                            ShimmerEffect(
                                modifier = Modifier
                                    .padding(horizontal = 20.dp)
                                    .fillMaxWidth()
                                    .height(35.dp)
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(Color.Gray)
                            )
                        }
                        is NetWorkResponse.Success -> {
                            InfoCard(label = "Domain", value = if(domainResponse.data?.domain!=null) domainResponse.data?.domain?.name.toString() else  "fill your domain")
                        }
                        is NetWorkResponse.Error -> {
                            Text("Error: ${domainResponse.message}", color = Color.Red)
                        }
                        else -> {
                            Text("Unexpected state", color = Color.Gray)
                        }
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    when (val dobResponse = dob.value) {
                        is NetWorkResponse.Loading -> {
                            ShimmerEffect(
                                modifier = Modifier
                                    .padding(horizontal = 20.dp)
                                    .fillMaxWidth()
                                    .height(35.dp)
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(Color.Gray)
                            )
                        }
                        is NetWorkResponse.Success -> {
                            InfoCard(label = "D.O.B", value = dobResponse.data.dob)
                        }
                        is NetWorkResponse.Error -> {
                            Text("Error: ${dobResponse.message}", color = Color.Red)
                        }
                        else -> {
                            Text("Unexpected state", color = Color.Gray)
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

//            InfoCard(label = "Linkedin URL", value = linkedin)
//            InfoCard(label = "Git hub URL", value = github)

                    Spacer(modifier = Modifier.height(16.dp))

                    when (val bioResponse = bio.value) {
                        is NetWorkResponse.Loading -> {
                            ShimmerEffect(
                                modifier = Modifier
                                    .padding(horizontal = 20.dp)
                                    .fillMaxWidth()
                                    .height(35.dp)
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(Color.Gray)
                            )
                        }
                        is NetWorkResponse.Success -> {
                            if(bioResponse.data.bio!=null) {
                                SectionHeader("Bio")

                                InfoText(bioResponse.data.bio)
                            }
                        }
                        is NetWorkResponse.Error -> {
                            Text("Error: ${bioResponse.message}", color = Color.Red)
                        }
                        else -> {
                            Text("Unexpected state", color = Color.Gray)
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    when (val achievementsResponse = achievements.value) {
                        is NetWorkResponse.Loading -> {
                            ShimmerEffect(
                                modifier = Modifier
                                    .padding(horizontal = 20.dp)
                                    .fillMaxWidth()
                                    .height(35.dp)
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(Color.Gray)
                            )
                        }
                        is NetWorkResponse.Success -> {
                            if (achievementsResponse.data.achievements != null) {
                                if(achievementsResponse.data.achievements.isNotEmpty()) {
                                    SectionHeader("Achievements")

                                    InfoText(achievementsResponse.data.achievements)
                                }
                            }
                        }
                        is NetWorkResponse.Error -> {
                            Text("Error: ${achievementsResponse.message}", color = Color.Red)
                        }
                        else -> {
                            Text("Unexpected state", color = Color.Gray)
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))


                    Row(modifier=Modifier.fillMaxWidth()) {  }


                    Row(Modifier.width(100.dp).wrapContentHeight()) {

                        when (val githubResponse = github.value) {
                            is NetWorkResponse.Loading -> {
                                ShimmerEffect(modifier = Modifier
                                    .width(24.dp)
                                    .height(35.dp)
                                    .padding(horizontal = 16.dp))
                            }
                            is NetWorkResponse.Success -> {
                                IconButton(onClick = {
                                    val intent = Intent(context, WebViewActivity::class.java)
                                    intent.putExtra("url", githubResponse.data.github_url)
                                    context.startActivity(intent)
                                }) {
                                    Icon(
                                        painter = painterResource(id = R.drawable.github_png),
                                        contentDescription = "GitHub",
                                        tint = Color.Unspecified
                                    )
                                }
                            }
                            is NetWorkResponse.Error -> {
                                Text("Error: ${githubResponse.message}", color = Color.Red)
                            }
                            else -> {
                                Text("Unexpected state", color = Color.Gray)
                            }
                        }



                        when (val linkedinResponse = linkedin.value) {
                            is NetWorkResponse.Loading -> {
                                ShimmerEffect(modifier = Modifier
                                    .width(24.dp)
                                    .height(35.dp)
                                    .padding(horizontal = 16.dp))
                            }
                            is NetWorkResponse.Success -> {
                                IconButton(onClick = {
                                    val intent = Intent(context, WebViewActivity::class.java)
                                    intent.putExtra("url", linkedinResponse.data.linkedin_url)
                                    context.startActivity(intent)
                                }) {
                                    Icon(
                                        painter = painterResource(id = R.drawable.linkedin_png),
                                        contentDescription = "LinkedIn",
                                        tint = Color.Unspecified
                                    )
                                }
                            }
                            is NetWorkResponse.Error -> {
                                Text("Error: ${linkedinResponse.message}", color = Color.Red)
                            }
                            else -> {
                                Text("Unexpected state", color = Color.Gray)
                            }
                        }
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


                Spacer(modifier=Modifier.height(110.dp))
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
