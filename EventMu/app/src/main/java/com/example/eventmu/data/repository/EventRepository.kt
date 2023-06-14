package com.example.eventmu.data.repository

import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.example.eventmu.data.local.entity.EventEntity
import com.example.eventmu.data.local.room.EventDao
import com.example.eventmu.data.remote.api.ApiService
import com.example.eventmu.data.remote.response.EventResponse
import com.example.eventmu.data.remote.response.EventResponseItem
import com.example.eventmu.helper.AppExecutor
import com.example.eventmu.helper.ResultState
import com.google.gson.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EventRepository private constructor(
    private val apiService: ApiService,
    private val eventDao: EventDao,
    private val appExecutor: AppExecutor
) {
    private val getEventResult = MediatorLiveData<ResultState<List<EventEntity>>>()

    fun getEventsByUserId(token: String, userId: Int): LiveData<ResultState<List<EventEntity>>> {
        getEventResult.value = ResultState.Loading
        val client = apiService.getEvent(token, userId)

        client.enqueue(object : Callback<List<EventResponseItem>> {
            override fun onResponse(
                call: Call<List<EventResponseItem>>,
                response: Response<List<EventResponseItem>>
            ) {
                if (response.isSuccessful) {
                    val responseBody = response.body()

                    if (responseBody != null) {
                        try {
                            val eventList = mutableListOf<EventEntity>()

                            for (i in 0 until responseBody.size) {
                                val eventResponseItem = responseBody[i]

                                val eventEntity = EventEntity(
                                    photoUrl = eventResponseItem.imageUrl,
                                    description = eventResponseItem.description,
                                    name = eventResponseItem.name,
                                    location = eventResponseItem.location,
                                    category = eventResponseItem.category,
                                    id = eventResponseItem.id,
                                    pricePerStand = eventResponseItem.pricePerStand
                                )
                                eventList.add(eventEntity)
                            }

                            getEventResult.value = ResultState.Success(eventList)

                            appExecutor.diskIO.execute {
                                eventDao.deleteAllEvent()
                                eventDao.insertEvent(eventList)
                            }
                        } catch (e: Exception) {
                            val errorMessage = "Failed to parse events JSON"
                            Log.e(TAG, errorMessage, e)
                            getEventResult.value = ResultState.Error(errorMessage)
                        }
                    } else {
                        val errorMessage = "Empty response body"
                        Log.e(TAG, errorMessage)
                        getEventResult.value = ResultState.Error(errorMessage)
                    }
                } else {
                    val errorMessage = "Failed to get events - ${response.message()}"
                    Log.d(TAG, errorMessage)
                    getEventResult.value = ResultState.Error(errorMessage)
                }
            }

            override fun onFailure(call: Call<List<EventResponseItem>>, t: Throwable) {
                val errorMessage = "Failed to get events - ${t.message.toString()}"
                Log.e(TAG, errorMessage)
                getEventResult.value = ResultState.Error(errorMessage)
            }
        })

        val localData = eventDao.getEvent()
        getEventResult.addSource(localData) {
            getEventResult.value = ResultState.Success(it)
        }

        return getEventResult
    }


    companion object {
        private val TAG = EventRepository::class.java.simpleName

        @Volatile
        private var instance: EventRepository? = null

        fun getInstance(
            apiService: ApiService,
            eventDao: EventDao,
            appExecutor: AppExecutor
        ): EventRepository =
            instance ?: synchronized(this) {
                instance ?: EventRepository(apiService, eventDao, appExecutor)
            }.also { instance = it }
    }
}
