package com.example.csiappcompose.pages.Chat

import android.R.attr.height
import android.annotation.SuppressLint
import android.os.Build
import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.annotation.RequiresExtension
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.FractionalThreshold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Reply
import androidx.compose.material.icons.filled.Scanner
import androidx.compose.material.rememberSwipeableState
import androidx.compose.material.swipeable
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester.Companion.createRefs
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.size.Dimension
import com.example.csiappcompose.R
import com.example.csiappcompose.dataModelsResponse.oldChatMessage
import com.example.csiappcompose.displayInFullScreen
import com.example.csiappcompose.ui.theme.PrimaryBackgroundColor
import com.example.csiappcompose.ui.theme.lightWaterBlue
import com.example.csiappcompose.ui.theme.primary
import com.example.csiappcompose.viewModels.ChatRoomViewModel
import com.example.csiappcompose.viewModels.ChatRoomViewModelFactory
import com.example.csiappcompose.viewModels.NetWorkResponse
import androidx.navigation.NavController
import com.example.csiappcompose.dataModelsResponse.Reactions
import com.example.csiappcompose.dataModelsResponse.SenderXX
import com.example.csiappcompose.dataModelsResponse.parent_message
import com.example.csiappcompose.ui.theme.skyBlue
import com.google.common.collect.Multimaps.index
import kotlinx.coroutines.delay
import java.net.URLDecoder
import java.nio.charset.StandardCharsets
import kotlin.math.roundToInt

@RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
@Composable
fun ChatRoomScreen(roomId: Int, token: String, RoomName: String, profilePic: String?,navController: NavController, unreadCnt:Int) {
    val openDialog = remember { mutableStateOf(false) }
    val isMessageSelected = remember { mutableStateOf(false) }
    val selectedText = remember { mutableStateOf("Nothing") }
    var LocalContext= LocalContext.current

    val viewModel: ChatRoomViewModel = viewModel(factory = ChatRoomViewModelFactory(roomId, token,LocalContext))
    val messagesResponse by viewModel.messages.collectAsState(NetWorkResponse.Loading)



    Log.i("UNREAD COUNT", "ChatRoomScreen: Unread count $unreadCnt")

   val unreadCount=remember { mutableStateOf(unreadCnt) }


    val messages = if (messagesResponse is NetWorkResponse.Success) {
        (messagesResponse as NetWorkResponse.Success).data
    } else {
        emptyList()
    }

    var unreadMessageId: Int?=null


//        if(unreadMessageId==null&&messages.size>=unreadCount.value)
//        unreadMessageId=messages[unreadCount.value].id
//





    val typingUsers by viewModel.typingUsers.collectAsState(emptySet())




    // For selected image
    var selected = remember { mutableStateOf<String?>(null) }


   // while exiting the screen
    DisposableEffect(Unit) {
        onDispose {
            viewModel.markAsRead()
        }
    }

    BackHandler {
        viewModel.markAsRead()
        navController.popBackStack()

    }


    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .background(color = PrimaryBackgroundColor)
            .consumeWindowInsets(WindowInsets.ime),
        topBar = { header(RoomName, profilePic, selected,navController,viewModel) }
    ) { paddingValues ->

    ConstraintLayout(
            modifier = Modifier
                .fillMaxSize()
                .background(color = skyBlue)
                .padding(paddingValues)
        ) {
            val (messagesList, chatBoxRef) = createRefs()

            RoomMessageList(
                modifier = Modifier.constrainAs(messagesList) {
                    top.linkTo(parent.top)
                    bottom.linkTo(chatBoxRef.top)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    height = androidx.constraintlayout.compose.Dimension.fillToConstraints
                },
                messages = messages.asReversed(),
                isMessageSelected = isMessageSelected,
                unreadCount =unreadCount,
                unreadMessageId = unreadMessageId,
                viewModel=viewModel,
                onReply = {

                    viewModel.setMessageToReply(it)

                }

            ) { reaction, id ->
                viewModel.reactMeassage(reaction, id)
            }



        if(typingUsers.isNotEmpty()){


            Log.i("TYPING USERS", "ChatRoomScreen: $typingUsers")



            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(color = Color.Transparent),

                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    modifier = Modifier.padding(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    typingUsers.forEach { it ->
                        Card(
                            modifier = Modifier
                                .padding(end = 10.dp)
                                .size(40.dp),
                            shape = CircleShape,
                        ) {
                            CompositionLocalProvider(LocalInspectionMode provides false) {
                                if (it.photo != null) {
                                    val imgUrl = it.photo.let {
                                        if (it.toString().startsWith("http://")) {
                                            it.toString().replace("http://", "https://")
                                        } else it
                                    } ?: ""

                                    ProfileImage(imgUrl.toString()) {}


                                    Log.d("ROOM_AVATAR", "groupListItem: ${it.photo}")
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

                }





                Box(
                    modifier = Modifier
                        .padding(
                            start = 8.dp,
                            end = 70.dp,
                            top = 4.dp,
                            bottom = 4.dp
                        )
                        .background(
                            color = Color.White,
                            shape = RoundedCornerShape(12.dp)
                        )
                        .padding(12.dp)
                ) {
                    Column {

                        Text(
                            text = "Typing....",
                            color = Color.Black,
                            fontWeight = FontWeight.W500
                        )
                    }

                }

            }
        }
            writeMessage(
                onMessageSend = { message,mentionList ->
                    viewModel.sendMessage(message,mentionList,LocalContext,R.raw.message_sending_sound)
                },
                viewModel,
                modifier = Modifier.constrainAs(chatBoxRef) {
                    bottom.linkTo(parent.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)

                }
                ,
                unreadCount=unreadCount
            )
        }

        selected?.value?.let { image ->
            displayInFullScreen(image = image) {
                selected.value = null
            }
        }
    }
}


@RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
@Composable
fun RoomMessageList(
    modifier: Modifier,
    messages: List<oldChatMessage>,
    isMessageSelected: MutableState<Boolean>,
    viewModel: ChatRoomViewModel,
    unreadCount: MutableState<Int>,
    unreadMessageId: Int?,
    onReply: (oldChatMessage) -> Unit,
    reactMessage: (String, Int) -> Unit,

    ) {
    val isFetching = viewModel.isFetching.collectAsState().value
    val lazyListState = rememberLazyListState()

    LazyColumn(
        modifier = modifier,
        reverseLayout = true,
        state = lazyListState
    ) {
        itemsIndexed(messages, key = { _, msg -> msg.localId }) { index, currentMessage ->

            val showProfile = index == messages.lastIndex ||
                    messages.getOrNull(index + 1)?.sender?.id != currentMessage.sender.id

            RoomMessageRow(currentMessage, isMessageSelected, reactMessage, showProfile, onReply)
        }

        if (isFetching) {
            item {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    CircularProgressIndicator()
                }
            }
        }
    }

    LaunchedEffect(lazyListState) {
        if (!isFetching) {
            snapshotFlow { lazyListState.firstVisibleItemIndex }
                .collect { index ->
                    if (index == 0) {
                        viewModel.fetchOldMessages()
                    }
                }
        }
    }


    val previousMessageCount = remember { mutableStateOf(messages.size) }

    LaunchedEffect(messages.size) {
        if (messages.size > previousMessageCount.value) {
            lazyListState.scrollToItem(0)
        }
        previousMessageCount.value = messages.size
    }

    

}


@OptIn(ExperimentalMaterialApi::class)
@SuppressLint("SuspiciousIndentation")
@Composable
fun RoomMessageRow(
    it: oldChatMessage,
    isMessageSelected: MutableState<Boolean>,
    reactMessage:(String, Int)-> Unit,
    showImage: Boolean,
    onReply: (oldChatMessage) -> Unit

) {





        val openDialog = remember { mutableStateOf(false) }

        val selectedText = remember { mutableStateOf("Nothing") }


        val isModel = it.is_self
        val isSelected = remember { mutableStateOf(false) }
        val popupAnchor = remember { mutableStateOf<Offset?>(null) }
      val swipeState = rememberSwipeableState(initialValue = 0)
    val density = LocalDensity.current
    val swipeThreshold = remember { with(density) { 100.dp.toPx() } }

    val backgroundColor = if (openDialog.value) {
        lightWaterBlue
    } else {
        Color.Transparent
    }


    LaunchedEffect(swipeState.currentValue) {
        if (swipeState.currentValue == 1) {
            delay(150)
            swipeState.snapTo(0)
            onReply(it)
        }
    }




    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(backgroundColor)
    ) {

        if (swipeState.offset.value > 0) {
            Box(
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .padding(start = 16.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Reply,
                    contentDescription = "Reply",
                    tint = primary,
                    modifier = Modifier.size(24.dp)
                )


            }
        }



        Row(
            modifier = Modifier.fillMaxWidth( )

                .pointerInput(Unit) {
                    detectTapGestures(
                        onLongPress = { offset ->
                            openDialog.value = true
                            isMessageSelected.value = true
                            popupAnchor.value = offset
                        }
                    )
                },
            horizontalArrangement = if (!isModel) Arrangement.Start else Arrangement.End,
            verticalAlignment = Alignment.Top
        ) {


            Row(
                modifier = Modifier
                    .swipeable(
                        state = swipeState,
                        anchors = mapOf(
                            0f to 0,
                            swipeThreshold to 1
                        ),
                        thresholds = { _, _ -> FractionalThreshold(0.5f) },
                        orientation = Orientation.Horizontal
                    )
                    .offset { IntOffset(swipeState.offset.value.roundToInt(), 0) }
            ){
            if (!isModel && showImage) {


                Card(
                    modifier = Modifier

                        .padding(end = 3.dp, start = 3.dp, top = 5.dp)
                        .size(40.dp),
                    shape = CircleShape,
                ) {
                    CompositionLocalProvider(LocalInspectionMode provides false) {
                        if (it.sender.photo != null) {
                            val imgUrl = it.sender.photo.let {
                                if (it.toString().startsWith("http://")) {
                                    it.toString().replace("http://", "https://")
                                } else it
                            } ?: ""

                            ProfileImage(imgUrl.toString()) {}


                            Log.d("ROOM_AVATAR", "groupListItem: ${it.sender.photo}")
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


            } else {
                Spacer(modifier = Modifier.width(50.dp))
            }


            Box(
                modifier = Modifier
                    .wrapContentHeight()
                    .padding(
                        start = if (!isModel) 8.dp else 70.dp,
                        end = if (!isModel) 70.dp else 8.dp,
                        top = if (showImage) 8.dp else 2.dp,
                        bottom = if (it.reactions != null) 16.dp else 0.dp
                    )
            ) {
                Box(
                    modifier = Modifier
                        .background(
                            color = if (!isModel) Color.White else primary,
                            shape = RoundedCornerShape(
                                bottomStart = 12.dp,
                                bottomEnd = 12.dp,
                                topStart = if (!isModel && showImage) 0.dp else 12.dp,
                                topEnd = if (isModel && showImage) 0.dp else 12.dp
                            )
                        )
                        .padding(bottom = 8.dp, start = 8.dp, end = 8.dp)
                ) {





                    Column {

                        Spacer(modifier = Modifier.height(4.dp))

                        if (!isModel && showImage){

                            Text(
                                text = "~${it.sender.first_name} ${it.sender.last_name}",
                                fontWeight = FontWeight.Bold,
                                color = primary
                            )

                        }
                        
                        if(it.parent_message!=null){
                            ReplyPreviewMini(

                                repliedMessage = it.parent_message,
                                isUserMessage = isModel

                            )
                        }


                        
                        
                        

                        Text(
                            text = it.content,
                            color = if (!isModel) Color.Black else Color.White,
                            fontWeight = FontWeight.W500
                        )

                        if (!it.sendingStatus.isNullOrEmpty()) {
                            Text(
                                text = it.sendingStatus,
                                color = if (!isModel) Color.Gray else Color.LightGray,
                                fontWeight = FontWeight.W400,
                                fontSize = 10.sp,
                                modifier = Modifier.align(Alignment.End)
                            )
                        }
                    }


                    if (it.reactions != null) {
                        val allReactions = getAllReactions(it.reactions!!)
                        if (allReactions.isNotEmpty()) {
                            Row(
                                modifier = Modifier
                                    .align(Alignment.BottomEnd)
                                    .offset(x = -2.dp, y = 25.dp)
                                    .background(
                                        color = if (isModel) Color(0xFFDCF8C6) else Color.White,
                                        shape = RoundedCornerShape(16.dp)
                                    )
                                    .border(
                                        0.5.dp,
                                        if (isModel) Color(0xFFB2D8A3) else Color.LightGray,
                                        RoundedCornerShape(16.dp)
                                    )
                                    .padding(horizontal = 4.dp, vertical = 2.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(2.dp)
                            ) {
                                allReactions.forEach { reaction ->
                                    WhatsAppReactionBadge(
                                        emoji = reaction.emoji,
                                        count = reaction.count,
                                        isUserMessage = isModel
                                    )
                                }
                            }
                        }
                    }
                }

                if (openDialog.value) {
                    popUpWindowAnimated(
                        isPopUpOpen = openDialog,
                        selected = selectedText,
                        it.id,
                        reactMessage
                    )
                }
            }
        }
        }


    }

}




@Composable
fun header(roomName:String,profilePic: String?,selected: MutableState<String?>,navController: NavController,viewModel: ChatRoomViewModel){





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



            IconButton( onClick = {
                                   viewModel.markAsRead()
                                  navController.popBackStack()
                                  }) {
                Icon(modifier = Modifier
                    .wrapContentSize()
                    .size(20.dp),
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

                        ProfileImage(decodedImageUrl) {
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



@Composable
fun UnreadMessagesTag() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Divider(
            color = Color.Blue,
            modifier = Modifier
                .weight(1f)
                .height(1.dp)
        )

        Text(
            text = "Unread Messages",
            color = Color.Blue,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 8.dp)
        )

        Divider(
            color = Color.Blue,
            modifier = Modifier
                .weight(1f)
                .height(1.dp)
        )
    }
}


@Composable
fun writeMessage(onMessageSend: (String,List<Int>) -> Unit, viewModel: ChatRoomViewModel,modifier: Modifier,unreadCount: MutableState<Int>) {
    var message by remember { mutableStateOf("") }
    var mentionIdx by remember { mutableStateOf(-1) }

    val messageToReply by viewModel.messageToReply

    val searchMemberState = viewModel.searchMemberResult.observeAsState()


    var mentionList by remember { mutableStateOf(emptyList<Int>()) }

    Column (modifier = modifier){
        messageToReply?.let { reply ->
            ReplyPreview(
                message = reply,
                onCancelReply = { viewModel.setMessageToReply(null) }
            )
        }



        Card(
            modifier = modifier
                .padding(horizontal = 8.dp, vertical = 8.dp)
                .fillMaxWidth()
                .wrapContentHeight(),
            shape = RoundedCornerShape(25.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {


            Column {
                when (val searchMember = searchMemberState.value) {
                    is NetWorkResponse.Success -> {
                        val list = searchMember.data

                        if (list.isNotEmpty()) {
                            LazyColumn(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .wrapContentHeight()
                                    .background(color = Color.White)
                                    .weight(1f)
                                    .padding(horizontal = 8.dp, vertical = 4.dp)
                            ) {
                                items(list) { member ->
                                    Text(
                                        text = "${member.first_name} ${member.last_name}",
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(12.dp)
                                            .clickable {


                                                message = message.substring(
                                                    0,
                                                    message.lastIndexOf('@') + 1
                                                ) + "${member.first_name} ${member.last_name} "
                                                mentionIdx = message.lastIndexOf('@')
                                                mentionList = mentionList + member.id

                                                viewModel.clearSearchResult()
                                            }
                                    )


                                }
                            }
                        }
                    }

                    is NetWorkResponse.Loading -> {
                        Box(
                            modifier = Modifier.fillMaxWidth(),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator()
                        }
                    }

                    else -> {}
                }


                Row(
                    modifier = Modifier
                        .background(color = Color(0xFFF7F7F7))
                        .fillMaxWidth()
                        .wrapContentHeight(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TextField(
                        value = message,
                        onValueChange = {
                            message = it

                            viewModel.sendIsTyping(true)


                            val lastAtIndex = it.lastIndexOf('@')

                            if (lastAtIndex != -1 && lastAtIndex != mentionIdx) {
                                if (lastAtIndex < it.length - 1) {
                                    val mentionText = it.substring(lastAtIndex + 1)
                                    viewModel.searchResult(mentionText)
                                }

                                if (it.lastOrNull() == ' ') {
                                    mentionIdx = lastAtIndex
                                }
                            } else {
                                viewModel.clearSearchResult()
                            }


                            viewModel.sendIsTyping(false)
                        },
                        modifier = Modifier
                            .weight(1f)
                            .heightIn(min = 40.dp, max = 120.dp),
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

                    if (message.isNotEmpty()) {
                        IconButton(
                            onClick = {
                                onMessageSend(message, mentionList)
                                message = ""
                                viewModel.markAsRead()
                                unreadCount.value = 0
                                mentionList = emptyList()
                            },
                            modifier = Modifier.size(40.dp)
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.send_button),
                                contentDescription = "Send",
                                modifier = Modifier
                                    .size(32.dp)
                                    .padding(end = 3.dp),
                                tint = Color.Unspecified
                            )
                        }
                    }
                }
            }
        }
    }
}



@Composable
fun WhatsAppReactionBadge(
    emoji: String,
    count: Int,
    isUserMessage: Boolean
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(2.dp)
    ) {
        Text(
            text = emoji,
            fontSize = 12.sp,
            modifier = Modifier.offset(y = (-1).dp)
        )

        if (count > 1) {
            Text(
                text = "$count",
                color = if (isUserMessage) Color(0xFF075E54) else Color.Gray,
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}



@Composable
fun ReplyPreview(
    message: oldChatMessage,
    onCancelReply: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.LightGray.copy(alpha = 0.2f))
            .padding(8.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
//            Icon(
//                imageVector = Icons.Default.Reply,
//                contentDescription = "Replying to",
//                tint = primary
//            )
            Spacer(modifier = Modifier.width(8.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Replying to ${message.sender.first_name}",
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = message.content.take(30) + if (message.content.length > 30) "..." else "",
                    maxLines = 1
                )
            }
            IconButton(onClick = onCancelReply) {
                Icon(Icons.Default.Close, "Cancel reply")
            }
        }
    }
}

@Preview
@Composable
fun ReplyPreviewMini(
    repliedMessage : parent_message= parent_message(content = "hello my name is achal vishnoi",
        id = 10,
        sender = SenderXX(
            name = "Achal",
            photo = null,
            id=1
        ),
        created_at = ""),
       isUserMessage: Boolean=false
) {


        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    color = if (isUserMessage)
                        Color.White.copy(alpha = 0.2f)
                    else
                        Color.Black.copy(alpha = 0.1f),
                    shape = RoundedCornerShape(8.dp)
                        )
                        .border(
                            1.dp,
                            if (isUserMessage) Color.White.copy(alpha = 0.3f)
                            else Color.Black.copy(alpha = 0.2f),
                            RoundedCornerShape(8.dp)
                        )

                        .padding(8.dp)
                )

                {


            
            Column {


                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Reply,
                        contentDescription = "Replying to",
                        tint = if (isUserMessage) Color.White else Color.Gray,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "${repliedMessage.sender?.name}",
                        fontWeight = FontWeight.Bold
                    )

                }
                Text(
                    text = repliedMessage.content.take(30) + if (repliedMessage.content.length > 30) "..." else "",
                    color =  Color.LightGray,
                    fontSize = 12.sp,
                    maxLines = 1
                )
            }


                     



                 



                    
                }



}



private fun getAllReactions(reactions: Reactions): List<ReactionItem> {
    return listOf(
        ReactionItem("\uD83D\uDC4D", reactions.Like ?: 0),
        ReactionItem("\uD83D\uDE0E", reactions.Cool ?: 0),
        ReactionItem("\uD83D\uDE02", reactions.HaHa ?: 0),
        ReactionItem("\u2764\uFE0F", reactions.Heart ?: 0),
        ReactionItem("\uD83D\uDE21", reactions.Anger ?: 0),
        ReactionItem("\uD83D\uDE42", reactions.Smile ?: 0),
        ReactionItem("\uD83D\uDE22", reactions.Sad ?: 0)
    ).filter { it.count > 0 }
}

private data class ReactionItem(
    val emoji: String,
    val count: Int
)