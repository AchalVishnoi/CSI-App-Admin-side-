package com.example.csiappcompose.viewModels

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.csiappcompose.Backend.RetrofitInstance
import com.example.csiappcompose.DataStoreManager
import com.example.csiappcompose.dataModelsResponseTask.Completed
import com.example.csiappcompose.dataModelsResponseTask.Current
import com.example.csiappcompose.dataModelsResponseTask.Pending
import com.example.csiappcompose.dataModelsResponseTask.TaskData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class TaskPageViewModel(private val context: Context) : ViewModel() {

    private val _token = MutableStateFlow<String?>(null)
    val token: StateFlow<String?> = _token

    private val _taskPage = MutableLiveData<NetWorkResponse<TaskData>>()
    val taskPage: LiveData<NetWorkResponse<TaskData>> = _taskPage

    private val _current = MutableLiveData<NetWorkResponse<List<Current>>>()
    val create: LiveData<NetWorkResponse<List<Current>>> = _current

    private val _pending = MutableLiveData<NetWorkResponse<List<Pending>>>()
    val pending: LiveData<NetWorkResponse<List<Pending>>> = _pending

    private val _completed = MutableLiveData<NetWorkResponse<List<Completed>>>()
    val completed: MutableLiveData<NetWorkResponse<List<Completed>>> = _completed

    private val _attachment = MutableLiveData<NetWorkResponse<List<Completed>>>()
    val attachment: LiveData<NetWorkResponse<List<Completed>>> = _attachment

    private val _description = MutableLiveData<NetWorkResponse<List<Completed>>>()
    val description: LiveData<NetWorkResponse<List<Completed>>> = _description

    private val _end_date = MutableLiveData<NetWorkResponse<List<Completed>>>()
    val end_date: LiveData<NetWorkResponse<List<Completed>>> = _end_date

    private var _start_date = MutableLiveData<NetWorkResponse<List<Completed>>>()
    val start_date: LiveData<NetWorkResponse<List<Completed>>> = _start_date

    private val _status = MutableLiveData<NetWorkResponse<List<Completed>>>()
    val status: LiveData<NetWorkResponse<List<Completed>>> = _status

    private val _title = MutableLiveData<NetWorkResponse<List<Completed>>>()
    val title: LiveData<NetWorkResponse<List<Completed>>> = _title

    private val _groups = MutableLiveData<NetWorkResponse<List<Completed>>>()
    val groups: LiveData<NetWorkResponse<List<Completed>>> = _groups

    private val _info_url = MutableLiveData<NetWorkResponse<List<Completed>>>()
    val info_url: LiveData<NetWorkResponse<List<Completed>>> = _info_url

    private val _current_progress = MutableLiveData<NetWorkResponse<List<Completed>>>()
    val current_progress: LiveData<NetWorkResponse<List<Completed>>> = _current_progress

    private val _id = MutableLiveData<NetWorkResponse<List<Completed>>>()
    val id: LiveData<NetWorkResponse<List<Completed>>> = _id

    fun fetchToken() {
        _taskPage.value = NetWorkResponse.Loading
        _current.value = NetWorkResponse.Loading
        _pending.value = NetWorkResponse.Loading
        _completed.value = NetWorkResponse.Loading
        _attachment.value = NetWorkResponse.Loading
        _id.value = NetWorkResponse.Loading
        _description.value = NetWorkResponse.Loading
        _end_date.value = NetWorkResponse.Loading
        _status.value = NetWorkResponse.Loading
        _title.value = NetWorkResponse.Loading
        _groups.value = NetWorkResponse.Loading
        _start_date.value = NetWorkResponse.Loading
        _info_url.value = NetWorkResponse.Loading

        viewModelScope.launch {
            DataStoreManager.getToken(context).collect { savedToken ->
                _token.value = savedToken
                if (!savedToken.isNullOrEmpty()) {
                    getTask(savedToken)
                } else {
                    _taskPage.value = NetWorkResponse.Error("failed to load data")
                    _current.value = NetWorkResponse.Error("failed to load data")
                    _pending.value = NetWorkResponse.Error("failed to load data")
                    _completed.value = NetWorkResponse.Error("failed to load data")
                    _id.value = NetWorkResponse.Error("failed to load data")
                    _attachment.value = NetWorkResponse.Error("failed to load data")
                    _description.value = NetWorkResponse.Error("failed to load data")
                    _end_date.value = NetWorkResponse.Error("failed to load data")
                    _status.value = NetWorkResponse.Error("failed to load data")
                    _title.value = NetWorkResponse.Error("failed to load data")
                    _groups.value = NetWorkResponse.Error("failed to load data")
                    _start_date.value = NetWorkResponse.Error("failed to load data")
                    _info_url.value = NetWorkResponse.Error("failed to load data")
                    _current_progress.value = NetWorkResponse.Error("failed to load data")

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
                        _current.value = NetWorkResponse.Success(it.current)
                        _pending.value = NetWorkResponse.Success(it.pending)
                        _completed.value = NetWorkResponse.Success(it.completed)
                        _attachment.value = NetWorkResponse.Success(it.completed)
                        _description.value = NetWorkResponse.Success(it.completed)
                        _end_date.value = NetWorkResponse.Success(it.completed)
                        _start_date.value = NetWorkResponse.Success(it.completed)
                        _status.value = NetWorkResponse.Success(it.completed)
                        _title.value = NetWorkResponse.Success(it.completed)
                        _groups.value = NetWorkResponse.Success(it.completed)
                        _info_url.value = NetWorkResponse.Success(it.completed)
                        _current_progress.value = NetWorkResponse.Success(it.completed)
                        _id.value = NetWorkResponse.Success(it.completed)
                    }
                } else {
                    _taskPage.value = NetWorkResponse.Error("failed to load data")
                    _id.value= NetWorkResponse.Error("failed to load data")
                    _current.value = NetWorkResponse.Error("failed to load data")
                    _pending.value = NetWorkResponse.Error("failed to load data")
                    _completed.value = NetWorkResponse.Error("failed to load data")
                    _attachment.value = NetWorkResponse.Error("failed to load data")
                    _description.value = NetWorkResponse.Error("failed to load data")
                    _end_date.value = NetWorkResponse.Error("failed to load data")
                    _status.value = NetWorkResponse.Error("failed to load data")
                    _title.value = NetWorkResponse.Error("failed to load data")
                    _groups.value = NetWorkResponse.Error("failed to load data")
                    _start_date.value = NetWorkResponse.Error("failed to load data")

                }
            } catch (e: Exception) {
                _taskPage.value = NetWorkResponse.Error("failed to load data")
                _id.value= NetWorkResponse.Error("failed to load data")
                _current.value = NetWorkResponse.Error("failed to load data")
                _pending.value = NetWorkResponse.Error("failed to load data")
                _completed.value = NetWorkResponse.Error("failed to load data")
                _attachment.value = NetWorkResponse.Error("failed to load data")
                _description.value = NetWorkResponse.Error("failed to load data")
                _end_date.value = NetWorkResponse.Error("failed to load data")
                _status.value = NetWorkResponse.Error("failed to load data")
                _title.value = NetWorkResponse.Error("failed to load data")
                _groups.value = NetWorkResponse.Error("failed to load data")

            }
        }
    }
}
