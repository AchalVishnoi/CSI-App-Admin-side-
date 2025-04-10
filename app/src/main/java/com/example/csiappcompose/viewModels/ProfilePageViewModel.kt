package com.example.csiappcompose.viewModels

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.csiappcompose.Backend.RetrofitInstance
import com.example.csiappcompose.DataStoreManager
import com.example.csiappcompose.dataModelsResponse.profileData
import com.example.csiappcompose.dataModelsResponseTask.TaskData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ProfilePageViewModel(private val context: Context) : ViewModel() {

    private val _token = MutableStateFlow<String?>(null)
    val token: StateFlow<String?> = _token

    private val _profilePage = MutableLiveData<NetWorkResponse<profileData>>()
    val profilePage: LiveData<NetWorkResponse<profileData>> = _profilePage


    fun fetchToken(){

        _profilePage.value= NetWorkResponse.Loading

        viewModelScope.launch{

            DataStoreManager.getToken(context).collect{ savedToken->
                _token.value=savedToken
                if (!savedToken.isNullOrEmpty()) {
                    getProfile(savedToken)

                }
                else{
                    _profilePage.value= NetWorkResponse.Error("failed to load data")
                }
            }

        }
    }

    fun getProfile(token: String) {


        viewModelScope.launch {
            val response = RetrofitInstance.apiService.profile("Token $token")

            Log.d("API_REQUEST", "Request: Token $token")


            Log.d("API_RESPONSE", "Code: ${response.code()} Body: ${response.body()}")

            try {
                if (response.isSuccessful) {
                    response.body()?.let {
                        _profilePage.value = NetWorkResponse.Success(it)
                    }
                } else {
                    _profilePage.value = NetWorkResponse.Error("failed to load data")
                }
            } catch (e: Exception) {
                _profilePage.value = NetWorkResponse.Error("failed to load data")
            }

        }

    }
}