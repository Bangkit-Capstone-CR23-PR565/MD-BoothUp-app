package com.example.eventmu.ui.all_event

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.eventmu.R
import com.example.eventmu.data.local.datastore.UserPreferences
import com.example.eventmu.databinding.ActivityAllEventBinding
import com.example.eventmu.helper.ResultState
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_preferences")

class AllEventActivity : AppCompatActivity() {
    private lateinit var userPreferences: UserPreferences
    private lateinit var binding: ActivityAllEventBinding
    private lateinit var allEventViewModel: AllEventViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAllEventBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = getString(R.string.all_event)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        userPreferences = UserPreferences.getInstance(this.dataStore)

        val token = getTokenFromPreferences()

        allEventViewModel = ViewModelProvider(
            this,
            AllEventViewModel.AllEventViewModelFactory.getInstance(
                this,
                UserPreferences.getInstance(this.dataStore)
            )
        )
            .get(AllEventViewModel::class.java)

        viewAllEvents(token)

        setSwipeRefreshLayout()
    }

    private fun getTokenFromPreferences(): String {
        var token = ""
        lifecycleScope.launch {
            token = userPreferences.getToken().first()
        }
        return token
    }

    private fun viewAllEvents(token: String) {
        allEventViewModel.getAllEvent(token).observe(this) { resultState ->
            when (resultState) {
                is ResultState.Loading -> {
                    binding.viewLoading.visibility = View.VISIBLE
                }
                is ResultState.Error -> {
                    binding.viewLoading.visibility = View.INVISIBLE
                    binding.swipeRefresh.isRefreshing = false
                }
                is ResultState.Success -> {
                    binding.viewLoading.visibility = View.INVISIBLE
                    binding.swipeRefresh.isRefreshing = false
                    val dataArray = resultState.data
                    binding.rvEvent.apply {
                        layoutManager = LinearLayoutManager(this@AllEventActivity)
                        adapter = AllEventAdapter(this@AllEventActivity, dataArray)
                    }
                }
            }
        }
    }

    private fun setSwipeRefreshLayout() {
        binding.swipeRefresh.setOnRefreshListener {
            val token = getTokenFromPreferences()
            viewAllEvents(token)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        binding.root.removeAllViews()
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
}
