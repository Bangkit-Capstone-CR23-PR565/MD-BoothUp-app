package com.example.eventmu.helper

import com.example.eventmu.data.remote.api.ApiConfig
import com.example.eventmu.data.repository.UserRepository

object Injection {
    fun provideUserRepository(): UserRepository {
        val apiService = ApiConfig.getApiService()
        return UserRepository.getInstance(apiService)
    }

}