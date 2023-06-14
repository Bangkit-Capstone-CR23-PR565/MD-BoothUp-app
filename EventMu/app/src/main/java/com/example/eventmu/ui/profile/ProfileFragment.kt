package com.example.eventmu.ui.profile

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.fragment.app.Fragment
import com.example.eventmu.data.local.datastore.UserPreferences
import com.example.eventmu.databinding.FragmentProfileBinding

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_preferences")
class ProfileFragment : Fragment() {
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    private val userPreferences: UserPreferences by lazy {
        UserPreferences.getInstance(requireContext().dataStore)
    }

    private val profileViewModel: ProfileViewModel by lazy {
        ProfileViewModel(userPreferences)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View {
        (requireActivity() as AppCompatActivity).supportActionBar?.hide()
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        val root: View = binding.root



        profileViewModel.fullName.observe(viewLifecycleOwner) { fullName ->
            binding.tvName.text = fullName
        }

        profileViewModel.email.observe(viewLifecycleOwner) { email ->
            binding.tvEmail.text = email
        }

        profileViewModel.phone.observe(viewLifecycleOwner) { phone ->
            binding.tvPhone.text = phone
        }

        profileViewModel.location.observe(viewLifecycleOwner) { location ->
            binding.tvProvince.text = location
        }

        profileViewModel.categoryInterest.observe(viewLifecycleOwner) { categoryInterest ->
            binding.tvFavEvent.text = categoryInterest
        }

        profileViewModel.loadUserProfile()

        val btnGmail: Button = binding.btnPartner
        btnGmail.setOnClickListener {
            openGmail()
        }

        val btnLogout: Button = binding.btnLogout
        btnLogout.setOnClickListener{
            profileViewModel.logout()
        }

        return root
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}



