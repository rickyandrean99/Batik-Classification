package com.rickyandrean.batikclassification

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.rickyandrean.batikclassification.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()
        binding.btnTakePicture.setBackgroundResource(R.drawable.bg_sapphire)
    }
}