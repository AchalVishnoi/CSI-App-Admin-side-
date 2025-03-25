package com.example.csiappcompose.viewModels

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class ChatViewModelFactory(private val context: Context): ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T{
        if(modelClass.isAssignableFrom(ChatViewModel::class.java))
            return ChatViewModel(context) as T


        throw IllegalArgumentException("Unknown ViewModel class")
    }

}






class ChatRoomViewModelFactory(private val roomId: Int, private val token: String,private val context: Context) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ChatRoomViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ChatRoomViewModel(roomId, token,context) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
