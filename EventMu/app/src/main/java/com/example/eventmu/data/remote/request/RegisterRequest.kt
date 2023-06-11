package com.example.eventmu.data.remote.request

import com.google.gson.annotations.SerializedName

data class RegisterRequest(
    @SerializedName("email")
    val email: String,
    @SerializedName("phone")
    val phone: String,
    @SerializedName("password")
    val password: String,
    @SerializedName("confPassword")
    val confPassword: String,
    @SerializedName("full_name")
    val fullName: String,
    @SerializedName("location")
    val location: String,
    @SerializedName("category_interest")
    val favEvent: String

)