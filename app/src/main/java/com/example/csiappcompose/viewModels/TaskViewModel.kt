package com.example.csiappcompose.viewModels


import android.content.Context
import androidx.lifecycle.viewModelScope
import com.example.csiappcompose.DataStoreManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class TaskViewModel(private val context: Context) {
    private val _token= MutableStateFlow<String?>(null)
    val token : StateFlow<String?> = _token



    fun fetchToken(){

     //   _groupChatResult.value= NetWorkResponse.Loading

//        viewModelScope.launch{
//
//            DataStoreManager.getToken(context).collect{ savedToken->
//                _token.value=savedToken
//                if (!savedToken.isNullOrEmpty()) {
//                    getChatList(savedToken)
//                }
//                else{
//                    _groupChatResult.value= NetWorkResponse.Error("failed to load data")
//                }
//            }
//
//        }
//        viewModelScope.launch{
//
//            DataStoreManager.getToken(context).collect{ savedToken->
//                _token.value=savedToken
//                if (!savedToken.isNullOrEmpty()) {
//                    getChatList(savedToken)
//                }
//                else{
//                    _groupChatResult.value= NetWorkResponse.Error("failed to load data")
//                }
//            }

        }
    }