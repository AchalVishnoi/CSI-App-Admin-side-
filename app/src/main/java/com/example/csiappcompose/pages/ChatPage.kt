package com.example.csiappcompose.pages


import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.csiappcompose.pages.Chat.addedGroups
import com.example.csiappcompose.viewModels.ChatViewModel


@Composable
fun ChatPage(modifier: Modifier = Modifier, chatViewModel: ChatViewModel) {
    addedGroups(chatViewModel)
}

