package com.example.eventmu.data.remote.api

import com.example.eventmu.data.remote.request.LoginRequest
import com.example.eventmu.data.remote.request.RegisterRequest
import com.example.eventmu.data.remote.response.LoginResponse
import com.example.eventmu.data.remote.response.RegisterResponse
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
}