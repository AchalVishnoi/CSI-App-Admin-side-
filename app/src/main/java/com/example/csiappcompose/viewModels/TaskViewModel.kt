package com.example.csiappcompose.viewModels


import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.csiappcompose.Backend.RetrofitInstance
import com.example.csiappcompose.DataStoreManager
import com.example.csiappcompose.dataModelsResponse.EventItem
import com.example.csiappcompose.dataModelsResponse.HomePageStats
import com.example.csiappcompose.dataModelsResponse.announcmentDisplay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.IOException
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

class TaskViewModel(private val context: Context) : ViewModel() {


    private val _token = MutableStateFlow<String?>(null)
    val token: StateFlow<String?> = _token


    private val _taskPage = MutableLiveData<NetWorkResponse<Any>>()
    val taskPage: LiveData<NetWorkResponse<Any>> = _taskPage


    fun fetchToken() {
        _taskPage.value = NetWorkResponse.Loading

        viewModelScope.launch {

            DataStoreManager.getToken(context).collect { savedToken ->
                _token.value = savedToken
                if (!savedToken.isNullOrEmpty()) {
                    getTask(savedToken)

                } else {
                    _taskPage.value = NetWorkResponse.Error("failed to load data")
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

class TaskViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TaskViewModel::class.java)) {
            return TaskViewModel(context) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

