package com.rickyandrean.batikclassification.database

import android.os.Parcelable
import androidx.room.*
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "batiks")
data class Batik(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Int = 0,

    @ColumnInfo(name = "name")
    var name: String,

    @ColumnInfo(name = "origin")
    var origin: String,

    @ColumnInfo(name = "description")
    var description: String
): Parcelable