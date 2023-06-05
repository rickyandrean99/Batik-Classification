package com.rickyandrean.batikclassification.presentation.guide

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.rickyandrean.batikclassification.R
import com.rickyandrean.batikclassification.databinding.ActivityGuideBinding

class GuideActivity : AppCompatActivity() {
    private lateinit var binding: ActivityGuideBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGuideBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupView()
    }

    private fun setupView() {
        supportActionBar?.hide()
    }
}