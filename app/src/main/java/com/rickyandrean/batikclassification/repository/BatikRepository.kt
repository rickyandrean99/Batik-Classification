package com.rickyandrean.batikclassification.repository

import android.app.Application
import androidx.lifecycle.LiveData
import com.rickyandrean.batikclassification.database.Batik
import com.rickyandrean.batikclassification.database.BatikDao
import com.rickyandrean.batikclassification.database.BatikRoomDatabase
import com.rickyandrean.batikclassification.model.PredictResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.io.File

class BatikRepository(application: Application) {
    private val mBatikDao: BatikDao

    init {
        val db = BatikRoomDatabase.getDatabase(application)
        mBatikDao = db.batikDao()
    }

    fun getBatikDetail(id: Int): LiveData<Batik?> = mBatikDao.getBatikDetail(id)

    // TODO: Model Classification
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

        fun getInstance(application: Application): BatikRepository {
            return INSTANCE ?: synchronized(this) {
                if (INSTANCE == null) {
                    INSTANCE = BatikRepository(application)
                }
                return INSTANCE as BatikRepository
            }
        }
    }
}