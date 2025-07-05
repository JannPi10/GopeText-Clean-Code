package com.example.gopetext.data.api

import com.example.gopetext.data.model.Contact
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part

interface AuthService {

    @POST("api/register")
    suspend fun register(@Body request: RegisterRequest): Response<RegisterResponse>

    @POST("api/login")
    suspend fun login(@Body request: LoginRequest): Response<LoginResponse>

    @POST("api/logout")
    suspend fun logout(): Response<LogoutResponse>

    @GET("api/user")
    suspend fun getUserProfile(): Response<UserSingleResponse>

    @Multipart
    @PUT("api/user")
    suspend fun showUserProfile(
        @Part("name") name: RequestBody,
        @Part("last_name") lastName: RequestBody,
        @Part("age") age: RequestBody
    ): Response<ShowProfileResponse>

    @Multipart
    @POST("api/user/profile/image")
    suspend fun uploadProfileImage(
        @Part nombreimagen: MultipartBody.Part
    ): Response<UploadProfileImageResponse>

    @GET("api/users")
    suspend fun getAllUsers(): Response<ListUsersResponse>
    
    @GET("api/chat/message")
    suspend fun getChats(): Response<List<Contact>>
}