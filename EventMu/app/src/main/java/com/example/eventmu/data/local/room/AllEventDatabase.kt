package com.example.eventmu.data.local.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.eventmu.data.local.entity.AllEventEntity

@Database(entities = [AllEventEntity::class], version = 1)
abstract class AllEventDatabase : RoomDatabase() {
    abstract fun allEventDao(): AllEventDao

    companion object {
        @Volatile
        private var instance: AllEventDatabase? = null
        fun getInstance(context: Context): AllEventDatabase =
            instance ?: synchronized(this) {
                instance ?: Room.databaseBuilder(
                    context,
                    AllEventDatabase::class.java,
                    "all_event.db"
                )
                    .build()
            }.also { instance = it }
    }
}