package com.app.instagram.auth

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.app.instagram.R
import com.app.instagram.databinding.ActivityLoginPhoneBinding

class LoginPhoneActivity : AppCompatActivity() {

    private lateinit var binding:ActivityLoginPhoneBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginPhoneBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }
}