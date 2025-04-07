package com.example.csiappcompose.Backend

import com.example.csiappcompose.dataModelsRequests.ForgotPasswordRequest
import com.example.csiappcompose.dataModelsRequests.ForgotPasswordResponse
import com.example.csiappcompose.dataModelsRequests.LoginRequest
import com.example.csiappcompose.dataModelsRequests.resetPasswordRequest
import com.example.csiappcompose.dataModelsResponse.EventItem
import com.example.csiappcompose.dataModelsResponse.GroupListItem
import com.example.csiappcompose.dataModelsResponse.HomePageStats
import com.example.csiappcompose.dataModelsResponse.LoginResponse
import com.example.csiappcompose.dataModelsResponse.LogoutResponse
import com.example.csiappcompose.dataModelsResponse.announcmentDisplay
import com.example.csiappcompose.dataModelsResponse.createEvent
import com.example.csiappcompose.dataModelsResponse.fillYourDetailsResponse
import com.example.csiappcompose.dataModelsResponse.oldMessagesResponse
import com.example.csiappcompose.dataModelsResponse.searchMemberItem
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    @POST("api/user/login/")
    suspend fun login(@Body LoginRequest: LoginRequest) : Response<LoginResponse>

    @POST("api/user/forgot-password/")
    suspend fun forgetPassword(
        @Body request: ForgotPasswordRequest
    ): Response<ForgotPasswordResponse>

    @POST("api/user/reset-password/")
    suspend fun resetPassword(
        @Body request: resetPasswordRequest
    ): Response<ForgotPasswordResponse>


    @POST("api/user/logout/")
    suspend fun logout(@Header("Authorization") token: String) : Response<LogoutResponse>


    @Multipart
    @POST("/api/user/profile/fill/")
    suspend fun submitProfile(
        @Header("Authorization") token: String,
        @Part photo: MultipartBody.Part,
        @Part("branch") branch: RequestBody,
        @Part("domain") domain: RequestBody,
        @Part("dob") dob: RequestBody,
        @Part("linkedin_url") linkedinUrl: RequestBody,
        @Part("bio") bio: RequestBody,
        @Part("github_url") githubUrl: RequestBody,
        @Part("achievements") achievements: RequestBody,
        @Part("hosteller") hosteller: RequestBody
    ) : Response<fillYourDetailsResponse>



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


    //home page
    @GET(" /api/homepage/stats/")
    suspend fun homepageStats(
        @Header("Authorization") token: String
    ) : Response<HomePageStats>

    @GET("/api/homepage/announcements/")
    suspend fun announcements(
        @Header("Authorization") token: String
    ): Response<List<announcmentDisplay>>

  @GET("/api/homepage/upcoming-events/")
    suspend fun upComingEvent(
        @Header("Authorization") token: String
    ):Response<List<EventItem>>

    @GET("/api/homepage/ongoing-events/")
    suspend fun ongoingEvent(
        @Header("Authorization") token: String
    ):Response<List<EventItem>>

    @Multipart
    @POST("api/event/create/")
    suspend fun createEvent(
        @Header("Authorization") token: String,
        @Part("title") title: RequestBody,
        @Part("description") description: RequestBody,
        @Part("guidelines") guidelines: RequestBody,
        @Part("venue") venue: RequestBody,
        @Part("registration_start_date") registrationStartDate: RequestBody,
        @Part("registration_end_date") registrationEndDate: RequestBody,
        @Part("event_date") eventDate: RequestBody,
        @Part("status") status: RequestBody,
        @Part("is_registrations_open") isRegistrationsOpen: RequestBody,
        @Part("payment_required") paymentRequired: RequestBody,
        @Part("amount") amount: RequestBody,
        @Part poster: MultipartBody.Part?,
        @Part gallery: List<MultipartBody.Part>?
    ): Response<createEvent>


}