package com.rickyandrean.batikclassification.repository

import com.rickyandrean.batikclassification.model.PredictResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.io.File

class BatikRepository() {
    suspend fun predictBatik(
        image: File
    ): Flow<Result<PredictResponse>> = flow {
        try {
            // val requestImageFile = image.asRequestBody("image/jpeg".toMediaTypeOrNull())
            // val imageMultipart: MultipartBody.Part = MultipartBody.Part.createFormData("file", image.name, requestImageFile)
            val response = PredictResponse("Batik Kawung", "98%", null)
            emit(Result.success(response))
        } catch (e: Exception) {
            e.printStackTrace()
            emit(Result.failure(e))
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: BatikRepository? = null

        fun getInstance(): BatikRepository {
            return INSTANCE ?: synchronized(this) {
                if (INSTANCE == null) {
                    INSTANCE = BatikRepository()
                }
                return INSTANCE as BatikRepository
            }
        }
    }
}