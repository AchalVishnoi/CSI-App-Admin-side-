package com.example.csiappcompose

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.example.csiappcompose.Backend.ApiService
import com.example.csiappcompose.Backend.RetrofitInstance
import com.example.csiappcompose.Backend.RetrofitInstance.apiService
import com.example.csiappcompose.dataModelsRequests.LoginRequest
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch

class AuthViewModel(application: Application) : AndroidViewModel(application) {


    private val context = application.applicationContext

    private val _token = MutableStateFlow<String?>(null)
    val token: StateFlow<String?> = _token

    private val _loginResult = MutableStateFlow<Result<String>?>(null)
    val loginResult: StateFlow<Result<String>?> = _loginResult

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading  // ✅ Expose loading state

    init {
        viewModelScope.launch {
            DataStoreManager.getToken(context).collect { savedToken ->
                _token.value = savedToken
            }
        }
    }

    fun loginUser(email: String, password: String) {
        viewModelScope.launch {
            _isLoading.value = true

            try {
                val request = LoginRequest(email, password)
                Log.d("API_REQUEST", "Request: $request")

                val response = RetrofitInstance.apiService.login(request)
                Log.d("API_RESPONSE", "Code: ${response.code()} Body: ${response.errorBody()?.string()}")
                if (response.isSuccessful) {
                    val token = response.body()?.token
                    if (!token.isNullOrEmpty()) {
                        saveToken(token)
                        _loginResult.value = Result.success(token)
                    } else {
                        _loginResult.value = Result.failure(Exception("Invalid Token"))
                    }
                } else {
                    _loginResult.value = Result.failure(Exception(response.errorBody()?.string() ?: "Invalid Credentials"))
                }
            } catch (e: Exception) {
                Log.e("API_ERROR", "Exception: ${e.message}")
                _loginResult.value = Result.failure(e)
            } finally {
                _isLoading.value = false
            }
        }
    }



   // logout

    private val _logoutState = MutableStateFlow(false)
    val logoutState: StateFlow<Boolean> = _logoutState


    fun logoutUser(navController: NavController) {
        viewModelScope.launch {
            DataStoreManager.clearToken(context) // Clear stored token
            _token.value = null
            _loginResult.value = null
            navController.navigate("login") {
                popUpTo("home") { inclusive = true } // Clear backstack
            }
        }
    }


    fun saveToken(token: String) {
        viewModelScope.launch {
            DataStoreManager.saveToken(context, token)
        }
    }

    fun clearToken() {
        viewModelScope.launch {
            DataStoreManager.clearToken(context)
        }
    }
}
