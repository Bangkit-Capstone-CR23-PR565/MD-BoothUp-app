package com.example.eventmu.data.local.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.eventmu.data.local.entity.AllEventEntity

@Dao
interface AllEventDao {
    @Query("SELECT * FROM all_event")
    fun getAllEvent(): LiveData<List<AllEventEntity>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun inserAllEvent(event: List<AllEventEntity>)

    @Query("DELETE FROM all_event")
    fun deleteAllEvent()
}