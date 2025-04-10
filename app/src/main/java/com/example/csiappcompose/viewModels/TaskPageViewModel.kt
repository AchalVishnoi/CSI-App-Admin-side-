package com.example.csiappcompose.viewModels

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.csiappcompose.Backend.RetrofitInstance
import com.example.csiappcompose.DataStoreManager
import com.example.csiappcompose.dataModelsResponseTask.TaskData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class TaskPageViewModel(private val context: Context) : ViewModel() {

    private val _token = MutableStateFlow<String?>(null)
    val token: StateFlow<String?> = _token

//    private val _profilePage = MutableLiveData<NetWorkResponse<profileData>>()
//    val profilePage: LiveData<NetWorkResponse<profileData>> = _profilePage

    private val _taskPage = MutableLiveData<NetWorkResponse<TaskData>>()
    val taskPage: LiveData<NetWorkResponse<TaskData>> = _taskPage


    fun fetchToken(){

        _taskPage.value= NetWorkResponse.Loading

        viewModelScope.launch{

            DataStoreManager.getToken(context).collect{ savedToken->
                _token.value=savedToken
                if (!savedToken.isNullOrEmpty()) {
                    getTask(savedToken)

                }
                else{
                    _taskPage.value= NetWorkResponse.Error("failed to load data")
                }
            }

        }
    }

    fun getTask(token: String) {


        viewModelScope.launch {
            val response = RetrofitInstance.apiService.task("Token $token")

            Log.d("API_REQUEST", "Request: Token $token")


            Log.d("API_RESPONSE", "Code: ${response.code()} Body: ${response.body()}")

            try {
                if (response.isSuccessful) {
                    response.body()?.let {
                        _taskPage.value = NetWorkResponse.Success(it)
                    }
                } else {
                    _taskPage.value = NetWorkResponse.Error("failed to load data")
                }
            } catch (e: Exception) {
                _taskPage.value = NetWorkResponse.Error("failed to load data")
            }

        }

    }
}