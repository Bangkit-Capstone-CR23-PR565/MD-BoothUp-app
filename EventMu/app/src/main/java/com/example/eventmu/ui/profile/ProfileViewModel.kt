package com.example.eventmu.ui.profile

import android.util.Log
import androidx.lifecycle.*
import com.example.eventmu.data.local.datastore.UserPreferences
import com.example.eventmu.data.remote.api.ApiConfig
import com.example.eventmu.data.remote.response.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class ProfileViewModel(private val userPreferences: UserPreferences) : ViewModel() {
    private val _fullName = MutableLiveData<String>()
    val fullName: LiveData<String> get() = _fullName

    private val _phone = MutableLiveData<String>()
    val phone: LiveData<String> get() = _phone

    private val _categoryInterest = MutableLiveData<String>()
    val categoryInterest: LiveData<String> get() = _categoryInterest

    private val _location = MutableLiveData<String>()
    val location: LiveData<String> get() = _location

    private val _email = MutableLiveData<String>()
    val email: LiveData<String> get() = _email

    fun loadUserProfile() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                try {
                    val userId = userPreferences.getUserId().first()
                    val userProfile = fetchUserProfile(userId)
                    _fullName.postValue(userProfile.fullName)
                    _phone.postValue(userProfile.phone)
                    _categoryInterest.postValue(userProfile.categoryInterest)
                    _location.postValue(userProfile.location)
                    _email.postValue(userProfile.email)
                } catch (e: Exception) {
                    Log.e("ProfileViewModel", "Failed to fetch user profile: ${e.message}")
                }
            }
        }
    }

    private suspend fun fetchUserProfile(userId: Int): User {
        Log.d("ProfileViewModel", "User ID: $userId")
        try {
            val apiService = ApiConfig.getApiService()
            val token = userPreferences.getToken().first() // Mendapatkan token dari UserPreferences

            val response = apiService.getProfileData(token, userId).execute()

            if (response.isSuccessful) {
                val user = response.body()
                // Mengambil data nama dan email dari objek user yang diperoleh
                val fullName = user?.fullName ?: ""
                Log.d("ProfileViewModel", "response: ${user}")
                val phone = user?.phone ?: ""
                val categoryInterest = user?.categoryInterest ?: ""
                val location = user?.location ?: ""
                val email = user?.email ?: ""
                // Membuat objek UserProfile dari data yang diperoleh
                return User(fullName, phone, categoryInterest, location, email)
            } else {
                val errorBody = response.errorBody()?.string()
                throw Exception("Failed to fetch user profile. Error: $errorBody")
            }
        } catch (e: Exception) {
            throw Exception("Failed to fetch user profile. Error: ${e.message}")
        }
    }

    fun logout() {
        viewModelScope.launch(Dispatchers.IO) {
            userPreferences.deleteToken()
        }
    }
}




