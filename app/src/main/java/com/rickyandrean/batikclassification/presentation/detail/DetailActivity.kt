package com.rickyandrean.batikclassification.presentation.detail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.rickyandrean.batikclassification.databinding.ActivityDetailBinding
import com.rickyandrean.batikclassification.model.PredictResponse
import kotlin.random.Random

class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding
    private lateinit var detailViewModel: DetailViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        val predictResult = intent.getParcelableExtra<PredictResponse>(PREDICT_RESULT) as PredictResponse

        detailViewModel = ViewModelProvider(this, DetailViewModelFactory.getInstance(application))[DetailViewModel::class.java]
        detailViewModel.getBatikRandom(predictResult.id).observe(this) {
            if (it != null) {
                binding.ivDetailImage.setImageBitmap(predictResult.image)
                binding.tvDetailNameContent.text = it.name
                binding.tvDetailScoreContent.text = predictResult.confidence + "%"
                binding.tvDetailOriginContent.text = it.origin
                binding.tvDetailDescriptionContent.text = it.description
            }
        }
    }

    companion object {
        const val PREDICT_RESULT = "predict_result"
    }
}