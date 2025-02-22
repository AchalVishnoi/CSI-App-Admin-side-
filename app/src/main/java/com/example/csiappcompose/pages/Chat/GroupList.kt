package com.example.csiappcompose.pages.Chat

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.Alignment
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
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalInspectionMode

import androidx.compose.ui.unit.dp
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel

import coil3.request.ImageRequest
import coil3.request.crossfade
import com.example.csiappcompose.dataModelsResponse.GroupListItem
import com.example.csiappcompose.viewModels.ChatViewModel
import com.example.csiappcompose.viewModels.ChatViewModelFactory
import com.example.csiappcompose.viewModels.NetWorkResponse
import kotlinx.coroutines.flow.collect
import coil3.compose.AsyncImage
import coil3.ImageLoader






@Composable
fun addedGroups(viewModel: ChatViewModel){

    val context= LocalContext.current


//    val chatViewModel : ChatViewModel= viewModel (factory = ChatViewModelFactory(context = context))
//    val token by chatViewModel.token.collectAsState()
//    val groupList by chatViewModel.groupList.collectAsState()

    val result= viewModel.groupChatResult.observeAsState()

    LaunchedEffect(Unit) {
        viewModel.fetchToken()
    }




   Box(modifier = Modifier.fillMaxSize().background(color = PrimaryBackgroundColor).padding(top = 70.dp, start = 10.dp, end=10.dp)){



       when (val result = result.value) {
           is NetWorkResponse.Error -> {
               Box(
                   modifier = Modifier.fillMaxSize(),
                   contentAlignment = Alignment.Center
               ) {
                   Text(text = result.message, color = Color.Red)
               }
           }
           is NetWorkResponse.Loading -> {
               Box(
                   modifier = Modifier.fillMaxSize(),
                   contentAlignment = Alignment.Center
               ) {
                   CircularProgressIndicator()
               }
           }
           is NetWorkResponse.Success -> {
               val groupList = result.data
               Column {
                   row1()
                   groupsListSection(groupList)
               }
           }
           else -> {}
       }



   }

}

@Composable
fun row1(){
    Row(modifier = Modifier.fillMaxWidth().wrapContentHeight().padding(horizontal = 10.dp),
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
                Icon(modifier = Modifier.wrapContentSize().fillMaxSize(0.6f),
                    painter = painterResource(id = R.drawable.meatballs_menu),
                    contentDescription = "Menu",

                )
            }
        }
    }
}

@Composable
fun groupsListSection(groupList: List<GroupListItem>) {
    val listState = rememberLazyListState()
    var isScrollable by remember { mutableStateOf(false) }
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
            .fillMaxWidth().wrapContentHeight()
    ) {
        items(groupList) { item ->
            groupListItem(onClick = {},item)
        }

//        if (listState.canScrollForward) {
//            item {
//                Spacer(modifier = Modifier.height(110.dp))
//            }
//        }
//        item {
//            val bottomPadding = if (listState.canScrollForward) 110.dp else 0.dp
//            Spacer(modifier = Modifier.height(bottomPadding))
//        }

        if (isScrollable) {
            item {
                Spacer(modifier = Modifier.height(110.dp))
            }
        }


    }
}

}


@Composable
fun groupListItem(onClick: () -> Unit, groupListItem: GroupListItem) {

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .background(color = Color(0xFFF7F7F7)).clickable{onClick()}

    ) {
        Row(
            modifier = Modifier.fillMaxWidth().wrapContentHeight()
                .padding(vertical = 10.dp, horizontal = 15.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Card(
                modifier = Modifier
                    .padding(end = 28.dp)
                    .size(60.dp),
                shape = CircleShape,
            ) {
                CompositionLocalProvider(LocalInspectionMode provides false) {
                    if (groupListItem.room_avatar != null) {
                        AsyncImage(
                            model = groupListItem.room_avatar,
                            contentDescription = null,
                            modifier = Modifier
                                .fillMaxSize()
                                .clip(CircleShape),
                            contentScale = ContentScale.Crop
                        )
                    } else {
                        Image(
                            painter = painterResource(id = R.drawable.profile_icon), // Replace with your drawable
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
                    Text(text = "${groupListItem.name}", fontSize = 18.sp)
                    Text(text = "06:25 pm", fontSize = 13.sp, color = Color(0xFF6F6F6F))
                }

                Spacer(modifier = Modifier.height(18.dp))

                Text(
                    text = "Hello!! My name is Achal Vishnoi. And how are you?",
                    fontSize = 13.sp,
                    color = Color(0xFF6F6F6F),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )


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

