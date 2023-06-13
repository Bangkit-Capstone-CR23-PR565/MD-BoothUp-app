package com.example.eventmu.ui.detail

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import com.bumptech.glide.Glide
import com.example.eventmu.R
import com.example.eventmu.databinding.ActivityDetailBinding

class DetailActivity : AppCompatActivity() {
    private val binding: ActivityDetailBinding by lazy {
        ActivityDetailBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        supportActionBar?.title = getString(R.string.detail_event)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val name = intent.getStringExtra(NAME_EXTRA)
        val description = intent.getStringExtra(DESCRIPTION_EXTRA)
        val price = intent.getStringExtra(PRICE_EXTRA)
        val imgUrl = intent.getStringExtra(IMAGE_URL_EXTRA)

        binding.tvEventName.text = name
        binding.tvEventDescription.text = description
        binding.tvPrice.text = price
        Glide.with(this)
            .load(imgUrl)
            .into(binding.ivStoryImage)

        binding.btnWa.setOnClickListener {
            openWhatsApp()
        }
        binding.btnGmail.setOnClickListener{
            openGmail()
        }
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
    }
}