package com.example.eventmu.data.remote.response

import com.google.gson.annotations.SerializedName

data class LoginResponse(

	@field:SerializedName("accessToken")
	val accessToken: String,
	@field:SerializedName("id")
	val userId: Int
)
