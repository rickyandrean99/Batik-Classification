package com.rickyandrean.batikclassification.presentation.detail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.rickyandrean.batikclassification.databinding.ActivityDetailBinding
import kotlin.random.Random

class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding
    private lateinit var detailViewModel: DetailViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        val randomNumber = Random.nextInt(1, 11)
        detailViewModel = ViewModelProvider(this, DetailViewModelFactory.getInstance(application))[DetailViewModel::class.java]

        detailViewModel.getBatikRandom(randomNumber).observe(this) {
            if (it != null) {
                binding.tvDetailNameContent.text = it.name
                binding.tvDetailOriginContent.text = it.origin
                binding.tvDetailDescriptionContent.text = it.description
            }
        }
    }
}