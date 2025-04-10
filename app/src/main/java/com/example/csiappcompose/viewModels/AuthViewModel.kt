package com.example.csiappcompose.viewModels

import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.example.csiappcompose.Backend.RetrofitInstance.apiService
import com.example.csiappcompose.DataStoreManager
import com.example.csiappcompose.dataModelsRequests.ForgotPasswordRequest
import com.example.csiappcompose.dataModelsRequests.LoginRequest
import com.example.csiappcompose.dataModelsRequests.resetPasswordRequest
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AuthViewModel(application: Application) : AndroidViewModel(application) {


    private val context = application.applicationContext

    private val _token = MutableStateFlow<String?>(null)
    val token: StateFlow<String?> = _token

    private val _loginResult = MutableStateFlow<Result<String>?>(null)
    val loginResult: StateFlow<Result<String>?> = _loginResult

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _requestForOtpResult = MutableStateFlow<Result<String>?>(null)
    val requestForOtpResult: StateFlow<Result<String>?> = _requestForOtpResult

    private val _resetPasswordResult = MutableStateFlow<Result<String>?>(null)
    val resetPasswordResult: StateFlow<Result<String>?> = _resetPasswordResult

    private val _detailsComplete = MutableStateFlow<Boolean?>(null)
    val detailsComplete: StateFlow<Boolean?> = _detailsComplete

    init {
        viewModelScope.launch {

            DataStoreManager.getToken(context).collect { savedToken ->
                Log.i("DATA MANAGER", "saved token= $savedToken")
                _token.value = savedToken
            }



        }
    }


    init {
        viewModelScope.launch{
            DataStoreManager.getCompleteDetailsStatus(context).collect { status ->
                Log.i("DATA MANAGER", "complete details status= $status")
                _detailsComplete.value = status
            }
        }

    }

    fun loginUser(email: String, password: String) {
        viewModelScope.launch {
            _isLoading.value = true

            try {
                val request = LoginRequest(email, password)
                Log.d("API_REQUEST", "Request: $request")

                val response = apiService.login(request)
                Log.d("API_RESPONSE", "Code: ${response.code()} Body: ${response.body()?.token}")

                if (response.isSuccessful) {
                    val token = response.body()?.token
                    if (!token.isNullOrEmpty()) {
                        saveToken(token)

                        val isComplete = response.body()?.is_completed == true
                        _loginResult.value = Result.success(
                            if (isComplete) "complete details" else "not complete details"
                        )


                        DataStoreManager.saveCompleteDetailsStatus(context, isComplete)
                        Log.i("DATA MANAGER", "saving status= $isComplete")
                    } else {
                        _loginResult.value = Result.failure(Exception("Invalid Token"))
                    }
                } else {
                    _loginResult.value = Result.failure(
                        Exception(response.errorBody()?.string() ?: "Invalid Credentials")
                    )
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
            DataStoreManager.clearToken(context)
            _token.value = null
            _loginResult.value = null
            navController.navigate("login") {
                popUpTo("home") { inclusive = true }
            }
        }
    }









    fun requestForOtp(email: String) {
        viewModelScope.launch {
            _isLoading.value = true

            try {
                val request = ForgotPasswordRequest(email)
                Log.d("API_REQUEST", "Request: $request")

                val response = apiService.forgetPassword(request)

                if (response.isSuccessful) {


                    if (response.body()?.message!=null) {

                        _requestForOtpResult.value= Result.success("otp sent to your email")
                        Toast.makeText(context,"Check your Email!!",Toast.LENGTH_SHORT).show()

                    } else {
                        _requestForOtpResult.value = Result.failure(Exception("Invalid email"))
                        Toast.makeText(context,"Invalid email!!",Toast.LENGTH_SHORT).show()
                    }
                } else {
                    _requestForOtpResult.value = Result.failure(Exception("Invalid Credentials"))
                    Toast.makeText(context,"Invalid email!!",Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Log.e("API_ERROR", "Exception: ${e.message}")
                _requestForOtpResult.value = Result.failure(e)
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun resetPassword(email: String,otp:String,password: String) {
        viewModelScope.launch {
            _isLoading.value = true

            try {
                val request = resetPasswordRequest(email,otp,password)
                Log.d("API_REQUEST", "Request: $request")

                val response = apiService.resetPassword(request)

                if (response.isSuccessful) {


                    if (response.body()?.message!=null) {

                        _resetPasswordResult.value= Result.success("Password changed successfully!!")
                        Toast.makeText(context,"Password changed successfully!!",Toast.LENGTH_SHORT).show()

                    } else {
                        _resetPasswordResult.value = Result.failure(Exception("Invalid otp"))
                        Toast.makeText(context,"Invalid otp!",Toast.LENGTH_SHORT).show()
                    }
                } else {
                    _resetPasswordResult.value = Result.failure(Exception("Invalid Credentials"))
                    Toast.makeText(context,"Invalid Credentials",Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Log.e("API_ERROR", "Exception: ${e.message}")
                _resetPasswordResult.value = Result.failure(e)
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun clearOtpResult() {
        _requestForOtpResult.value = null
    }

    fun clearResetResult() {
        _resetPasswordResult.value = null
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

    fun saveCompleteDetailsStatus(detailCompletedStatus: Boolean) {
        viewModelScope.launch {
            DataStoreManager.saveCompleteDetailsStatus(context, detailCompletedStatus)
        }
    }






}
