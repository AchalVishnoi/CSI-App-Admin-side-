package com.example.csiappcompose

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import com.example.csiappcompose.dataModelsResponse.chatMessages
import com.google.gson.Gson
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.WebSocket
import okhttp3.WebSocketListener


class WebSocketManager(private val roomId: Int, private val token: String) {

    private val client = OkHttpClient()
    private var webSocket: WebSocket? = null
    private val gson = Gson()

    private val _messages = MutableStateFlow<List<chatMessages>>(emptyList())
    val messages = _messages.asStateFlow() // Expose as StateFlow

    private val _isConnected = MutableStateFlow(false) // Connection status
    val isConnected = _isConnected.asStateFlow() // Expose as StateFlow

    fun connect() {
        val wsUrl = "wss://csi-backend-wvn0.onrender.com/ws/chat/$roomId/?token=$token"
        val request = Request.Builder().url(wsUrl).build()

        webSocket = client.newWebSocket(request, object : WebSocketListener() {

            override fun onOpen(webSocket: WebSocket, response: okhttp3.Response) {
                _isConnected.value = true
                Log.d("WebSocket", "Connected to WebSocket")
            }

            override fun onMessage(webSocket: WebSocket, text: String) {
                Log.d("WebSocket", "Message received: $text")

                try {
                    val messageObject = gson.fromJson(text, chatMessages::class.java)

                    Log.i("SEND BY ${messageObject.sender.name}", "sendMessage: ${messageObject.message}")

                    _messages.value = _messages.value + messageObject
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

    fun sendMessage(message: String) {
        if (_isConnected.value) {
            val jsonMessage = """
                {
                    "action": "send_message",
                    "message": "$message",
                    "message_type": "text"
                }
            """.trimIndent()

            webSocket?.send(jsonMessage)
            Log.i("SEND BY YOU IN ID $roomId ", "sendMessage: $message $token")
        } else {
            Log.e("WebSocket", "Cannot send message. WebSocket is not connected.")
        }
    }

    fun disconnect() {
        webSocket?.close(1000, "User left room")
    }

    fun isSocketConnected(): Boolean {
        return _isConnected.value
    }
}
