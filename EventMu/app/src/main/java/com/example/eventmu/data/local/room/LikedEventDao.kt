package com.example.eventmu.data.local.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.eventmu.data.local.entity.LikedEventEntity

@Dao
interface LikedEventDao {
    @Query("SELECT * FROM liked_event")
    fun getLikedEvent(): LiveData<List<LikedEventEntity>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertLikedEvent(event: List<LikedEventEntity>)

    @Query("DELETE FROM liked_event")
    fun deleteAllLikedEvent()

    @Query("SELECT count(*) FROM liked_event WHERE liked_event.id = :id")
    suspend fun checkLikedEvent(id: kotlin.Int): Int
}