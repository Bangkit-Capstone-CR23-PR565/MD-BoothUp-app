package com.example.eventmu.ui.favorite

import android.content.Context
import androidx.lifecycle.*
import com.example.eventmu.data.local.datastore.UserPreferences
import com.example.eventmu.data.local.entity.LikedEventEntity
import com.example.eventmu.data.repository.LikedEventRepository

import com.example.eventmu.helper.Injection
import com.example.eventmu.helper.ResultState

class FavoriteViewModel(
    private val userPreferences: UserPreferences,
    private val likedEventRepository: LikedEventRepository
) : ViewModel() {

    fun getLikedEvents(token: String, userId: Int): LiveData<ResultState<List<LikedEventEntity>>> {
        return likedEventRepository.getLikedEventsByUserId(token, userId)
    }

    class FavoriteViewModelFactory private constructor(
        private val likedEventRepository: LikedEventRepository,
        private val userPreferences: UserPreferences
    ) : ViewModelProvider.Factory {

        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(FavoriteViewModel::class.java)) {
                return FavoriteViewModel(userPreferences, likedEventRepository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }

        companion object {
            @Volatile
            private var instance: FavoriteViewModelFactory? = null

            fun getInstance(context: Context, userPreferences: UserPreferences): FavoriteViewModelFactory {
                return instance ?: synchronized(this) {
                    instance ?: FavoriteViewModelFactory(
                        Injection.provideLikedEventRepository(context),
                        userPreferences
                    )
                }.also { instance = it }
            }
        }
    }
}