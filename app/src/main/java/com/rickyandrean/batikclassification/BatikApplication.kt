package com.rickyandrean.batikclassification

import android.app.Application
import com.rickyandrean.batikclassification.database.BatikRoomDatabase
import com.rickyandrean.batikclassification.repository.BatikRepository

class BatikApplication: Application() {
    val database by lazy { BatikRoomDatabase.getDatabase(this) }
    val repository by lazy { BatikRepository(database.batikDao()) }
}