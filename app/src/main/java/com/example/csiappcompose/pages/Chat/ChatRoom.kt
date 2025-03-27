package com.example.csiappcompose.pages.Chat

import android.R.attr.height
import android.annotation.SuppressLint
import android.os.Build
import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.annotation.RequiresExtension
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
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
import java.net.URLDecoder
import java.nio.charset.StandardCharsets

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
                .padding(paddingValues)
        ) {
            val (messagesList, chatBoxRef) = createRefs() // ✅ Define constraints

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
                viewModel=viewModel

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
    reactMessage: (String, Int) -> Unit,
) {
    var previousSenderId: Int? = null
    val isFetching = viewModel.isFetching.collectAsState().value



    val lazyListState = rememberLazyListState()
    var unreadIndex = unreadCount.value



    LazyColumn(
        modifier = modifier,
        reverseLayout = true,
        state = lazyListState
    ) {
        items(messages.size) { index ->

            if (unreadCount.value>0&&index==unreadCount.value) {
                UnreadMessagesTag()
            }


            val currentMessage = messages[index]
            val showProfile = previousSenderId == null || previousSenderId != currentMessage.sender.id

            RoomMessageRow(currentMessage, isMessageSelected, reactMessage, showProfile)

            previousSenderId = currentMessage.sender.id
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




}


@SuppressLint("SuspiciousIndentation")
@Composable
fun RoomMessageRow(
    it: oldChatMessage,
    isMessageSelected: MutableState<Boolean>,
    reactMessage:(String, Int)-> Unit,
    showImage: Boolean

) {





        val openDialog = remember { mutableStateOf(false) }

        val selectedText = remember { mutableStateOf("Nothing") }


        val isModel = it.is_self
        val isSelected = remember { mutableStateOf(false) }
        val popupAnchor = remember { mutableStateOf<Offset?>(null) }








        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(color = if (openDialog.value) lightWaterBlue else Color.Transparent)
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
            verticalAlignment = Alignment.CenterVertically
        ) {

            if (!isModel) {


                Card(
                    modifier = Modifier
                        .padding(end = 10.dp)
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


            }


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
                    .pointerInput(Unit) {
                        detectHorizontalDragGestures{ change,dragAmount->
                            if(dragAmount>50) {

                            }

                        }
                    }

            ) {
                Column {

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
                    if (it.reactions != null) {

                        var reaction = ""


                        var likeCnt = 0
                        var coolCnt = 0
                        var haHaCnt = 0
                        var heartCnt = 0
                        var angerCnt = 0
                        var smileCnt = 0
                        var sadCnt = 0

                        it.reactions?.let { reaction ->
                            likeCnt = reaction.Like ?: 0
                            coolCnt = reaction.Cool ?: 0
                            haHaCnt = reaction.HaHa ?: 0
                            heartCnt = reaction.Heart ?: 0
                            angerCnt = reaction.Anger ?: 0
                            smileCnt = reaction.Smile ?: 0
                            sadCnt = reaction.Sad ?: 0
                        }

                        if (likeCnt > 0) {
                            reaction += "\uD83D\uDC4D:$likeCnt "
                        }
                        if (coolCnt > 0) {
                            reaction += "\uD83D\uDE0E:$coolCnt "
                        }
                        if (haHaCnt > 0) {
                            reaction += "\uD83D\uDE02:$haHaCnt "
                        }
                        if (heartCnt > 0) {
                            reaction += "\u2764\uFE0F:$heartCnt "
                        }
                        if (angerCnt > 0) {
                            reaction += "\uD83D\uDE21:$angerCnt "
                        }
                        if (smileCnt > 0) {
                            reaction += "\uD83D\uDE42:$smileCnt "
                        }
                        if (sadCnt > 0) {
                            reaction += "\uD83D\uDE22:$sadCnt "
                        }


                        Text(
                            text = reaction,
                            color = if (!isModel) Color.Gray else Color.LightGray,
                            fontWeight = FontWeight.W400,
                            fontSize = 10.sp,
                            modifier = Modifier.align(Alignment.End)
                        )
                    }


                }



                if (openDialog.value) {
                    popUpWindowAnimated(isPopUpOpen = openDialog, selected = selectedText,it.id,reactMessage)
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

    val searchMemberState = viewModel.searchMemberResult.observeAsState()


    var mentionList by remember { mutableStateOf(emptyList<Int>()) }




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
                        }

                        else{
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
                            onMessageSend(message,mentionList)
                            message = ""
                            viewModel.markAsRead()
                            unreadCount.value=0
                            mentionList=emptyList()
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



