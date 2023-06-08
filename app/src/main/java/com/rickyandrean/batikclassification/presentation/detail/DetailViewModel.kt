package com.rickyandrean.batikclassification.presentation.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.rickyandrean.batikclassification.database.Batik
import com.rickyandrean.batikclassification.repository.BatikRepository

class DetailViewModel(private val repository: BatikRepository) : ViewModel() {
    fun getBatikRandom(id: Int): LiveData<Batik?> = repository.getBatikDetail(id)
}