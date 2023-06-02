package com.rickyandrean.batikclassification.presentation.preview

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.rickyandrean.batikclassification.model.PredictResponse
import com.rickyandrean.batikclassification.repository.BatikRepository
import kotlinx.coroutines.flow.Flow
import java.io.File

class PreviewViewModel(private val repository: BatikRepository) : ViewModel() {
    private val _image = MutableLiveData<File?>()
    val image: LiveData<File?> = _image

    init {
        _image.value = null
    }

    fun setImage(img: File?) {
        _image.value = img
    }

    suspend fun classifyImage(): Flow<Result<PredictResponse>> =
        repository.predictBatik(image.value!!)
}