package com.example.eventmu.data.local.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.eventmu.data.local.entity.EventEntity

@Dao
interface EventDao {
    @Query("SELECT * FROM event")
    fun getEvent(): LiveData<List<EventEntity>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertEvent(event: List<EventEntity>)

    @Query("DELETE FROM event")
    fun deleteAllEvent()
}