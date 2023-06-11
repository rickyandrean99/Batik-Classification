package com.rickyandrean.batikclassification.presentation.preview

import android.graphics.Bitmap
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class PreviewViewModel : ViewModel() {
    private val _image = MutableLiveData<Bitmap?>()
    val image: LiveData<Bitmap?> = _image

    init {
        _image.value = null
    }

    fun setImage(img: Bitmap?) {
        _image.value = img
    }
}