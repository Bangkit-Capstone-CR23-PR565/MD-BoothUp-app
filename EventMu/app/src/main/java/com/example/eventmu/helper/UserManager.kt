package com.example.eventmu.helper

object UserManager {
    private var isLoggedIn = false
    private var token: String? = null

    fun login(token: String) {
        isLoggedIn = true
        this.token = token
    }

    fun logout() {
        isLoggedIn = false
        token = null
    }

    fun isLoggedIn(): Boolean {
        return isLoggedIn
    }

    fun getToken(): String? {
        return token
    }
}
