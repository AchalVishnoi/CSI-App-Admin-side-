package com.example.csiappcompose.Backend

import androidx.annotation.MainThread
import com.example.csiappcompose.dataModelsRequests.LoginRequest
import com.example.csiappcompose.dataModelsResponse.GroupListItem
import com.example.csiappcompose.dataModelsResponse.LoginResponse
import com.example.csiappcompose.dataModelsResponse.LogoutResponse
import com.example.csiappcompose.dataModelsResponse.chatMessages
import com.example.csiappcompose.dataModelsResponse.oldChatMessage
import com.example.csiappcompose.dataModelsResponse.oldMessagesResponse
import com.example.csiappcompose.dataModelsResponse.searchMemberItem
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    @POST("api/user/login/")
    suspend fun login(@Body LoginRequest: LoginRequest) : Response<LoginResponse>

    @POST("api/user/logout/")
    suspend fun logout(@Header("Authorization") token: String) : Response<LogoutResponse>

    @GET("api/chat/groups/")
    suspend fun getJoinedGroups(@Header("Authorization") token: String) : Response<List<GroupListItem>>

    @GET("api/chat/groups/{group_id}/messages/")
    suspend fun getOldMessages(
        @Path("group_id") groupId: Int,
        @Header("Authorization") token: String,
        @Query("page") page: Int,
        @Query("page_size") page_size: Int
    ): oldMessagesResponse


    @GET("/api/chat/groups/{room_id}/search/members/")
    suspend fun searchMember(
        @Path("room_id") roomId: Int,
        @Header("Authorization") token: String,
        @Query("q") query:String
    ) : Response<List<searchMemberItem>>

    @POST("/api/chat/groups/{room_id}/mark-as-read/")
    suspend fun markAsRead(
        @Path("room_id") roomId:Int,
        @Header("Authorization") token: String
    )


}