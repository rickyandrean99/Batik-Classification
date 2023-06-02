package com.rickyandrean.batikclassification.presentation.main

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.rickyandrean.batikclassification.R
import com.rickyandrean.batikclassification.databinding.ActivityMainBinding
import com.rickyandrean.batikclassification.presentation.preview.PreviewActivity

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupView()
        setupListener()
    }

    private fun setupView() {
        supportActionBar?.hide()
        binding.btnTakePicture.setBackgroundResource(R.drawable.bg_sapphire)
        binding.btnTips.setBackgroundResource(R.drawable.bg_light_orange)
    }

    private fun setupListener() {
        binding.btnTakePicture.setOnClickListener {
            val intent = Intent(this@MainActivity, PreviewActivity::class.java)
            startActivity(intent)
        }
    }
}