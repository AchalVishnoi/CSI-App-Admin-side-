package com.example.csiappcompose.viewModels

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.csiappcompose.Backend.RetrofitInstance
import com.example.csiappcompose.DataStoreManager
import com.example.csiappcompose.dataModelsResponseTask.CreateTask
import com.example.csiappcompose.dataModelsResponseTask.GroupX
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.IOException
import com.google.gson.Gson


class CreateTaskViewModel(private val context: Context) : ViewModel() {

    private val _token = MutableStateFlow<String?>(null)
    val token: StateFlow<String?> = _token

    private val _createTask = MutableLiveData<NetWorkResponse<List<CreateTask>>>()
    var createTask: LiveData<NetWorkResponse<List<CreateTask>>> = _createTask

    var title by mutableStateOf("")
    var description by mutableStateOf("")
    var startDate by mutableStateOf("")
    var endDate by mutableStateOf("")
    var status by mutableStateOf("")
    var currentProgress by mutableStateOf("")
    var attachmentUrl by mutableStateOf("")
    var infoUrl by mutableStateOf("")
    var groups = mutableStateListOf<GroupX>()

    fun fetchToken() {
        _createTask.value = NetWorkResponse.Loading
        viewModelScope.launch {
            DataStoreManager.getToken(context).collect { savedToken ->
                _token.value = savedToken
                if (savedToken.isNullOrEmpty()) {
                    _createTask.value = NetWorkResponse.Error("failed to load data")
                }
            }
        }
    }

    val isLoading = mutableStateOf(false)
    val successMessage = mutableStateOf<Boolean?>(null)
    val errorMessage = mutableStateOf<String?>(null)

    @SuppressLint("SuspiciousIndentation")
    fun submitTask() {
        viewModelScope.launch {
            try {
                val savedToken = DataStoreManager.getToken(context).firstOrNull()
                if (!savedToken.isNullOrEmpty()) {
                    isLoading.value = true
                    val formattedStartDate = formatToISO8601(startDate)
                    val formattedEndDate = formatToISO8601(endDate)

                    val response = RetrofitInstance.apiService.createTask(
                        token = "Token $savedToken",
                        title = title.toPlainRequestBody(),
                        description = description.toPlainRequestBody(),
                        registrationStartDate = formattedStartDate.toPlainRequestBody(),
                        registrationEndDate = formattedEndDate.toPlainRequestBody(),
                        status = status.toPlainRequestBody(),
                        currentProgress = currentProgress.toPlainRequestBody(),
                        attachment = attachmentUrl.toPlainRequestBody(),
                        infoUrl = infoUrl.toPlainRequestBody(),
                        groups = groups.toJsonRequestBody()
                    )

                    if (response.isSuccessful) {
                        successMessage.value = true
                        Toast.makeText(context, "Task created successfully!", Toast.LENGTH_LONG).show()
                        Log.i("TASK", "submitTask : Success")
                    } else {
                        val errorBody = response.errorBody()?.string()
                        errorMessage.value = errorBody ?: "Failed to create task"
                        Log.e("TASK", "submitTask: $errorBody")
                        Toast.makeText(context, errorMessage.value, Toast.LENGTH_LONG).show()
                    }
                } else {
                    errorMessage.value = "Token not found. Please log in again."
                    Toast.makeText(context, errorMessage.value, Toast.LENGTH_LONG).show()
                }
            } catch (e: IOException) {
                errorMessage.value = "Network error: Please check your connection"
                Log.e("ERROR", "IOException: ${e.message}")
            } catch (e: Exception) {
                errorMessage.value = e.message ?: "An unexpected error occurred"
                Log.e("ERROR", "Exception: ${e.message}")
            } finally {
                isLoading.value = false
            }
        }
    }

    fun updateGroup(index: Int, newGroup: GroupX) {
        groups[index] = newGroup
    }
}

fun Any.toJsonRequestBody(): RequestBody {
    val gson = Gson()
    val json = gson.toJson(this)
    return json.toRequestBody("application/json".toMediaType())
}
