package com.example.eventmu.ui.home

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.eventmu.data.local.datastore.UserPreferences
import com.example.eventmu.databinding.FragmentHomeBinding
import com.example.eventmu.helper.ResultState
import com.example.eventmu.ui.all_event.AllEventActivity
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_preferences")

class HomeFragment : Fragment() {
    private lateinit var userPreferences: UserPreferences


    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!


    private lateinit var homeViewModel: HomeViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        (requireActivity() as AppCompatActivity).supportActionBar?.hide()
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val view = binding.root

        binding.btnSeeAll.setOnClickListener {
            val intent = Intent(requireContext(), AllEventActivity::class.java)
            startActivity(intent)
        }

        setSwipeRefreshLayout()

        return view
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        userPreferences = UserPreferences.getInstance(requireContext().dataStore)
        super.onViewCreated(view, savedInstanceState)

        val token = getTokenFromPreferences()
        val userId = getUserIdFromPreferences()

        homeViewModel = ViewModelProvider(
            this,
            HomeViewModel.HomeViewModelFactory.getInstance(
                requireActivity(),
                UserPreferences.getInstance(requireContext().dataStore)
            )
        )
            .get(HomeViewModel::class.java)

        viewEvents(token, userId)
    }

    private fun getTokenFromPreferences(): String {
        var token = ""
        lifecycleScope.launch {
            token = userPreferences.getToken().first()
        }
        return token
    }

    private fun getUserIdFromPreferences(): Int {
        var userId = 0
        lifecycleScope.launch {
            userId = userPreferences.getUserId().first()
        }
        return userId
    }


    private fun viewEvents(token: String, userId: Int) {
        homeViewModel.getEvents(token, userId).observe(viewLifecycleOwner) { resultState ->
            when (resultState) {
                is ResultState.Loading -> {
                    binding.viewLoading.visibility = View.VISIBLE
                }
                is ResultState.Error -> {
                    binding.viewLoading.visibility = View.INVISIBLE
                    binding.swipeRefresh.isRefreshing = false
//                    val error = resultState.error
//                    Toast.makeText(requireContext(), error, Toast.LENGTH_SHORT).show()
                }
                is ResultState.Success -> {
                    binding.viewLoading.visibility = View.INVISIBLE
                    binding.swipeRefresh.isRefreshing = false
                    val dataArray = resultState.data
                    binding.rvEvent.apply {
                        layoutManager = LinearLayoutManager(requireContext())
                        adapter = EventAdapter(requireContext(), dataArray)
                    }
                }
            }
        }

    }

    private fun setSwipeRefreshLayout() {
        binding.swipeRefresh.setOnRefreshListener {
            val token = getTokenFromPreferences()
            val userId = getUserIdFromPreferences()
            viewEvents(token, userId)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

