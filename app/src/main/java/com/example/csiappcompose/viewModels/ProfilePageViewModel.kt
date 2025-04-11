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

    private val _photo = MutableLiveData<NetWorkResponse<profileData>>()
    val photo: LiveData<NetWorkResponse<profileData>> = _photo

    private val _name = MutableLiveData<NetWorkResponse<profileData>>()
    val name: LiveData<NetWorkResponse<profileData>> = _name

    private val _branch = MutableLiveData<NetWorkResponse<profileData>>()
    val branch: LiveData<NetWorkResponse<profileData>> = _branch

    private val _year = MutableLiveData<NetWorkResponse<profileData>>()
    val year: LiveData<NetWorkResponse<profileData>> = _year

    private val _bio = MutableLiveData<NetWorkResponse<profileData>>()
    val bio: LiveData<NetWorkResponse<profileData>> = _bio

    private val _achievements = MutableLiveData<NetWorkResponse<profileData>>()
    val achievements: LiveData<NetWorkResponse<profileData>> = _achievements

    private val _github = MutableLiveData<NetWorkResponse<profileData>>()
    val github: LiveData<NetWorkResponse<profileData>> = _github

    private val _linkedin = MutableLiveData<NetWorkResponse<profileData>>()
    val linkedin: LiveData<NetWorkResponse<profileData>> = _linkedin

    private val _domain = MutableLiveData<NetWorkResponse<profileData>>()
    val domain: LiveData<NetWorkResponse<profileData>> = _domain

    private val _dob = MutableLiveData<NetWorkResponse<profileData>>()
    val dob: LiveData<NetWorkResponse<profileData>> = _dob




    fun fetchToken(){

        _profilePage.value= NetWorkResponse.Loading
        _dob.value= NetWorkResponse.Loading
        _domain.value= NetWorkResponse.Loading
        _github.value= NetWorkResponse.Loading
        _linkedin.value= NetWorkResponse.Loading
        _achievements.value= NetWorkResponse.Loading
        _bio.value= NetWorkResponse.Loading
        _year.value= NetWorkResponse.Loading
        _branch.value= NetWorkResponse.Loading
        _photo.value= NetWorkResponse.Loading
        _name.value= NetWorkResponse.Loading

        viewModelScope.launch{

            DataStoreManager.getToken(context).collect{ savedToken->
                _token.value=savedToken
                if (!savedToken.isNullOrEmpty()) {
                    getProfile(savedToken)

                }
                else{
                    _profilePage.value= NetWorkResponse.Error("failed to load data")
                    _dob.value= NetWorkResponse.Error("failed to load data")
                    _domain.value= NetWorkResponse.Error("failed to load data")
                    _github.value= NetWorkResponse.Error("failed to load data")
                    _linkedin.value= NetWorkResponse.Error("failed to load data")
                    _achievements.value= NetWorkResponse.Error("failed to load data")
                    _bio.value= NetWorkResponse.Error("failed to load data")
                    _year.value= NetWorkResponse.Error("failed to load data")
                    _branch.value= NetWorkResponse.Error("failed to load data")
                    _photo.value= NetWorkResponse.Error("failed to load data")
                    _name.value= NetWorkResponse.Error("failed to load data")
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
                        _dob.value = NetWorkResponse.Success(it)
                        _domain.value = NetWorkResponse.Success(it)
                        _github.value = NetWorkResponse.Success(it)
                        _linkedin.value = NetWorkResponse.Success(it)
                        _achievements.value = NetWorkResponse.Success(it)
                        _bio.value = NetWorkResponse.Success(it)
                        _year.value = NetWorkResponse.Success(it)
                        _branch.value = NetWorkResponse.Success(it)
                        _photo.value = NetWorkResponse.Success(it)
                        _name.value = NetWorkResponse.Success(it)
                    }
                } else {
                    _profilePage.value = NetWorkResponse.Error("failed to load data")
                    _dob.value = NetWorkResponse.Error("failed to load data")
                    _domain.value = NetWorkResponse.Error("failed to load data")
                    _github.value = NetWorkResponse.Error("failed to load data")
                    _linkedin.value = NetWorkResponse.Error("failed to load data")
                    _achievements.value = NetWorkResponse.Error("failed to load data")
                    _bio.value = NetWorkResponse.Error("failed to load data")
                    _year.value = NetWorkResponse.Error("failed to load data")
                    _branch.value = NetWorkResponse.Error("failed to load data")
                    _photo.value = NetWorkResponse.Error("failed to load data")
                    _name.value = NetWorkResponse.Error("failed to load data")

                }
            } catch (e: Exception) {
                _profilePage.value = NetWorkResponse.Error("failed to load data")
                _dob.value = NetWorkResponse.Error("failed to load data")
                _domain.value = NetWorkResponse.Error("failed to load data")
                _github.value = NetWorkResponse.Error("failed to load data")
                _linkedin.value = NetWorkResponse.Error("failed to load data")
                _achievements.value = NetWorkResponse.Error("failed to load data")
                _bio.value = NetWorkResponse.Error("failed to load data")
                _year.value = NetWorkResponse.Error("failed to load data")
                _branch.value = NetWorkResponse.Error("failed to load data")
                _photo.value = NetWorkResponse.Error("failed to load data")
                _name.value = NetWorkResponse.Error("failed to load data")
            }

        }

    }
}