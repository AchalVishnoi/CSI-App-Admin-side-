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

//        writeMessage { message ->
//            chatViewModel.sendMessage(message)
//        }
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









