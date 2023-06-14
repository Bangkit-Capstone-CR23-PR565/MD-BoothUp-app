package com.example.eventmu.ui.home

import android.content.Context
import androidx.lifecycle.*
import com.example.eventmu.data.local.datastore.UserPreferences
import com.example.eventmu.data.local.entity.EventEntity
import com.example.eventmu.data.repository.EventRepository
import com.example.eventmu.helper.Injection
import com.example.eventmu.helper.ResultState

class HomeViewModel(
    private val userPreferences: UserPreferences,
    private val eventRepository: EventRepository
) : ViewModel() {

    fun getEvents(token: String, userId: Int): LiveData<ResultState<List<EventEntity>>> {
        return eventRepository.getEventsByUserId(token, userId)
    }

    class HomeViewModelFactory private constructor(
        private val eventRepository: EventRepository,
        private val userPreferences: UserPreferences
    ) : ViewModelProvider.Factory {

        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
                return HomeViewModel(userPreferences, eventRepository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }

        companion object {
            @Volatile
            private var instance: HomeViewModelFactory? = null

            fun getInstance(
                context: Context,
                userPreferences: UserPreferences
            ): HomeViewModelFactory {
                return instance ?: synchronized(this) {
                    instance ?: HomeViewModelFactory(
                        Injection.provideEventRepository(context),
                        userPreferences
                    )
                }.also { instance = it }
            }
        }
    }
}
