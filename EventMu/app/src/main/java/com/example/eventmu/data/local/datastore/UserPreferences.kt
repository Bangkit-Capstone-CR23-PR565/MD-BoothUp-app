package com.example.eventmu.data.local.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class UserPreferences private constructor(private val dataStore: DataStore<Preferences>) {
    private val token = stringPreferencesKey("TOKEN")
    private val userId = stringPreferencesKey("USER_ID") // Menambahkan PreferencesKey untuk ID pengguna
    private val USER_ID_KEY = intPreferencesKey("user_id")


    fun getToken(): Flow<String> {
        return dataStore.data.map {
            it[token] ?: "null"
        }
    }


    fun getUserId(): Flow<Int> = dataStore.data.map { preferences ->
        preferences[USER_ID_KEY] ?: 0 // Menggunakan nilai default 0 jika data tidak ada
    }


    suspend fun saveToken(token: String) {
        dataStore.edit {
            it[this.token] = token
        }
    }

    suspend fun saveUserId(userId: Int) {
        dataStore.edit { preferences ->
            preferences[USER_ID_KEY] = userId
        }
    }


    suspend fun deleteToken() {
        dataStore.edit {
            it[token] = "null"
        }
    }

    companion object {
        @Volatile
        private var instance: UserPreferences? = null

        fun getInstance(dataStore: DataStore<Preferences>): UserPreferences =
            instance ?: synchronized(this) {
                instance ?: UserPreferences(dataStore)
            }.also { instance = it }
    }
}
