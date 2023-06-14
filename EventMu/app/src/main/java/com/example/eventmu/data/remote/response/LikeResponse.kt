package com.example.eventmu.data.remote.response

import com.google.gson.annotations.SerializedName

data class LikeResponse(

	@field:SerializedName("added_at")
	val addedAt: AddedAt,

	@field:SerializedName("event_id")
	val eventId: Int,

	@field:SerializedName("user_id")
	val userId: String,

	@field:SerializedName("id")
	val id: Int
)

data class AddedAt(

	@field:SerializedName("val")
	val added: String
)
