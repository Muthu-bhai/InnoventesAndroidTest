package com.innoventes.android.test.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.innoventes.android.test.databinding.ActivityUserDetailsBinding

class UserDetailsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUserDetailsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityUserDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val panNumber = intent.getStringExtra("PAN_NUMBER")
        val dateOfBirth = intent.getStringExtra("DATE_OF_BIRTH")

        binding.panNumberTextView.text = "PAN Number : $panNumber"
        binding.dateOfBirthTextView.text = "Date of Birth : $dateOfBirth"
    }
}

