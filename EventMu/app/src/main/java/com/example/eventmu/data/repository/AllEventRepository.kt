package com.example.eventmu.data.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.example.eventmu.data.local.entity.AllEventEntity
import com.example.eventmu.data.local.room.AllEventDao
import com.example.eventmu.data.remote.api.ApiService
import com.example.eventmu.data.remote.response.AllEventResponseItem
import com.example.eventmu.helper.AppExecutor
import com.example.eventmu.helper.ResultState
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AllEventRepository private constructor(
    private val apiService: ApiService,
    private val allEventDao: AllEventDao,
    private val appExecutor: AppExecutor
) {
    private val getEventResult = MediatorLiveData<ResultState<List<AllEventEntity>>>()

    fun getAllEvent(token: String): LiveData<ResultState<List<AllEventEntity>>> {
        getEventResult.value = ResultState.Loading
        val client = apiService.getAllEvent(token)

        client.enqueue(object : Callback<List<AllEventResponseItem>> {
            override fun onResponse(
                call: Call<List<AllEventResponseItem>>,
                response: Response<List<AllEventResponseItem>>
            ) {
                if (response.isSuccessful) {
                    val responseBody = response.body()

                    if (responseBody != null) {
                        try {
                            val eventList = mutableListOf<AllEventEntity>()

                            for (i in 0 until responseBody.size) {
                                val allEventResponseItem = responseBody[i]

                                val eventEntity = AllEventEntity(
                                    photoUrl = allEventResponseItem.imageUrl,
                                    description = allEventResponseItem.description,
                                    name = allEventResponseItem.name,
                                    location = allEventResponseItem.location,
                                    category = allEventResponseItem.category,
                                    id = allEventResponseItem.id,
                                    pricePerStand = allEventResponseItem.pricePerStand
                                )
                                eventList.add(eventEntity)
                            }

                            getEventResult.value = ResultState.Success(eventList)

                            appExecutor.diskIO.execute {
                                allEventDao.deleteAllEvent()
                                allEventDao.inserAllEvent(eventList)
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

            override fun onFailure(call: Call<List<AllEventResponseItem>>, t: Throwable) {
                val errorMessage = "Failed to get events - ${t.message.toString()}"
                Log.e(TAG, errorMessage)
                getEventResult.value = ResultState.Error(errorMessage)
            }
        })

        val localData = allEventDao.getAllEvent()
        getEventResult.addSource(localData) {
            getEventResult.value = ResultState.Success(it)
        }

        return getEventResult
    }


    companion object {
        private val TAG = AllEventRepository::class.java.simpleName

        @Volatile
        private var instance: AllEventRepository? = null

        fun getInstance(
            apiService: ApiService,
            allEventDao: AllEventDao,
            appExecutor: AppExecutor
        ): AllEventRepository =
            instance ?: synchronized(this) {
                instance ?: AllEventRepository(apiService, allEventDao, appExecutor)
            }.also { instance = it }
    }
}