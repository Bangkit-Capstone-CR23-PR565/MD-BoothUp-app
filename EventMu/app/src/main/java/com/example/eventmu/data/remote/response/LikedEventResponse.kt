package com.example.eventmu.data.remote.response

import com.google.gson.annotations.SerializedName

data class LikedEventResponse(
	val likedEventResponse: List<LikedEventResponseItem>
)

data class LikedEventResponseItem(

	@field:SerializedName("end_date")
	val endDate: Any,

	@field:SerializedName("stand_available")
	val standAvailable: Int,

	@field:SerializedName("like_count")
	val likeCount: Int,

	@field:SerializedName("image_url")
	val imageUrl: String,

	@field:SerializedName("description")
	val description: String,

	@field:SerializedName("created_at")
	val createdAt: String,

	@field:SerializedName("updated_at")
	val updatedAt: String,

	@field:SerializedName("name")
	val name: String,

	@field:SerializedName("location")
	val location: String,

	@field:SerializedName("id")
	val id: Int,

	@field:SerializedName("category")
	val category: String,

	@field:SerializedName("stand_capacity")
	val standCapacity: Int,

	@field:SerializedName("price_per_stand")
	val pricePerStand: String,

	@field:SerializedName("start_date")
	val startDate: Any
)
