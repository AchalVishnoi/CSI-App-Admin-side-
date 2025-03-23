
package com.example.csiappcompose.viewModels

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.csiappcompose.Backend.RetrofitInstance
import com.example.csiappcompose.Backend.RetrofitInstance.apiService
import com.example.csiappcompose.DataStoreManager
import com.example.csiappcompose.dataModelsResponse.GroupListItem

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ChatViewModel(private val context: Context) : ViewModel() {


    private val _token= MutableStateFlow<String?>(null)
    val token : StateFlow<String?> = _token

//    private val _groupList= MutableStateFlow<List<GroupListItem>>(emptyList())
//    val groupList : StateFlow<List<GroupListItem>> = _groupList

    private val _groupChatResult= MutableLiveData< NetWorkResponse<List<GroupListItem>>>()
    val groupChatResult: LiveData<NetWorkResponse<List<GroupListItem>>> = _groupChatResult


    fun fetchToken(){

        _groupChatResult.value= NetWorkResponse.Loading

        viewModelScope.launch{

            DataStoreManager.getToken(context).collect{ savedToken->
                _token.value=savedToken
                if (!savedToken.isNullOrEmpty()) {
                    getChatList(savedToken)

                }
                else{
                    _groupChatResult.value= NetWorkResponse.Error("failed to load data")
                }
            }

        }
    }





    fun getChatList( token:String){


        viewModelScope.launch{
            val response = RetrofitInstance.apiService.getJoinedGroups("Token $token")

            Log.d("API_REQUEST", "Request: Token $token")


            Log.d("API_RESPONSE", "Code: ${response.code()} Body: ${response.body()}")

            try{
                if(response.isSuccessful){
                    response.body()?.let {
                        _groupChatResult.value= NetWorkResponse.Success(it)
                    }
                }
                else{
                    _groupChatResult.value= NetWorkResponse.Error("failed to load data")
                }
            }
            catch (e: Exception){
                _groupChatResult.value= NetWorkResponse.Error("failed to load data")
            }

        }

    }








}