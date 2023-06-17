package com.rickyandrean.batikclassification.presentation.preprocess

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.rickyandrean.batikclassification.R
import com.yalantis.ucrop.UCrop
import java.io.File
import java.lang.StringBuilder
import java.util.*

class PreprocessActivity : AppCompatActivity() {
    private lateinit var sourceUri: String
    private lateinit var destinationUri: String
    private lateinit var uri: Uri

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_preprocess)

        if (intent.extras != null) {
            sourceUri = intent.getStringExtra("image") as String
            uri = Uri.parse(sourceUri)
        }

        destinationUri = StringBuilder(UUID.randomUUID().toString()).append(".jpg").toString()

        UCrop.of(uri, Uri.fromFile(File(cacheDir, destinationUri)))
            .withOptions(UCrop.Options())
            .withAspectRatio(1F, 1F)
            .withMaxResultSize(600, 600)
            .start(this)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        val intent = Intent()
        if (resultCode == RESULT_OK && requestCode == UCrop.REQUEST_CROP) {
            intent.putExtra("crop", "${UCrop.getOutput(data!!)!!}")
            setResult(101, intent)
        } else if (resultCode == UCrop.RESULT_ERROR) {
            setResult(102, intent)
        }
        finish()
    }
}