package com.example.csiappcompose.pages.Chat

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.csiappcompose.R
import com.example.csiappcompose.ui.theme.PrimaryBackgroundColor
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.clickable
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.BiasAbsoluteAlignment
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign

import androidx.compose.ui.unit.dp
import androidx.core.app.NotificationCompat
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController


import com.example.csiappcompose.dataModelsResponse.GroupListItem
import com.example.csiappcompose.viewModels.ChatViewModel
import com.example.csiappcompose.viewModels.ChatViewModelFactory
import com.example.csiappcompose.viewModels.NetWorkResponse
import kotlinx.coroutines.flow.collect
import coil.compose.AsyncImage
import org.intellij.lang.annotations.JdkConstants
import java.net.URLEncoder
import java.nio.charset.StandardCharsets
import java.text.SimpleDateFormat
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle


import java.util.*


@Composable
fun addedGroups(
    viewModel: ChatViewModel,
    navController: NavHostController,
    selected: MutableState<String?>
) {
    val context = LocalContext.current

    // Observing ViewModel states
    val groupChatResult = viewModel.groupChatResult.observeAsState()
    val tokenState = viewModel.token.collectAsState(initial = "")
    val token = tokenState.value

    // Fetching the token when the composable is launched
    LaunchedEffect(Unit) {
        viewModel.fetchToken()
    }

    // Main layout
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = PrimaryBackgroundColor)
            .padding(top = 70.dp, start = 10.dp, end = 10.dp)
    ) {
        when (val response = groupChatResult.value) {
            is NetWorkResponse.Error -> {
                // Displaying error message
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = response.message ?: "An error occurred",
                        color = Color.Red
                    )
                }
            }
            is NetWorkResponse.Loading -> {
                // Displaying loading indicator
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
            is NetWorkResponse.Success -> {
                // Displaying the list of groups
                val groupList = response.data
                groupList?.let {
                    Column {
                        row1()
                        groupsListSection(
                            groupList = it,
                            navController = navController,
                            token = token,
                            selected = selected
                        )
                    }
                } ?: run {

                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "No groups available",
                            color = Color.Gray
                        )
                    }
                }
            }

            else->{}
        }
    }
}


@Composable
fun row1(){
    Row(modifier = Modifier
        .fillMaxWidth()
        .wrapContentHeight()
        .padding(horizontal = 10.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically) {
        Text( fontSize= 24.sp, text = "Chats")
        Row (modifier = Modifier.wrapContentSize()
            , horizontalArrangement = Arrangement.SpaceAround
        ){
            IconButton(onClick = { /* Handle click event */ }) {
                Icon(

                    modifier = Modifier.fillMaxSize(0.6f),
                    painter = painterResource(id = R.drawable.search),
                    contentDescription = "Search Icon",

                )
            }
            IconButton(onClick = { }) {
                Icon(modifier = Modifier
                    .wrapContentSize()
                    .fillMaxSize(0.6f),
                    painter = painterResource(id = R.drawable.meatballs_menu),
                    contentDescription = "Menu",

                )
            }
        }
    }
}

@Composable
fun groupsListSection(navController: NavHostController, token: String?, groupList: List<GroupListItem>,
                      selected: MutableState<String?>) {
    //val listState = rememberLazyListState()



            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(5.dp)

                    .clip(RoundedCornerShape(16.dp)),
                shape = RoundedCornerShape(16.dp),

                ) {


                LazyColumn(
                    modifier = Modifier
                        .background(color = Color(0xFFF7F7F7))
                        .fillMaxWidth()

                ) {
                    items(groupList) { item ->
                        groupListItem(
                            groupListItem = item,
                            token = token,
                            navController = navController,
                            selected = selected
                        )
                    }

                }
            }



        Spacer(modifier = Modifier.height(110.dp))



}






@Composable
fun groupListItem(navController: NavHostController, groupListItem: GroupListItem,token:String?,
                  selected: MutableState<String?>) {



    //time formater

    val createdAt=if(groupListItem.last_message!=null) formatTime(groupListItem.last_message.created_at.toString())
                         else "   "
    val lastMessage= if(groupListItem.last_message!=null) groupListItem.last_message.content
                            else "you were added in this group"



    val encodedImageUrl = groupListItem.room_avatar?.toString()?.let {
        URLEncoder.encode(it, StandardCharsets.UTF_8.toString())
    } ?: ""





    Column(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .background(color = Color(0xFFF7F7F7))
            .clickable {

                Log.i("Unread Count", " ${groupListItem.name} Unread Count : ${groupListItem.unread_count} ")

                navController.navigate("chat/${groupListItem.id}/${token}/${groupListItem.name}/${encodedImageUrl}/${groupListItem.unread_count}/")
            },



    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(vertical = 7.dp, horizontal = 15.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Card(
                modifier = Modifier
                    .padding(end = 28.dp)
                    .size(50.dp),
                shape = CircleShape,
            ) {
                CompositionLocalProvider(LocalInspectionMode provides false) {
                    if (groupListItem.room_avatar != null) {
                        val imgUrl = groupListItem.room_avatar?.let {
                            if (it.toString().startsWith("http://")) {
                                it.toString().replace("http://", "https://")
                            } else it
                        } ?: ""

                        Log.d("ROOM_AVATAR", "Final Image URL: $imgUrl")

                       ProfileImage(imgUrl.toString()) {
                        selected.value=imgUrl.toString()
                       }


                        Log.d("ROOM_AVATAR", "groupListItem: ${groupListItem.room_avatar}")
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


            Column(modifier = Modifier.weight(1f)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = groupListItem.name,
                        fontSize = 18.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(2f)
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    Text(
                        text = createdAt,
                        fontSize = 13.sp,
                        color = Color(0xFF6F6F6F),
                        modifier = Modifier.weight(1f),
                        textAlign = TextAlign.End
                    )
                }

                Spacer(modifier = Modifier.height(5.dp))

                    Row (
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ){

                           Text(
                               text = "${lastMessage}",
                               fontSize = 13.sp,
                               color = Color(0xFF6F6F6F),
                               modifier = Modifier.weight(2f),
                               maxLines = 1,
                               overflow = TextOverflow.Ellipsis
                           )

                        if (groupListItem.unread_count != 0) {



                                Card(
                                    modifier = Modifier
                                        .wrapContentSize()
                                        .weight(0.5f)
                                        .size(24.dp),
                                    shape = CircleShape,
                                    colors = CardDefaults.cardColors(
                                        containerColor = Color(
                                            0xFFFF3D00
                                        )
                                    ),

                                    ) {
                                    Box(
                                        modifier = Modifier.fillMaxSize(),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = groupListItem.unread_count.toString(),
                                            fontSize = 12.sp,
                                            color = Color.White,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                }



                        }
                    }


            }
        }


        Divider(
            modifier = Modifier
                .fillMaxWidth(0.85f)
                .height(1.dp)
                .align(Alignment.CenterHorizontally), // Align center horizontally
            color = Color.LightGray
        )


    }




 }





@SuppressLint("SimpleDateFormat")
fun formatTime(createdAt: String): String {
    return try {

        val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSSXXX")
        inputFormat.timeZone = TimeZone.getTimeZone("UTC")


        val date = inputFormat.parse(createdAt)


        val outputFormat = SimpleDateFormat("hh:mm a")
        outputFormat.format(date ?: Date())
    } catch (e: Exception) {
        "Invalid Time"
    }
}





@Composable
fun ProfileImage(imageUrl: String, onClick: () -> Unit) {
    AsyncImage(
        model = imageUrl,
        contentDescription = "Group Profile Picture",
        modifier = Modifier
            .clip(CircleShape)
            .clickable { onClick() },
        contentScale = ContentScale.Crop
    )
}


