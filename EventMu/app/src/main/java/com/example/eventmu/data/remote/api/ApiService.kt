package com.example.eventmu.data.remote.api

import com.example.eventmu.data.remote.request.LikeRequest
import com.example.eventmu.data.remote.request.LoginRequest
import com.example.eventmu.data.remote.request.RegisterRequest
import com.example.eventmu.data.remote.response.*
import retrofit2.Call
import retrofit2.http.*

interface ApiService {
    @POST("login")
    @Headers("Content-Type: application/json")
    fun login(
        @Body loginRequest: LoginRequest
    ): Call<LoginResponse>

    @POST("register")
    @Headers("Content-Type: application/json")
    fun register(
        @Body registerRequest: RegisterRequest
    ): Call<RegisterResponse>

    @POST("/users/{id}/likes")
    @Headers("Content-Type: application/json")
    fun addLike(
        @Header("Authorization") token: String,
        @Path("id") userId: Int,
        @Body likeRequest: LikeRequest
    ): Call<LikeResponse>

    @DELETE("users/{user_id}/likes/{event_id}")
    fun deleteLikedEvent(
        @Header("Authorization") token: String,
        @Path("user_id") userId: Int,
        @Path("event_id") eventId: Int
    ): Call<DeleteLikeResponse>

    @GET("/users/{id}/recommendation-results")
    fun getEvent(
        @Header("Authorization") token: String,
        @Path("id") id: Int,
        @Query("page") page: Int? = null,
        @Query("size") size: Int? = null
    ): Call<List<EventResponseItem>>

    @GET("/users/{id}/likes")
    fun getLikedEvent(
        @Header("Authorization") token: String,
        @Path("id") id: Int,
        @Query("page") page: Int? = null,
        @Query("size") size: Int? = null
    ): Call<List<LikedEventResponseItem>>


    @GET("users/{id}")
    fun getProfileData(
        @Header("Authorization") token: String,
        @Path("id") id: Int
    ): Call<User>

}