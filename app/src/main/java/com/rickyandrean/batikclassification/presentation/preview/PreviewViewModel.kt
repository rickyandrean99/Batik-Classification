package com.rickyandrean.batikclassification.presentation.preview

import android.graphics.Bitmap
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class PreviewViewModel : ViewModel() {
    private val _image = MutableLiveData<Bitmap?>()
    private val _ready = MutableLiveData<Boolean>()
    val image: LiveData<Bitmap?> = _image
    val ready: LiveData<Boolean> = _ready

    init {
        _image.value = null
        _ready.value = false
    }

    fun setImage(img: Bitmap?) {
        _image.value = img
    }

    fun setReady() {
        _ready.value = true
    }
}