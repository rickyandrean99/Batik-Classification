package com.rickyandrean.batikclassification.model

import java.io.File

data class PredictResponse (
    val name: String,
    val confidence: String,
    var image: File?
)