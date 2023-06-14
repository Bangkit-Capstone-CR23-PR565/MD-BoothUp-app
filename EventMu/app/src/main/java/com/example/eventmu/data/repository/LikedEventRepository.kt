package com.example.eventmu.data.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.example.eventmu.data.local.entity.LikedEventEntity
import com.example.eventmu.data.local.room.LikedEventDao
import com.example.eventmu.data.remote.api.ApiService
import com.example.eventmu.data.remote.response.LikedEventResponseItem
import com.example.eventmu.helper.AppExecutor
import com.example.eventmu.helper.ResultState
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LikedEventRepository private constructor(
    private val apiService: ApiService,
    private val likedEventDao: LikedEventDao,
    private val appExecutor: AppExecutor
) {
    private val getLikedEventResult = MediatorLiveData<ResultState<List<LikedEventEntity>>>()

    fun getLikedEventsByUserId(token: String, userId: Int): LiveData<ResultState<List<LikedEventEntity>>> {
        getLikedEventResult.value = ResultState.Loading
        val client = apiService.getLikedEvent(token, userId)

        client.enqueue(object : Callback<List<LikedEventResponseItem>> {
            override fun onResponse(
                call: Call<List<LikedEventResponseItem>>,
                response: Response<List<LikedEventResponseItem>>
            ) {
                if (response.isSuccessful) {
                    val responseBody = response.body()

                    if (responseBody != null) {
                        try {
                            val likedEventList = mutableListOf<LikedEventEntity>()

                            for (i in 0 until responseBody.size) {
                                val likedEventResponseItem = responseBody[i]

                                val likedEventEntity = LikedEventEntity(
                                    photoUrl = likedEventResponseItem.imageUrl,
                                    description = likedEventResponseItem.description,
                                    name = likedEventResponseItem.name,
                                    location = likedEventResponseItem.location,
                                    id = likedEventResponseItem.id,
                                    pricePerStand = likedEventResponseItem.pricePerStand
                                )
                                likedEventList.add(likedEventEntity)
                            }

                            getLikedEventResult.value = ResultState.Success(likedEventList)

                            appExecutor.diskIO.execute {
                                likedEventDao.deleteAllLikedEvent()
                                likedEventDao.insertLikedEvent(likedEventList)
                            }
                        } catch (e: Exception) {
                            val errorMessage = "Failed to parse events JSON"
                            Log.e(TAG, errorMessage, e)
                            getLikedEventResult.value = ResultState.Error(errorMessage)
                        }
                    } else {
                        val errorMessage = "Empty response body"
                        Log.e(TAG, errorMessage)
                        getLikedEventResult.value = ResultState.Error(errorMessage)
                    }
                } else {
                    val errorMessage = "Failed to get liked events - ${response.message()}"
                    Log.d(TAG, errorMessage)
                    getLikedEventResult.value = ResultState.Error(errorMessage)
                }
            }

            override fun onFailure(call: Call<List<LikedEventResponseItem>>, t: Throwable) {
                val errorMessage = "Failed to get liked events - ${t.message.toString()}"
                Log.e(TAG, errorMessage)
                getLikedEventResult.value = ResultState.Error(errorMessage)
            }
        })

        val localData = likedEventDao.getLikedEvent()
        getLikedEventResult.addSource(localData) {
            getLikedEventResult.value = ResultState.Success(it)
        }

        return getLikedEventResult
    }


    companion object {
        private val TAG = LikedEventRepository::class.java.simpleName

        @Volatile
        private var instance: LikedEventRepository? = null

        fun getInstance(
            apiService: ApiService,
            likedEventDao: LikedEventDao,
            appExecutor: AppExecutor
        ): LikedEventRepository =
            instance ?: synchronized(this) {
                instance ?: LikedEventRepository(apiService, likedEventDao, appExecutor)
            }.also { instance = it }
    }
}