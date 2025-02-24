package com.example.csiappcompose.Backend

import com.example.csiappcompose.dataModelsRequests.LoginRequest
import com.example.csiappcompose.dataModelsResponse.GroupListItem
import com.example.csiappcompose.dataModelsResponse.LoginResponse
import com.example.csiappcompose.dataModelsResponse.LogoutResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface ApiService {

    @POST("api/user/login/")
    suspend fun login(@Body LoginRequest: LoginRequest) : Response<LoginResponse>

    @POST("api/user/logout/")
    suspend fun logout(@Header("Authorization") token: String) : Response<LogoutResponse>

    @GET("api/chat/groups/")
    suspend fun getJoinedGroups(@Header("Authorization") token: String) : Response<List<GroupListItem>>

}