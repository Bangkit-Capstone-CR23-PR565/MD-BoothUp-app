package com.example.eventmu.ui.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.eventmu.data.repository.UserRepository
import com.example.eventmu.helper.Injection

class RegisterViewModel(private val userRepository: UserRepository) : ViewModel() {
    fun register(email: String, phone: String, password: String, confPassword: String, fullName: String, location: String, favEvent: String) =
        userRepository.registerUser(email,phone, password, confPassword, fullName, location, favEvent)

    class RegisterViewModelFactory private constructor(private val userRepository: UserRepository) :
        ViewModelProvider.NewInstanceFactory() {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(RegisterViewModel::class.java)) {
                return RegisterViewModel(userRepository) as T
            }

            throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }

        companion object {
            @Volatile
            private var instance: RegisterViewModelFactory? = null

            fun getInstance(): RegisterViewModelFactory = instance ?: synchronized(this) {
                instance ?: RegisterViewModelFactory(Injection.provideUserRepository())
            }
        }
    }
}