package com.example.csiappcompose

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.ui.platform.LocalContext
import com.example.csiappcompose.dataModelsResponse.Sender
import com.example.csiappcompose.dataModelsResponse.chatMessages

import com.example.csiappcompose.pages.Chat.playSound
import com.google.gson.Gson
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.WebSocket
import okhttp3.WebSocketListener


class WebSocketManager(private val roomId: Int, private val token: String,private val context: Context) {

    private val client = OkHttpClient()
    private var webSocket: WebSocket? = null
    private val gson = Gson()

    private val _messages = MutableStateFlow<List<chatMessages>>(emptyList())
    val messages = _messages.asStateFlow()

    private val _isConnected = MutableStateFlow(false)
    val isConnected = _isConnected.asStateFlow()

    private val _typingUsers = MutableStateFlow<Set<Sender>>(emptySet())
    val typingUsers = _typingUsers.asStateFlow()





    fun connect() {
        val wsUrl = "wss://csi-backend-wvn0.onrender.com/ws/chat/$roomId/?token=$token"
        val request = Request.Builder().url(wsUrl).build()



        webSocket = client.newWebSocket(request, object : WebSocketListener() {

            override fun onOpen(webSocket: WebSocket, response: okhttp3.Response) {
                _isConnected.value = true
                Log.d("WebSocket", "Connected to WebSocket")
            }

            @SuppressLint("SuspiciousIndentation")
            override fun onMessage(webSocket: WebSocket, text: String) {
                Log.d("WebSocket", "Message received: $text")

                try {
                    val messageObject = gson.fromJson(text, chatMessages::class.java)
                    if(messageObject.action=="typing") {

                        if (messageObject.is_typing == true) {
                            _typingUsers.value = _typingUsers.value + messageObject.sender


                        } else {
                            _typingUsers.value = _typingUsers.value - messageObject.sender
                        }
                    }

                    else{


                        Log.i(
                            "SEND BY ${messageObject.sender.name}",
                            "sendMessage: ${messageObject.message}"
                        )

                        if(!messageObject.is_self)
                            playSound(context,R.raw.message_reccieved_sound2)
                        else
                            playSound(context,R.raw.message_sending_sound)
                        _messages.value = _messages.value + messageObject

                    }



                    Log.i("typingUsersList", "onMessage: ${typingUsers}")
                }
                catch (e: Exception) {
                    Log.e("WebSocket", "Error parsing message JSON: ${e.message}")
                }
            }

            override fun onFailure(webSocket: WebSocket, t: Throwable, response: okhttp3.Response?) {
                _isConnected.value = false
                Log.e("WebSocket not connected", "Error: ${t.message}")
            }

            override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
                _isConnected.value = false
                Log.d("WebSocket", "Closing WebSocket: $code / $reason")
                webSocket.close(1000, null)
            }

            override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
                _isConnected.value = false
                Log.d("WebSocket", "WebSocket closed: $code / $reason")
            }
        })
    }

    fun sendMessage(message: String,mentionUserList:List<Int>) {
        if (_isConnected.value) {
            val jsonMessage = """
                {
                    "action": "send_message",
                    "message": "$message",
                    "message_type": "text",
                    "mentions": ${mentionUserList.joinToString(prefix = "[", postfix = "]")}
                    
                }
            """.trimIndent()

            Log.i("MENTIONS ID", "Mentions : ${mentionUserList.joinToString(prefix = "[", postfix = "]")}")

            webSocket?.send(jsonMessage)
            Log.i("SEND BY YOU IN ID $roomId ", "sendMessage: $message $token")
        } else {
            Log.e("WebSocket", "Cannot send message. WebSocket is not connected.")
        }
    }


    fun react(reaction: String, message_id: Int){
        if (_isConnected.value) {
            val jsonMessage = """
                {
                    "action" : "react_message",
                    "message_id" : $message_id,
                    "reaction" : "$reaction"
                }
            """.trimIndent()

            webSocket?.send(jsonMessage)
            Log.i("SEND BY YOU IN ID $roomId ", "reactMessage: $reaction id: $message_id")
        } else {
            Log.e("WebSocket", "Cannot send message. WebSocket is not connected.")
        }
    }


    private var lastSignal="stop_typing"

    fun sendTypingEvent(typing:String) {

        if(typing==lastSignal) return


        if (_isConnected.value) {
            val jsonMessage = """
            {
                "action": "${typing}"
            }
        """.trimIndent()

            webSocket?.send(jsonMessage)
            Log.i("WebSocket", "Sent ${typing} event")
            lastSignal=typing



        } else {
            Log.e("WebSocket", "Cannot send typing event. WebSocket is not connected.")
        }
    }

    fun disconnect() {
        webSocket?.close(1000, "User left room")
    }

    fun isSocketConnected(): Boolean {
        return _isConnected.value
    }



}