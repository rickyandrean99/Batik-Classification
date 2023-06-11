package com.rickyandrean.batikclassification.database

import android.content.Context
import androidx.room.*
import androidx.sqlite.db.SupportSQLiteDatabase
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.util.concurrent.Executors
import com.rickyandrean.batikclassification.R

@Database(entities = [Batik::class], version = 1)
abstract class BatikRoomDatabase : RoomDatabase() {
    abstract fun batikDao(): BatikDao

    companion object {
        @Volatile
        private var INSTANCE: BatikRoomDatabase? = null

        @JvmStatic
        fun getDatabase(context: Context): BatikRoomDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    BatikRoomDatabase::class.java,
                    "batik.db"
                )
                    .fallbackToDestructiveMigration()
                    .addCallback(object: Callback(){
                        override fun onCreate(db: SupportSQLiteDatabase) {
                            super.onCreate(db)

                            INSTANCE?.let {
                                Executors.newSingleThreadExecutor().execute {
                                    fillWithStartingData(context.applicationContext, it.batikDao())
                                }
                            }
                        }
                    }).build()

                INSTANCE = instance
                instance
            }
        }

        private fun fillWithStartingData(context: Context, dao: BatikDao) {
            val jsonArray = loadJsonArray(context)

            try {
                if (jsonArray != null) {
                    for (i in 0 until jsonArray.length()) {
                        val item = jsonArray.getJSONObject(i)
                        dao.insert(
                            Batik(
                                item.getInt("id"),
                                item.getString("name"),
                                item.getString("origin"),
                                item.getString("description")
                            )
                        )
                    }
                }
            } catch (exception: JSONException) {
                exception.printStackTrace()
            }
        }

        private fun loadJsonArray(context: Context): JSONArray? {
            val builder = StringBuilder()
            val `in` = context.resources.openRawResource(R.raw.batik)
            val reader = BufferedReader(InputStreamReader(`in`))
            var line: String?
            try {
                while (reader.readLine().also { line = it } != null) {
                    builder.append(line)
                }
                val json = JSONObject(builder.toString())
                return json.getJSONArray("batiks")
            } catch (exception: IOException) {
                exception.printStackTrace()
            } catch (exception: JSONException) {
                exception.printStackTrace()
            }
            return null
        }
    }
}