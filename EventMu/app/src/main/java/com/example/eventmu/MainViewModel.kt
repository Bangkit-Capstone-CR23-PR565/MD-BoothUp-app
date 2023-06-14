package com.example.eventmu

import android.content.Context
import androidx.lifecycle.*
import com.example.eventmu.data.local.datastore.UserPreferences
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class MainViewModel(
    private val userPreferences: UserPreferences
) : ViewModel() {

    fun checkToken(): LiveData<String> {
        return userPreferences.getToken().asLiveData()
    }

    class MainViewModelFactory private constructor(
        private val userPreferences: UserPreferences
    ) : ViewModelProvider.NewInstanceFactory() {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
                return MainViewModel(userPreferences) as T
            }

            throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }

        companion object {
            @Volatile
            private var instance: MainViewModelFactory? = null
            fun getInstance(
                context: Context,
                userPreferences: UserPreferences
            ): MainViewModelFactory = instance ?: synchronized(this) {
                instance ?: MainViewModelFactory(
                    userPreferences
                )
            }.also { instance = it }
        }
    }
}