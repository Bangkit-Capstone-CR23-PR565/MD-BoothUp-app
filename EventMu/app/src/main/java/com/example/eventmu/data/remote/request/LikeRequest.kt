package com.example.eventmu.data.remote.request

import com.google.gson.annotations.SerializedName

data class LikeRequest(
    @SerializedName("event_id")
    val eventId: Int
)
