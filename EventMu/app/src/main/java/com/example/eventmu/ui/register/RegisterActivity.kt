package com.example.eventmu.ui.register

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import com.example.eventmu.R
import com.example.eventmu.databinding.ActivityRegisterBinding
import com.example.eventmu.helper.ResultState
import com.example.eventmu.ui.login.LoginActivity

class RegisterActivity : AppCompatActivity() {
    private val binding: ActivityRegisterBinding by lazy {
        ActivityRegisterBinding.inflate(layoutInflater)
    }

    private val registerViewModel: RegisterViewModel by viewModels {
        RegisterViewModel.RegisterViewModelFactory.getInstance()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(binding.root)


        val provinceArray = arrayOf(
            "Pilih Provinsi",
            "Nanggroe Aceh Darussalam",
            "Sumatera Utara",
            "Sumatera Selatan",
            "Sumatera Barat",
            "Bengkulu",
            "Riau",
            "Kepulauan Riau",
            "Jambi",
            "Lampung",
            "Bangka Belitung",
            "Kalimantan Barat",
            "Kalimantan Timur",
            "Kalimantan Selatan",
            "Kalimantan Tengah",
            "Kalimantan Utara",
            "Banten",
            "DKI Jakarta",
            "Jawa Barat",
            "Jawa Tengah",
            "Daerah Istimewa Yogyakarta",
            "Jawa Timur",
            "Bali",
            "Nusa Tenggara Timur",
            "Nusa Tenggara Barat",
            "Gorontalo",
            "Sulawesi Barat",
            "Sulawesi Tengah",
            "Sulawesi Utara",
            "Sulawesi Tenggara",
            "Sulawesi Selatan",
            "Maluku Utara",
            "Maluku",
            "Papua Barat",
            "Papua",
            "Papua Tengah",
            "Papua Pegunungan",
            "Papua Selatan",
            "Papua Barat Daya"
        )
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, provinceArray)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.provinceSpinner.adapter = adapter

        val favEventArray = arrayOf(
            "Pilih Event yang Diminati",
            "Otomotif",
            "Bangunan",
            "Kesehatan",
            "Teknologi",
            "Pariwisata",
            "Kuliner",
            "Edukasi",
            "Bisnis",
            "Sains",
            "Fashion",
            "Anak-anak",
            "Pertanian",
            "Transportasi",
            "Lingkungan",
            "Hewan",
            "Hiburan",
            "Musik",
            "Keuangan",
            "Furnitur",
            "Seni"
        )
        val adapter2 = ArrayAdapter(this, android.R.layout.simple_spinner_item, favEventArray)
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.favoriteSpinner.adapter = adapter2

        binding.btnRegister.setOnClickListener {
            val email = binding.etEmail.text.toString()
            val phone = binding.etPhone.text.toString()
            val password = binding.etPassword.text.toString()
            val confPassword = binding.etConfpassword.text.toString()
            val fullName = binding.etName.text.toString()
            val location: String = binding.provinceSpinner.selectedItem.toString()
            val favEvent: String = binding.favoriteSpinner.selectedItem.toString()

            if (fullName.isNotEmpty() && email.isNotEmpty() && phone.isNotEmpty() &&
                password.isNotEmpty() && password.length >= 8 &&
                confPassword.isNotEmpty() && confPassword.length >= 8 && password == confPassword &&
                location != "Pilih Provinsi" && favEvent != "Pilih Event yang Diminati"
            ) {
                val result = registerViewModel.register(
                    email,
                    phone,
                    password,
                    confPassword,
                    fullName,
                    location,
                    favEvent
                )
                result.observe(this) {
                    when (it) {
                        is ResultState.Error -> {
                            val error = it.error
                            Toast.makeText(this, error, Toast.LENGTH_SHORT).show()
                        }
                        is ResultState.Success -> {
                            binding.progressBar.visibility = View.INVISIBLE
                            Toast.makeText(
                                this,
                                getString(R.string.registration_success),
                                Toast.LENGTH_SHORT
                            ).show()
                            val intent = Intent(this, LoginActivity::class.java)
                            startActivity(intent)
                        }
                        is ResultState.Loading -> {
                            binding.progressBar.visibility = View.VISIBLE
                        }
                    }
                }
            } else {
                if (fullName.isNullOrEmpty()) binding.etName.error = getString(R.string.empty_name)
                else if (phone.isNullOrEmpty()) binding.etPhone.error =
                    getString(R.string.empty_phone)
                else if (password != confPassword) binding.etConfpassword.error =
                    getString(R.string.password_not_match)
                else if (location == "Pilih Provinsi") {
                    val error = getString(R.string.empty_location)
                    (binding.provinceSpinner.selectedView as TextView).error = error
                } else if (favEvent == "Pilih Event yang Diminati") {
                    val error = getString(R.string.empty_fav_event)
                    (binding.favoriteSpinner.selectedView as TextView).error = error
                }
            }
        }

        binding.btnLogin.setOnClickListener {
            startActivity(
                Intent(
                    this, LoginActivity::class.java
                )
            )
        }
    }
}
