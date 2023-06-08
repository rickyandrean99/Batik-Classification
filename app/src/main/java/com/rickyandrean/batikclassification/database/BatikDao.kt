package com.rickyandrean.batikclassification.database

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface BatikDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(batik: Batik)

    @Query("SELECT * FROM batiks WHERE id = :id")
    fun getBatikDetail(id: Int): LiveData<Batik>
}