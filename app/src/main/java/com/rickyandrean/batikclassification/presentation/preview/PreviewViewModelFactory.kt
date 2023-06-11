package com.rickyandrean.batikclassification.presentation.preview

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.rickyandrean.batikclassification.repository.BatikRepository

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

        fun getInstance(application: Application): PreviewViewModelFactory =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: PreviewViewModelFactory()
            }
    }
}