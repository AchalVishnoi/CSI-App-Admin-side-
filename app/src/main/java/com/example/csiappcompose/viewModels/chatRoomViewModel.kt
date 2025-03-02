package com.example.csiappcompose.viewModels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.csiappcompose.Backend.ApiService
import com.example.csiappcompose.Backend.RetrofitInstance
import com.example.csiappcompose.WebSocketManager
import com.example.csiappcompose.dataModelsResponse.Sender
import com.example.csiappcompose.dataModelsResponse.SenderX
import com.example.csiappcompose.dataModelsResponse.Status
import com.example.csiappcompose.dataModelsResponse.chatMessages
import com.example.csiappcompose.dataModelsResponse.oldChatMessage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ChatRoomViewModel(private val roomId: Int, private val token: String) : ViewModel() {

    private val webSocketManager = WebSocketManager(roomId, token)

    private val _messages = MutableStateFlow<List<oldChatMessage>>(emptyList())
    val messages = _messages.asStateFlow()

    init {
        webSocketManager.connect()
        viewModelScope.launch {
            webSocketManager.messages.collect { newMessages ->


                val convertedMessages = newMessages.map { msg ->


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
                            parent_message = null,
                            reactions = msg.reactions,
                            sendingStatus = "",
                            updated_at = msg.created_at,
                            room = roomId,
                            is_self = msg.is_self,
                            status = null,
                            new_content = msg.new_content,
                            action = msg.action
                        )

                }

                _messages.update { currentMessages ->
                    val filteredMessages = mutableListOf<oldChatMessage>()


                    for (oldMsg in currentMessages) {
                        if (!(oldMsg.is_self && oldMsg.sendingStatus == "sending")) {
                            filteredMessages.add(oldMsg)
                        }
                    }

                    // Add only new unique messages
                    for (newMsg in convertedMessages) {

                        if(newMsg.action=="edited"){
                            for (filteredMsg in filteredMessages) {
                                if (filteredMsg.id == newMsg.id) {

                                    filteredMsg.content=newMsg.new_content.toString()
                                    break
                                }
                            }
                        }

                        else if(newMsg.action=="reacted"){

                            for (filteredMsg in filteredMessages) {
                                if (filteredMsg.id == newMsg.id) {

                                    filteredMsg.reactions=newMsg.reactions
                                    break
                                }
                            }


                        }

                        else{

                            var isUnique = true
                            for (filteredMsg in filteredMessages) {
                                if (filteredMsg.id == newMsg.id) {
                                    isUnique = false
                                    break
                                }
                            }
                            if (isUnique) {
                                filteredMessages.add(newMsg)
                            }
                        }

                    }








                    filteredMessages
                }
            }
        }
    }





    fun fetchOldMessages() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val oldMessages = RetrofitInstance.apiService.getOldMessages(roomId, "Token $token")
                withContext(Dispatchers.Main) {
                    _messages.update { oldMessages.reversed() + it }
                }
            } catch (e: Exception) {
                Log.e("API", "Failed to fetch old messages: ${e.message}")
            }
        }
    }


    fun sendMessage(message: String) {
        val tempMessage = oldChatMessage(
            id = null,
            content = message,
            created_at = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSSXXX", Locale.getDefault()).format(Date()),
            message_type = "text",
            sender = SenderX(id = -1, first_name = "You", photo = null, role = "user", last_name = null, year = null),
            attachment = null,
            is_deleted = false,
            is_edited = false,
            mentions = emptyList(),
            parent_message = null,
            reactions = null,
            sendingStatus = "sending",
            updated_at = null,
            room = roomId,
            is_self = true,
            status = null,
            action = null,
            new_content = null
        )


        _messages.update { it + tempMessage }

        webSocketManager.sendMessage(message)

        Log.i("SEND BY YOU", "sendMessage: $message")
    }


    fun reactMeassage(react: String, message_id:Int){
     webSocketManager.react(react,message_id)
    }


    override fun onCleared() {
        super.onCleared()
        webSocketManager.disconnect()
    }
}
