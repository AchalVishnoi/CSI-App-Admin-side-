package com.example.csiappcompose.pages.Chat

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.csiappcompose.R
import com.example.csiappcompose.ui.theme.PrimaryBackgroundColor
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.font.FontWeight
import com.example.csiappcompose.ui.theme.primary
import com.example.csiappcompose.viewModels.AiChatViewModel
import com.example.csiappcompose.viewModels.AiMessageModel
import java.net.URLDecoder
import java.nio.charset.StandardCharsets


@Composable
fun AiChat(chatViewModel: AiChatViewModel) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = PrimaryBackgroundColor)
    ) {
//        header("GemCSI")

        MessageList(modifier = Modifier.weight(1f), messages = chatViewModel.messageList)

        writeMessage { message ->
            chatViewModel.sendMessage(message)
        }
    }
}

@Composable
fun MessageList(modifier:Modifier,messages: List<AiMessageModel>) {
    LazyColumn(modifier = modifier,
        reverseLayout = true) {
        items(messages.reversed().size) { index ->
            messageRow(messages.reversed()[index])
        }


    }
}


@Composable
fun messageRow(it: AiMessageModel) {
    val isModel = it.role == "model"

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = if (isModel) Arrangement.Start else Arrangement.End,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .padding(
                    start = if (isModel) 8.dp else 70.dp,
                    end = if (isModel) 70.dp else 8.dp,
                    top = 4.dp,
                    bottom = 4.dp
                )
                .background(
                    color = if (isModel) Color.White else primary,
                    shape = RoundedCornerShape(12.dp)
                )
                .padding(12.dp)
        ) {
            SelectionContainer(){
                Text(
                    text = it.message,
                    color = if(isModel) Color.Black else Color.White,
                    fontWeight = FontWeight.W500
                )
            }

        }
    }
}




@Composable
fun writeMessage(onMessageSend:(String)-> Unit){

    var message by remember { mutableStateOf("") }

    Card (
        modifier = Modifier.padding(horizontal = 8.dp, vertical = 8.dp)
            .fillMaxWidth().wrapContentHeight(),
        shape = RoundedCornerShape(topEnd = 25.dp, bottomEnd = 25.dp, topStart = 25.dp, bottomStart = 25.dp),

        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ){
       Row (
           modifier = Modifier.background(color = Color(0xFFF7F7F7))
               .fillMaxWidth().wrapContentHeight()

               ,
           verticalAlignment = Alignment.CenterVertically
       ){


           TextField(
               value = message,
               onValueChange = { message = it },
               modifier = Modifier.background(color = Color.Transparent)
                   .wrapContentHeight().weight(1f).heightIn(min = 40.dp, max = 120.dp),
               placeholder = { Text("Type a message...") },
               maxLines = 6,
               singleLine = false,
               colors = TextFieldDefaults.colors(
                   focusedContainerColor = Color.Transparent,
                   unfocusedContainerColor = Color.Transparent,
                   cursorColor = Color.Black,
                   focusedIndicatorColor = Color.Transparent,
                   unfocusedIndicatorColor = Color.Transparent,
                   focusedPlaceholderColor = Color.Gray,
                   unfocusedPlaceholderColor = Color.Gray


               )
           )
if(!message.toString().isEmpty()) {
    IconButton(
        onClick = { onMessageSend(message)
                  message=""},
        modifier = Modifier.size(40.dp)
    ) {
        Icon(
            painter = painterResource(id = R.drawable.send_button),
            contentDescription = "Send",
            modifier = Modifier.size(32.dp).padding(end = 3.dp),
            tint = Color.Unspecified
        )
    }
}


       }
   }




}




@Composable
fun header(roomName:String,profilePic: String?,selected: MutableState<String?>){





    Column(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .background(color = Color(0xFFF7F7F7))


    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(vertical = 10.dp, horizontal = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {



            IconButton( onClick = {}) {
                Icon(modifier = Modifier.wrapContentSize().size(20.dp),
                    painter = painterResource(id = R.drawable.back_arrow),
                    contentDescription = "Menu")
            }




            Card(
                modifier = Modifier
                    .padding(end = 28.dp)
                    .size(60.dp),
                shape = CircleShape,
            ) {



//if profile pic selected

                CompositionLocalProvider(LocalInspectionMode provides false) {
                    if (profilePic != null && profilePic.isNotEmpty()) {
                        var decodedImageUrl = URLDecoder.decode(profilePic, StandardCharsets.UTF_8.toString())

                        // Ensure it starts with HTTPS
                        if (decodedImageUrl.startsWith("http://")) {
                            decodedImageUrl = "https://" + decodedImageUrl.removePrefix("http://")
                        }

                        GroupProfileImage(decodedImageUrl) {
                            selected.value = decodedImageUrl
                        }

                        Log.d("ROOM_AVATAR", "groupListItem: $decodedImageUrl")
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




                /*   Image(
                       painter = painterResource(id = R.drawable.gem_csi_logo), // Replace with your drawable
                       contentDescription = "Profile Image",
                       modifier = Modifier
                           .fillMaxSize()
                           .clip(CircleShape),
                       contentScale = ContentScale.Crop
                   )*/

                }

            Column(modifier = Modifier.weight(1f)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "$roomName", fontSize = 20.sp)

                }



            }


            }



        Divider()
        }





    }

