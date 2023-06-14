package com.example.eventmu.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "liked_event")
data class LikedEventEntity(

    @field:ColumnInfo(name = "photoUrl")
    val photoUrl: String,

    @field:ColumnInfo(name = "name")
    val name: String,

    @field:ColumnInfo(name = "location")
    val location: String,

    @field:ColumnInfo(name = "price_per_stand")
    val pricePerStand: String,

    @field:ColumnInfo(name = "description")
    val description: String,

    @field:SerializedName("category")
    val category: String,

    @field:ColumnInfo(name = "id")
    @PrimaryKey
    val id: Int

)