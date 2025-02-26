package com.example.csiappcompose.viewModels

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.csiappcompose.BuildConfig
import com.google.ai.client.generativeai.GenerativeModel
import kotlinx.coroutines.launch
import androidx.compose.runtime.*
import com.google.ai.client.generativeai.type.content

class AiChatViewModel : ViewModel (){

    var messageList =mutableStateListOf<AiMessageModel>()

    var generativeModel: GenerativeModel= GenerativeModel(
        modelName = "gemini-1.5-flash",

       apiKey= BuildConfig.apiKey
    )


    @SuppressLint("NewApi")
    fun sendMessage(question :String){
       viewModelScope.launch(){
            val chat=generativeModel.startChat(
                history = messageList.map {
                    content(it.role){text(it.message)}
                }.toList()
            )
           messageList.add(AiMessageModel(question,"user"))
           messageList.add(AiMessageModel("Typing....","model"))
           var response=chat.sendMessage(question)
           messageList.removeLast()
           messageList.add(AiMessageModel(response.text.toString(),"model"))
           Log.i("response from ai", response.text.toString())
       }
    }



}