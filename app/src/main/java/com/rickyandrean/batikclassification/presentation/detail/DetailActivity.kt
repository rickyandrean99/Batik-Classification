package com.rickyandrean.batikclassification.presentation.detail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.rickyandrean.batikclassification.databinding.ActivityDetailBinding

class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupView()
    }

    private fun setupView() {
        supportActionBar?.hide()
    }
}