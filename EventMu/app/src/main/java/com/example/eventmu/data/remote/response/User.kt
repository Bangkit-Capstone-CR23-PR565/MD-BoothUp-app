package com.example.eventmu.data.remote.response

import com.google.gson.annotations.SerializedName

data class User(
    @field:SerializedName("full_name")
    val fullName: String,

    @field:SerializedName("phone")
    val phone: String,

    @field:SerializedName("category_interest")
    val categoryInterest: String,

    @field:SerializedName("location")
    val location: String,

    @field:SerializedName("email")
    val email: String,

)
