package com.example.csiappcompose.pages.Chat

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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.csiappcompose.R
import com.example.csiappcompose.dataModelsResponse.oldChatMessage
import com.example.csiappcompose.ui.theme.PrimaryBackgroundColor
import com.example.csiappcompose.ui.theme.primary
import com.example.csiappcompose.viewModels.AiChatViewModel
import com.example.csiappcompose.viewModels.AiMessageModel
import com.example.csiappcompose.viewModels.ChatRoomViewModel
import com.example.csiappcompose.viewModels.ChatRoomViewModelFactory
import com.example.csiappcompose.viewModels.ChatViewModelFactory


@Composable
fun ChatRoomScreen(roomId: Int, token:String,RoomName:String) {


    val viewModel: ChatRoomViewModel = viewModel(factory = ChatRoomViewModelFactory(roomId, token))

    val messages by viewModel.messages.collectAsState(emptyList())

    LaunchedEffect(Unit) {
        viewModel.fetchOldMessages()
    }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = PrimaryBackgroundColor)
    ) {
        header(RoomName)

        RoomMessageList(modifier = Modifier.weight(1f), messages = messages)

        writeMessage { message ->
            viewModel.sendMessage(message)
        }
    }
}

@Composable
fun RoomMessageList(modifier:Modifier,messages: List<oldChatMessage>) {
    LazyColumn(modifier = modifier,
        reverseLayout = true) {
        items(messages.reversed().size) { index ->
            RoomMessageRow(messages.reversed()[index])
        }


    }
}


@Composable
fun RoomMessageRow(it : oldChatMessage) {
    val isModel = it.sender.id == 13

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = if (!isModel) Arrangement.Start else Arrangement.End,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .padding(
                    start = if (!isModel) 8.dp else 70.dp,
                    end = if (!isModel) 70.dp else 8.dp,
                    top = 4.dp,
                    bottom = 4.dp
                )
                .background(
                    color = if (!isModel) Color.White else primary,
                    shape = RoundedCornerShape(12.dp)
                )
                .padding(12.dp)
        ) {
            SelectionContainer(){
                Text(
                    text = it.content,
                    color = if(!isModel) Color.Black else Color.White,
                    fontWeight = FontWeight.W500
                )
            }

        }
    }
}







