package com.rickyandrean.batikclassification.presentation.main

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.rickyandrean.batikclassification.R
import com.rickyandrean.batikclassification.databinding.ActivityMainBinding
import com.rickyandrean.batikclassification.presentation.camera.CameraActivity

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
    }

    private fun setupListener() {
        binding.btnTakePicture.setOnClickListener {
            val intent = Intent(this@MainActivity, CameraActivity::class.java)
            startActivity(intent)
        }
    }
}