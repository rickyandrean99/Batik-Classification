package com.rickyandrean.batikclassification.presentation.preview

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class PreviewViewModelFactory private constructor() :
    ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T =
        when {
            modelClass.isAssignableFrom(PreviewViewModel::class.java) -> {
                PreviewViewModel() as T
            }
            else -> throw Throwable("Unknown ViewModel class: " + modelClass.name)
        }

    companion object {
        @Volatile
        private var INSTANCE: PreviewViewModelFactory? = null

        fun getInstance(): PreviewViewModelFactory =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: PreviewViewModelFactory()
            }
    }
}