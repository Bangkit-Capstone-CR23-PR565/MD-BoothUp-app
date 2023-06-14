package com.example.eventmu.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.example.eventmu.data.remote.api.ApiService
import com.example.eventmu.data.remote.request.LoginRequest
import com.example.eventmu.data.remote.request.RegisterRequest
import com.example.eventmu.data.remote.response.LoginResponse
import com.example.eventmu.data.remote.response.RegisterResponse
import com.example.eventmu.helper.ResultState
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserRepository private constructor(private val apiService: ApiService) {
    private val loginResult = MediatorLiveData<ResultState<LoginResponse>>()
    private val registerResult = MediatorLiveData<ResultState<RegisterResponse>>()

    fun loginUser(email: String, password: String): LiveData<ResultState<LoginResponse>> {
        loginResult.value = ResultState.Loading
        val loginRequest = LoginRequest(email, password)
        val client = apiService.login(
            loginRequest
        )

        client.enqueue(object : Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                if (response.isSuccessful) {
                    val loginInfo = response.body()
                    if (loginInfo != null) {
                        loginResult.value = ResultState.Success(loginInfo)
                    } else {
                        loginResult.value = ResultState.Error(LOGIN_ERROR)
                    }
                } else {
                    loginResult.value = ResultState.Error(LOGIN_ERROR)
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                loginResult.value = ResultState.Error(LOGIN_ERROR)
            }
        })

        return loginResult
    }

    fun registerUser(
        email: String,
        phone: String,
        password: String,
        confPassword: String,
        fullName: String,
        location: String,
        favEvent: String
    ): LiveData<ResultState<RegisterResponse>> {
        registerResult.value = ResultState.Loading
        val registerRequest =
            RegisterRequest(email, phone, password, confPassword, fullName, location, favEvent)
        val client = apiService.register(
            registerRequest
        )
        client.enqueue(object : Callback<RegisterResponse> {
            override fun onResponse(
                call: Call<RegisterResponse>,
                response: Response<RegisterResponse>
            ) {
                if (response.isSuccessful) {
                    val registerStatus = response.body()
                    if (registerStatus != null) {
                        registerResult.value = ResultState.Success(registerStatus)
                    } else {
                        registerResult.value = ResultState.Error(REGISTER_ERROR)
                    }
                } else {
                    registerResult.value = ResultState.Error(REGISTER_ERROR)
                }
            }

            override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                registerResult.value = ResultState.Error(REGISTER_ERROR)
            }

        })

        return registerResult
    }

    companion object {
        @Volatile
        private var instance: UserRepository? = null

        fun getInstance(apiService: ApiService) =
            instance ?: synchronized(this) {
                instance ?: UserRepository(apiService)
            }.also { instance = it }

        private const val REGISTER_ERROR = "Failed to Register, please try again."
        private const val LOGIN_ERROR = "Failed to login, please try again."
    }
}