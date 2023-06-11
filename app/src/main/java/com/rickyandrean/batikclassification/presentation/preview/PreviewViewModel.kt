package com.rickyandrean.batikclassification.presentation.preview

import android.graphics.Bitmap
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.rickyandrean.batikclassification.database.Batik
import com.rickyandrean.batikclassification.model.PredictResponse
import com.rickyandrean.batikclassification.repository.BatikRepository
import kotlinx.coroutines.flow.Flow
import java.io.File

class PreviewViewModel(private val repository: BatikRepository) : ViewModel() {
    private val _image = MutableLiveData<Bitmap?>()
    val image: LiveData<Bitmap?> = _image

    init {
        _image.value = null
    }

    fun setImage(img: Bitmap?) {
        _image.value = img
    }
}