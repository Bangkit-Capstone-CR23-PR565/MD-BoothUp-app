package com.example.eventmu.data.local.datastore

import android.content.Context
import android.content.SharedPreferences

class SessionManager(private val context: Context) {

    private val sharedPreferences: SharedPreferences = context.getSharedPreferences("MyAppSession", Context.MODE_PRIVATE)
    private val ACCOUNT_ID_KEY = "accountId"

    fun saveAccountId(accountId: String) {
        sharedPreferences.edit().putString(ACCOUNT_ID_KEY, accountId).apply()
    }

    fun getAccountId(): String? {
        return sharedPreferences.getString(ACCOUNT_ID_KEY, null)
    }

    fun clearSession() {
        sharedPreferences.edit().remove(ACCOUNT_ID_KEY).apply()
    }
}
