package com.rickyandrean.batikclassification.presentation.preview

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.io.File

class PreviewViewModel() : ViewModel() {
    private val _image = MutableLiveData<File?>()
    val image: LiveData<File?> = _image

    fun setImage(img: File?) {
        _image.value = img
    }
}