package com.example.eventmu.helper

import android.content.Context
import com.example.eventmu.data.local.room.EventDatabase
import com.example.eventmu.data.local.room.LikedEventDatabase
import com.example.eventmu.data.remote.api.ApiConfig
import com.example.eventmu.data.repository.EventRepository
import com.example.eventmu.data.repository.LikedEventRepository
import com.example.eventmu.data.repository.UserRepository
import com.example.eventmu.ui.home.HomeFragment

object Injection {
    fun provideUserRepository(): UserRepository {
        val apiService = ApiConfig.getApiService()
        return UserRepository.getInstance(apiService)
    }

    fun provideEventRepository(context: Context): EventRepository {
        val apiService = ApiConfig.getApiService()
        val database = EventDatabase.getInstance(context)
        val eventDao = database.eventDao()
        val appExecutors = AppExecutor()
        return EventRepository.getInstance(apiService, eventDao, appExecutors)
    }

    fun provideLikedEventRepository(context: Context): LikedEventRepository {
        val apiService = ApiConfig.getApiService()
        val database = LikedEventDatabase.getInstance(context)
        val likedEventDao = database.likedEventDao()
        val appExecutors = AppExecutor()
        return LikedEventRepository.getInstance(apiService, likedEventDao, appExecutors)
    }

}