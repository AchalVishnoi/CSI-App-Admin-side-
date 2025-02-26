package com.example.csiappcompose.pages

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavHostController
import com.example.csiappcompose.pages.Chat.addedGroups
import com.example.csiappcompose.ui.theme.PrimaryBackgroundColor
import com.example.csiappcompose.viewModels.ChatViewModel


@Composable
fun ChatPage(modifier: Modifier = Modifier,chatViewModel: ChatViewModel,navController: NavHostController) {




    addedGroups(navController = navController, viewModel = chatViewModel)
}

