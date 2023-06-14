package com.example.eventmu.ui.all_event

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.eventmu.data.local.datastore.UserPreferences
import com.example.eventmu.data.local.entity.AllEventEntity
import com.example.eventmu.data.repository.AllEventRepository
import com.example.eventmu.helper.Injection
import com.example.eventmu.helper.ResultState

class AllEventViewModel(
    private val userPreferences: UserPreferences,
    private val allEventRepository: AllEventRepository
) : ViewModel() {
    fun getAllEvent(token: String): LiveData<ResultState<List<AllEventEntity>>> {
        return allEventRepository.getAllEvent(token)
    }

    class AllEventViewModelFactory private constructor(
        private val allEventRepository: AllEventRepository,
        private val userPreferences: UserPreferences
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(AllEventViewModel::class.java)) {
                return AllEventViewModel(userPreferences, allEventRepository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }
        companion object {
            @Volatile
            private var instance: AllEventViewModelFactory? = null
            fun getInstance(
                context: Context,
                userPreferences: UserPreferences
            ): AllEventViewModelFactory {
                return instance ?: synchronized(this) {
                    instance ?: AllEventViewModelFactory(
                        Injection.provideAllEventRepository(context),
                        userPreferences
                    )
                }.also { instance = it }
            }
        }
    }
}
