package com.example.eventmu.data.remote.response

import com.google.gson.annotations.SerializedName

data class AllEventResponse(
	val allEventResponse: List<AllEventResponseItem>
)

data class AllEventResponseItem(

	@field:SerializedName("end_date")
	val endDate: String,

	@field:SerializedName("stand_available")
	val standAvailable: String,

	@field:SerializedName("like_count")
	val likeCount: String,

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
	val standCapacity: String,

	@field:SerializedName("price_per_stand")
	val pricePerStand: String,

	@field:SerializedName("start_date")
	val startDate: String
)
