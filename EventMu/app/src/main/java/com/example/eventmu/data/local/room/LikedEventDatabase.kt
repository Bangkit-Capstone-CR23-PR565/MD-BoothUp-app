package com.example.eventmu.data.local.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.eventmu.data.local.entity.LikedEventEntity

@Database(entities = [LikedEventEntity::class], version = 1)
abstract class LikedEventDatabase : RoomDatabase() {
    abstract fun likedEventDao(): LikedEventDao

    companion object {
        @Volatile
        private var instance: LikedEventDatabase? = null
        fun getInstance(context: Context): LikedEventDatabase =
            instance ?: synchronized(this) {
                instance ?: Room.databaseBuilder(context, LikedEventDatabase::class.java, "liked_event.db")
                    .build()
            }.also { instance = it }
    }
}