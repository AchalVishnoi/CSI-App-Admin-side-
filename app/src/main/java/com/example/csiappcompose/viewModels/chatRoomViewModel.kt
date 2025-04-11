package com.example.csiappcompose.viewModels


import android.content.Context
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresExtension
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.csiappcompose.Backend.RetrofitInstance
import com.example.csiappcompose.WebSocketManager
import com.example.csiappcompose.dataModelsResponse.Sender
import com.example.csiappcompose.dataModelsResponse.SenderX
import com.example.csiappcompose.dataModelsResponse.SenderXX
import com.example.csiappcompose.dataModelsResponse.oldChatMessage
import com.example.csiappcompose.dataModelsResponse.parent_message
import com.example.csiappcompose.dataModelsResponse.searchMemberItem
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import retrofit2.HttpException
import java.util.concurrent.atomic.AtomicReference

class ChatRoomViewModel(private val roomId: Int, private val token: String,private val context: Context) : ViewModel() {

    private val webSocketManager = WebSocketManager(roomId, token,context)

    private val _typingUsers = MutableStateFlow<Set<Sender>>(emptySet())




    val typingUsers = webSocketManager.typingUsers







    private val _messages = MutableStateFlow<NetWorkResponse<List<oldChatMessage>>>(NetWorkResponse.Loading)
    val messages = _messages.asStateFlow()


    init {
        webSocketManager.connect()
        viewModelScope.launch {
            webSocketManager.messages.collect { newMessages ->


                val convertedMessages = newMessages.map { msg ->
                    Log.d("WebSocket Processing", "Processing message ID: ${msg.id} with reactions: ${msg.reactions}")
                    oldChatMessage(
                        id = msg.id,
                        content = msg.message,
                        created_at = msg.created_at,
                        message_type = msg.message_type,
                        sender = SenderX(
                            id = msg.sender.id,
                            first_name = msg.sender.name,
                            photo = msg.sender.photo,
                            role = msg.sender.role,
                            last_name = null,
                            year = null
                        ),
                        attachment = msg.attachment,
                        is_deleted = false,
                        is_edited = false,
                        mentions = emptyList(),
                        parent_message = msg.parent_message,
                        reactions = msg.reactions,
                        sendingStatus = "",
                        updated_at = msg.created_at,
                        room = roomId,
                        is_self = msg.is_self,
                        status = null,
                        new_content = msg.new_content,
                        action = msg.action,
                        is_typing = msg.is_typing
                    )
                }

              /* if(convertedMessages.size>0) {

                   _messageToReply.value = convertedMessages.get(0)
                   Log.i("CHAT SCREEN", "new message= ${_messageToReply.value}")
               }
               */

                _messages.update { currentMessages ->
                    val filteredMessages = if (currentMessages is NetWorkResponse.Success) {
                        currentMessages.data.filterNot { it.is_self && it.sendingStatus == "sending" }.toMutableList()
                    } else {
                        mutableListOf()
                    }

                    // Create a new list with updated reactions
                    val updatedMessages = filteredMessages.map { oldMsg ->
                        val newMsg = convertedMessages.find { it.id == oldMsg.id }
                        if (newMsg != null) {
                            when (newMsg.action) {
                                "edited" -> oldMsg.copy(content = newMsg.new_content.toString())
                                "reacted" -> oldMsg.copy(reactions = newMsg.reactions)
                                else -> oldMsg
                            }
                        } else {
                            oldMsg
                        }
                    }.toMutableList()

                    // Append new messages that are not already in the list
                    convertedMessages.forEach { newMsg ->
                        if (updatedMessages.none { it.id == newMsg.id }) {
                            updatedMessages.add(newMsg)
                        }
                    }


                  NetWorkResponse.Success(updatedMessages.toList())
                }





            }
        }
    }




    private var currentPage = 1
    private val _isFetching = MutableStateFlow(false)  // ✅ Kept as val
    val isFetching: StateFlow<Boolean> = _isFetching
    private var hasMoreMessages = true

    @RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
    fun fetchOldMessages() {
        if (_isFetching.value || !hasMoreMessages) return



        viewModelScope.launch(Dispatchers.IO) {
            try {
                withContext(Dispatchers.Main) { _isFetching.value = true }

                val response = RetrofitInstance.apiService
                    .getOldMessages(roomId, "Token $token", currentPage, 100)
                    .results

                withContext(Dispatchers.Main) {
                    if (response.isNotEmpty()) {
                        _messages.update { currentMessages ->
                            val newMessages = response.reversed()
                            when (currentMessages) {
                                is NetWorkResponse.Success -> NetWorkResponse.Success(newMessages + currentMessages.data)
                                else -> NetWorkResponse.Success(newMessages)
                            }
                        }
                        currentPage++
                    } else {
                        hasMoreMessages = false
                    }
                }
            } catch (e: HttpException) {
                val errorCode = e.code()
                when (errorCode) {
                    404 -> {
                        Log.e("API", "No more messages! Stopping pagination.")
                        withContext(Dispatchers.Main) { hasMoreMessages = false }
                    }
                    else -> {
                        Log.e("API", "HTTP Error: $errorCode -> ${e.message()}")
                    }
                }
            } catch (e: Exception) {
                Log.e("API", "Failed to fetch old messages: ${e.message}")
            } finally {
                withContext(Dispatchers.Main) { _isFetching.value = false }
            }
        }
    }

    fun sendMessage(message: String, mentionList:List<Int>,context: Context,soundResId: Int,) {


        val tempMessage = oldChatMessage(
            id = null,
            content = message,
            created_at = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSSXXX", Locale.getDefault()).format(Date()),
            message_type = "text",
            sender = SenderX(id = -1, first_name = "You", photo = null, role = "user", last_name = null, year = null),
            attachment = null,
            is_deleted = false,
            is_edited = false,
            mentions = mentionList,
            parent_message = if(messageToReply.value!=null)
                parent_message(
                content = messageToReply.value?.content.toString(),
                id=messageToReply.value?.id,
                created_at = messageToReply.value?.created_at,
                sender = SenderXX(
                    id =messageToReply.value?.sender?.id,
                    name = messageToReply.value?.sender?.first_name.toString(),
                    photo = messageToReply.value?.sender?.photo.toString())
                   )
            else null,
            reactions = null,
            sendingStatus = "sending",
            updated_at = null,
            room = roomId,
            is_self = true,
            status = null,
            action = null,
            new_content = null,
            is_typing = false
        )


        _messages.update { currentMessages ->
            when (currentMessages) {
                is NetWorkResponse.Success -> {
                    val updatedMessages = currentMessages.data + tempMessage
                    NetWorkResponse.Success(updatedMessages)
                }
                else -> NetWorkResponse.Success(listOf(tempMessage)) // First message case
            }
        }



        webSocketManager.sendMessage(message,mentionList,_messageToReply.value?.id)

        _messageToReply.value=null

        Log.i("SEND BY YOU", "sendMessage: $message")
    }



    fun reactMeassage(react: String, message_id:Int){
     webSocketManager.react(react,message_id)
        Log.i("REACTION", "reactMeassage: Reaction send to id $message_id")

    }





    override fun onCleared() {
        super.onCleared()
        webSocketManager.disconnect()
    }






    private val _searchingResult = MutableLiveData<NetWorkResponse<List<searchMemberItem>>>()
    val searchMemberResult=_searchingResult


    fun searchResult(Query : String){


        viewModelScope.launch{

            var response= RetrofitInstance.apiService.searchMember(roomId,"Token $token",Query)

            try {
                if(response.isSuccessful){
                    Log.i("SEARCH RESULT", "searchResult: ${response.body().toString()}")
                    response.body()?.let {
                        _searchingResult.value= NetWorkResponse.Success(it)
                    }
                }
                else{
                    _searchingResult.value= NetWorkResponse.Error("No user found with $Query")
                }
            }
            catch (e: Exception){
                _searchingResult.value= NetWorkResponse.Error("failed to load data")
            }

        }






    }

       var typingJob: Job?=null

    fun sendIsTyping(isTyping: Boolean){
        if(isTyping) webSocketManager.sendTypingEvent("typing")
        else {
            typingJob?.cancel()
            typingJob = CoroutineScope(Dispatchers.IO).launch {
                delay(500)
                webSocketManager.sendTypingEvent("stop_typing")
            }

        }
    }








    fun clearSearchResult() {
        _searchingResult.value = NetWorkResponse.Success(emptyList())
    }


    fun markAsRead(){
        viewModelScope.launch {
            RetrofitInstance.apiService.markAsRead(roomId, "Token $token")
        }
    }


    // Add to WebSocketManager class
    private val heartbeatJob = AtomicReference<Job?>(null)

//    fun startHeartbeat() {
//        stopHeartbeat() // Cancel any existing heartbeat
//
//        heartbeatJob.set(viewModelScope.launch(Dispatchers.IO) {
//            while (webSocketManager.isConnected.value) {
//                try {
//                    delay(25_000L)
//                    webSocketManager.sendMessage("",em)
//                    Log.d("Heartbeat", "Ping sent")
//                } catch (e: Exception) {
//                    Log.e("Heartbeat", "Failed: ${e.message}")
//                    break
//                }
//            }
//        })
//    }

//    fun stopHeartbeat() {
//        heartbeatJob.get()?.cancel()
//    }




    private val _messageToReply = mutableStateOf<oldChatMessage?>(null)
    val messageToReply = _messageToReply



    fun setMessageToReply(message: oldChatMessage?) {
        _messageToReply.value = message
    }


}
