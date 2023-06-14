package com.example.eventmu.ui.detail

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.example.eventmu.R
import com.example.eventmu.data.local.datastore.UserPreferences
import com.example.eventmu.data.local.room.LikedEventDao
import com.example.eventmu.data.local.room.LikedEventDatabase
import com.example.eventmu.data.remote.api.ApiConfig
import com.example.eventmu.data.remote.request.LikeRequest
import com.example.eventmu.data.remote.response.DeleteLikeResponse
import com.example.eventmu.data.remote.response.LikeResponse
import com.example.eventmu.databinding.ActivityDetailBinding
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_preferences")

class DetailActivity : AppCompatActivity() {
    private val binding: ActivityDetailBinding by lazy {
        ActivityDetailBinding.inflate(layoutInflater)
    }

    private lateinit var userPreferences: UserPreferences
    private lateinit var likedEventDao: LikedEventDao
    private lateinit var likedEventDatabase: LikedEventDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        userPreferences = UserPreferences.getInstance(applicationContext.dataStore)
        likedEventDatabase = LikedEventDatabase.getInstance(applicationContext)
        likedEventDao = likedEventDatabase.likedEventDao()

        val name = intent.getStringExtra(NAME_EXTRA)
        val description = intent.getStringExtra(DESCRIPTION_EXTRA)
        val price = intent.getStringExtra(PRICE_EXTRA)
        val imgUrl = intent.getStringExtra(IMAGE_URL_EXTRA)
        val location = intent.getStringExtra(LOCATION_EXTRA)
        val category = intent.getStringExtra(CATEGORY_EXTRA)
        val id = intent.getIntExtra(ID_EXTRA, 0)

        var token: String
        var userId: Int

        lifecycleScope.launch {
            token = getTokenFromPreferences()
            userId = getUserIdFromPreferences()

            binding.tvEventName.text = name
            binding.tvEventDescription.text = description
            binding.tvPrice.text = price
            binding.tvLocation.text = location
            binding.tvCategory.text = category

            Glide.with(this@DetailActivity)
                .load(imgUrl)
                .into(binding.ivStoryImage)

            binding.btnWa.setOnClickListener {
                openWhatsApp()
            }
            binding.btnGmail.setOnClickListener {
                openGmail()
            }

            var isChecked = false
            val count = checkLikedEvent(id)
            if (count != null) {
                isChecked = count > 0
                binding.toggleFavorite.isChecked = isChecked
            }

            binding.toggleFavorite.setOnClickListener {
                isChecked = !isChecked
                if (isChecked) {
                    addToFavorite(token, userId, id)
                } else {
                    deleteLikedEvent(token, userId, id)
                }
                binding.toggleFavorite.isChecked = isChecked
            }
        }

        supportActionBar?.title = getString(R.string.detail_event)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private suspend fun checkLikedEvent(id: Int) = likedEventDao?.checkLikedEvent(id)

    private suspend fun getTokenFromPreferences(): String {
        return "Bearer ${userPreferences.getToken().first()}"
    }

    private suspend fun getUserIdFromPreferences(): Int {
        return userPreferences.getUserId().first()
    }

    private fun addToFavorite(token: String, userId: Int, eventId: Int) {
        Log.d("AddToFavorite", "Tokennyaaaa: $token")
        val apiService = ApiConfig.getApiService()

        val likeRequest = LikeRequest(eventId)

        val call = apiService.addLike(token, userId, likeRequest)

        call.enqueue(object : Callback<LikeResponse> {
            override fun onResponse(call: Call<LikeResponse>, response: Response<LikeResponse>) {
                if (response.isSuccessful) {
                    Log.d("AddToFavorite", "Event berhasil ditambahkan ke favorit")
                } else {
                    Log.e("AddToFavorite", "Gagal menambahkan ke favorit: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<LikeResponse>, t: Throwable) {
                Log.e("AddToFavorite", "Error: ${t.message}")
            }
        })
    }

    fun deleteLikedEvent(token: String, userId: Int, eventId: Int) {
        val apiService = ApiConfig.getApiService()

        val call = apiService.deleteLikedEvent(token, userId, eventId)

        call.enqueue(object : Callback<DeleteLikeResponse> {
            override fun onResponse(
                call: Call<DeleteLikeResponse>,
                response: Response<DeleteLikeResponse>
            ) {
                if (response.isSuccessful) {
                    Log.d("DeleteLikedEvent", "Liked event berhasil dihapus")
                } else {
                    Log.e("DeleteLikedEvent", "Gagal menghapus liked event: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<DeleteLikeResponse>, t: Throwable) {
                Log.e("DeleteLikedEvent", "Error: ${t.message}")
            }
        })
    }


    private fun openGmail() {
        val email = "boothupapp@gmail.com"
        val subject = "I Wanna be your partner!"
        val message = "Hi, BoothUP! I want to register my event."

        val intent = Intent(Intent.ACTION_SENDTO)
        intent.data = Uri.parse("mailto:$email")
        intent.putExtra(Intent.EXTRA_SUBJECT, subject)
        intent.putExtra(Intent.EXTRA_TEXT, message)

        startActivity(Intent.createChooser(intent, "Send Email"))
    }

    private fun openWhatsApp() {
        val phoneNumber = "6289668937980" //bayu daru
        val message = "Halo BoothUP! Saya berminat untuk mendaftar event."
        val intent = Intent(Intent.ACTION_VIEW).apply {
            data = Uri.parse("https://api.whatsapp.com/send?phone=$phoneNumber&text=$message")
        }
        startActivity(intent)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    companion object {
        const val NAME_EXTRA = "name_extra"
        const val DESCRIPTION_EXTRA = "desc_extra"
        const val PRICE_EXTRA = "price_extra"
        const val IMAGE_URL_EXTRA = "img_extra"
        const val LOCATION_EXTRA = "location_extra"
        const val CATEGORY_EXTRA = "category_extra"
        const val ID_EXTRA = "id"
    }
}
