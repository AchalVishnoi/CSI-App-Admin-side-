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

class ChatRoomViewModel(private val roomId: Int, private val token: String) : ViewModel() {

    private val webSocketManager = WebSocketManager(roomId, token)

    private val _messages = MutableStateFlow<List<oldChatMessage>>(emptyList())
    val messages = _messages.asStateFlow() // Expose to UI

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
                        sender = SenderX(id=msg.sender.id, first_name = msg.sender.name, photo = msg.sender.photo, role = msg.sender.role, last_name = null, year = null),
                        attachment = msg.attachment,
                        is_deleted = false,
                        is_edited = false,
                        mentions = emptyList(),
                        parent_message = null,
                        reactions = null,
                        status = null,
                        updated_at = msg.created_at,
                        room = roomId

                    )
                }
                _messages.value = convertedMessages
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
        webSocketManager.sendMessage(message)
        Log.i("SEND BY YOU", "sendMessage: $message")
    }

    override fun onCleared() {
        super.onCleared()
        webSocketManager.disconnect()
    }
}
