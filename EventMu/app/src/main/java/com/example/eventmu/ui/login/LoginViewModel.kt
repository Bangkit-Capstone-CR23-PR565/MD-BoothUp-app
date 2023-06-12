package com.example.eventmu.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.eventmu.data.local.datastore.UserPreferences
import com.example.eventmu.data.repository.UserRepository
import com.example.eventmu.helper.Injection
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LoginViewModel(
    private val userRepository: UserRepository,
    private val userPreferences: UserPreferences
) : ViewModel() {
    fun login(email: String, password: String) = userRepository.loginUser(email, password)

    fun saveToken(token: String) {
        viewModelScope.launch(Dispatchers.IO) {
            userPreferences.saveToken(token)
        }
    }

    fun saveUserId(userId : Int) {
        viewModelScope.launch(Dispatchers.IO) {
            userPreferences.saveUserId(userId)
        }
    }


    class LoginViewModelFactory private constructor(
        private val userRepository: UserRepository,
        private val loginPreferences: UserPreferences
    ) :
        ViewModelProvider.NewInstanceFactory() {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
                return LoginViewModel(userRepository, loginPreferences) as T
            }

            throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }

        companion object {
            @Volatile
            private var instance: LoginViewModelFactory? = null
            fun getInstance(
                loginPreferences: UserPreferences
            ): LoginViewModelFactory =
                instance ?: synchronized(this) {
                    instance ?: LoginViewModelFactory(
                        Injection.provideUserRepository(),
                        loginPreferences
                    )
                }
        }
    }
}