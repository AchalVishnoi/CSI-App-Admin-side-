package com.example.csiappcompose.viewModels

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.csiappcompose.Backend.RetrofitInstance
import com.example.csiappcompose.DataStoreManager
import com.example.csiappcompose.dataModelsResponse.EventItem
import com.example.csiappcompose.dataModelsResponse.HomePageStats
import com.example.csiappcompose.dataModelsResponse.announcmentDisplay
import com.example.csiappcompose.dataModelsResponse.profileData
import com.example.csiappcompose.dataModelsResponseTask.TaskData
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

class HomePageViewModel(private val context: Context) : ViewModel() {



    private val _token= MutableStateFlow<String?>(null)
    val token : StateFlow<String?> = _token

    private val _homeStats= MutableLiveData< NetWorkResponse<HomePageStats>>()
    val homeStats: LiveData< NetWorkResponse<HomePageStats>> = _homeStats

    private val _announcements= MutableLiveData< NetWorkResponse<List<announcmentDisplay>>>()
    val announcements: LiveData< NetWorkResponse<List<announcmentDisplay>>> = _announcements

    private val _ongoingEvents= MutableLiveData< NetWorkResponse<List<EventItem>>>()
    val ongoingEvents: LiveData< NetWorkResponse<List<EventItem>>> = _ongoingEvents

    private val _upcomingEvents= MutableLiveData< NetWorkResponse<List<EventItem>>>()
    val upcomingEvents: LiveData< NetWorkResponse<List<EventItem>>> = _upcomingEvents


    fun fetchToken(){

        _homeStats.value= NetWorkResponse.Loading
        _announcements.value= NetWorkResponse.Loading
        _ongoingEvents.value= NetWorkResponse.Loading
        _upcomingEvents.value= NetWorkResponse.Loading

        viewModelScope.launch{

            DataStoreManager.getToken(context).collect{ savedToken->
                _token.value=savedToken
                if (!savedToken.isNullOrEmpty()) {
                    getHomeState(savedToken)
                    getAnnouncements(savedToken)

                }
                else{
                    _homeStats.value= NetWorkResponse.Error("failed to load data")
                    _announcements.value= NetWorkResponse.Error("failed to load data")
                    _ongoingEvents.value= NetWorkResponse.Error("failed to load data")
                    _upcomingEvents.value= NetWorkResponse.Error("failed to load data")
                }
            }

        }
    }




    fun getHomeState( token:String){


        viewModelScope.launch{
            val response = RetrofitInstance.apiService.homepageStats("Token $token")

            Log.d("API_REQUEST", "Request: Token $token")


            Log.d("API_RESPONSE", "Code: ${response.code()} Body: ${response.body()}")

            try{
                if(response.isSuccessful){
                    response.body()?.let {
                        _homeStats.value= NetWorkResponse.Success(it)
                    }
                }
                else{
                    _homeStats.value= NetWorkResponse.Error("failed to load data")
                }
            }
            catch (e: Exception){
                _homeStats.value= NetWorkResponse.Error("failed to load data")
            }

        }

    }

    fun getAnnouncements( token:String){


        viewModelScope.launch{
            val response = RetrofitInstance.apiService.announcements("Token $token")

            Log.d("API_REQUEST", "Request: Token $token")


            Log.d("API_RESPONSE", "Code: ${response.code()} Body: ${response.body()}")

            try{
                if(response.isSuccessful){
                    response.body()?.let {
                        _announcements.value= NetWorkResponse.Success(it)
                    }
                }
                else{
                    _announcements.value= NetWorkResponse.Error("failed to load data")
                }
            }
            catch (e: Exception){
                _announcements.value= NetWorkResponse.Error("failed to load data")
            }

        }

    }



    fun getEvents( token:String){


        viewModelScope.launch{
            val response1 = RetrofitInstance.apiService.ongoingEvent("Token $token")
            val response2 = RetrofitInstance.apiService.upComingEvent("Token $token")

            Log.d("API_REQUEST", "Request: Token $token")




            try{
                if(response1.isSuccessful){
                    response1.body()?.let {
                        _ongoingEvents.value= NetWorkResponse.Success(it)
                    }
                }
                else{
                    _ongoingEvents.value= NetWorkResponse.Error("failed to load data")
                    Toast.makeText(context, "upcoming Events", Toast.LENGTH_LONG).show()
                }
                if(response2.isSuccessful){
                    response2.body()?.let {
                        _upcomingEvents.value= NetWorkResponse.Success(it)
                    }
                }
                else{
                    _upcomingEvents.value= NetWorkResponse.Error("failed to load data")
                    Toast.makeText(context, "upcoming Events", Toast.LENGTH_LONG).show()
                }


            }
            catch (e: Exception){
                _ongoingEvents.value= NetWorkResponse.Error("failed to load data")
                _upcomingEvents.value= NetWorkResponse.Error("failed to load data")
            }

        }

    }








    val isLoading = mutableStateOf(false)
    val successMessage = mutableStateOf<Boolean?>(null)
    val errorMessage = mutableStateOf<String?>(null)

    @SuppressLint("SuspiciousIndentation")
    fun submitEvent(
        title: String,
        description: String,
        guidelines: String,
        venue: String,
        registrationStartDate: String,
        registrationEndDate: String,
        eventDate: String,
        status: String,
        isRegistrationsOpen: Boolean,
        paymentRequired: Boolean,
        amount: Int,
        poster: MultipartBody.Part?,
        gallery: List<MultipartBody.Part>?
    ) {
        viewModelScope.launch {
            try {
                val savedToken = DataStoreManager.getToken(context).firstOrNull()

                if (!savedToken.isNullOrEmpty()) {
                    isLoading.value = true


                    val formattedStartDate = formatToISO8601(registrationStartDate)
                    val formattedEndDate = formatToISO8601(registrationEndDate)
                    val formattedEventDate = formatToISO8601(eventDate)


                    val response = RetrofitInstance.apiService.createEvent(
                        token = "Token $savedToken",
                        title = title.toPlainRequestBody(),
                        description = description.toPlainRequestBody(),
                        guidelines = guidelines.toPlainRequestBody(),
                        venue = venue.toPlainRequestBody(),
                        registrationStartDate = formattedStartDate.toPlainRequestBody(),
                        registrationEndDate = formattedEndDate.toPlainRequestBody(),
                        eventDate = formattedEventDate.toPlainRequestBody(),
                        status = status.toPlainRequestBody(),
                        isRegistrationsOpen = isRegistrationsOpen.toPlainRequestBody(),
                        paymentRequired = paymentRequired.toPlainRequestBody(),
                        amount = amount.toPlainRequestBody(),
                        poster = poster,
                        gallery = gallery
                    )

                    if (response.isSuccessful) {
                        successMessage.value = true
                        Toast.makeText(context,"Event created successfully!".toString(), Toast.LENGTH_LONG).show()

                        Log.i("EVENT", "submitEvent : Event created successfully")
                    } else {
                        val errorBody = response.errorBody()?.string()
                        errorMessage.value = errorBody ?: "Failed to create event"
                        Log.e("EVENT", "submitEvent: $errorBody")
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


    val isProfileLoading = mutableStateOf(false)
    val profileSuccessMessage = mutableStateOf<String?>(null)
    val profileErrorMessage = mutableStateOf<String?>(null)

    fun submitProfileDetails(
        branch: String,
        domain: Int,
        dob: String,
        linkedinUrl: String,
        bio: String,
        githubUrl: String,
        achievements: String? = null,
        hosteller: Boolean,
        photo: MultipartBody.Part?,
        onSuccess: () -> Unit
    ) {
        viewModelScope.launch {
            try {
                val savedToken = DataStoreManager.getToken(context).firstOrNull()

                if (!savedToken.isNullOrEmpty()) {
                    isProfileLoading.value = true


                    val formatterInput = DateTimeFormatter.ofPattern("d/M/yyyy")
                    val parsedDate = LocalDate.parse(dob, formatterInput)
                    val formattedDob = parsedDate.format(DateTimeFormatter.ISO_LOCAL_DATE)

                    val response = RetrofitInstance.apiService.submitProfile(
                        token = "Token $savedToken",
                        photo = photo!!,
                        branch = branch.toPlainRequestBody(),
                        domain = domain.toPlainRequestBody(),
                        dob = formattedDob.toPlainRequestBody(),
                        linkedinUrl = linkedinUrl.toPlainRequestBody(),
                        bio = bio.toPlainRequestBody(),
                        githubUrl = githubUrl.toPlainRequestBody(),
                        achievements = (achievements ?: "").toPlainRequestBody(),
                        hosteller = hosteller.toPlainRequestBody()
                    )

                    if (response.isSuccessful) {
                        profileSuccessMessage.value = "Profile submitted successfully!"
                        Toast.makeText(context, profileSuccessMessage.value, Toast.LENGTH_LONG).show()
                        Log.i("PROFILE", "submitProfileDetails: Profile updated")

                        onSuccess()
                    } else {
                        val errorBody = response.errorBody()?.string()
                        profileErrorMessage.value = errorBody ?: "Failed to submit profile"
                        Log.e("PROFILE", "submitProfileDetails: $errorBody")
                        Toast.makeText(context, profileErrorMessage.value, Toast.LENGTH_LONG).show()
                    }

                } else {
                    profileErrorMessage.value = "Token not found. Please log in again."
                    Toast.makeText(context, profileErrorMessage.value, Toast.LENGTH_LONG).show()
                }

            } catch (e: IOException) {
                profileErrorMessage.value = "Network error: Please check your connection"
                Log.e("ERROR", "IOException: ${e.message}")
            } catch (e: Exception) {
                profileErrorMessage.value = e.message ?: "An unexpected error occurred"
                Log.e("ERROR", "Exception: ${e.message}")
            } finally {
                isProfileLoading.value = false
            }
        }
    }






}


fun formatToISO8601(date: String): String {
    val formatterInput = DateTimeFormatter.ofPattern("d/M/yyyy")
    val parsedDate = LocalDate.parse(date, formatterInput)

    // Attach time and timezone (assuming IST +05:30 here)
    val dateTime = parsedDate.atStartOfDay(ZoneId.of("Asia/Kolkata"))
    return dateTime.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME)
}

fun String.toPlainRequestBody(): RequestBody =
    this.toRequestBody("text/plain".toMediaTypeOrNull())

fun Boolean.toPlainRequestBody(): RequestBody =
    this.toString().toRequestBody("text/plain".toMediaTypeOrNull())

fun Int.toPlainRequestBody(): RequestBody =
    this.toString().toRequestBody("text/plain".toMediaTypeOrNull())