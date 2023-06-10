package com.example.eventmu.data.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.example.eventmu.data.remote.api.ApiService
import com.example.eventmu.data.remote.request.LoginRequest
import com.example.eventmu.data.remote.response.LoginResponse
import com.example.eventmu.helper.ResultState
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserRepository private constructor(private val apiService: ApiService) {
    private val loginResult = MediatorLiveData<ResultState<LoginResponse>>()

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

    companion object {
        @Volatile
        private var instance: UserRepository? = null

        fun getInstance(apiService: ApiService) =
            instance ?: synchronized(this) {
                instance ?: UserRepository(apiService)
            }.also { instance = it }

//        private const val REGISTER_ERROR = "Failed to Register, please try again."
        private const val LOGIN_ERROR = "Failed to login, please try again."
    }
}