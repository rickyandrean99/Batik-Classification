package com.rickyandrean.batikclassification.presentation.preview

import android.Manifest
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.ViewModelProvider
import com.rickyandrean.batikclassification.R
import com.rickyandrean.batikclassification.databinding.ActivityPreviewBinding
import com.rickyandrean.batikclassification.ml.Batik
import com.rickyandrean.batikclassification.model.PredictResponse
import com.rickyandrean.batikclassification.presentation.camera.CameraActivity
import com.rickyandrean.batikclassification.presentation.detail.DetailActivity
import com.rickyandrean.batikclassification.presentation.preprocess.PreprocessActivity
import org.tensorflow.lite.DataType
import org.tensorflow.lite.support.common.ops.NormalizeOp
import org.tensorflow.lite.support.image.ImageProcessor
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.support.image.ops.ResizeOp
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer

class PreviewActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPreviewBinding
    private lateinit var previewViewModel: PreviewViewModel
    private val labels = listOf("Cendrawasih", "Geblek Renteng", "Insang", "Kawung", "Mega Mendung", "Parang", "Pring Sdapur", "Simbut", "Sogan", "Truntum")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPreviewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        previewViewModel = ViewModelProvider(this, PreviewViewModelFactory.getInstance(application))[PreviewViewModel::class.java]

        setupView()
        setListener()
        setupObserver()
    }

    private fun setupView() {
        supportActionBar?.hide()
    }

    private fun setListener() {
        binding.btnAddRemovePhoto.setOnClickListener {
            if (previewViewModel.image.value == null) {
                val intent = Intent(this@PreviewActivity, CameraActivity::class.java)
                launcherIntentCameraX.launch(intent)
            } else {
                previewViewModel.setImage(null)
            }
        }

        previewViewModel.image.observe(this) {
            if (it != null) {
                binding.btnPreviewSubmit.isEnabled = true
                binding.btnPreviewSubmit.setBackgroundResource(R.drawable.bg_sapphire)
                binding.btnAddRemovePhoto.text = resources.getString(R.string.remove_photo)
                binding.btnAddRemovePhoto.setBackgroundResource(R.drawable.bg_light_red)
            } else {
                binding.btnPreviewSubmit.isEnabled = false
                binding.btnPreviewSubmit.setBackgroundResource(R.drawable.bg_light_grey)
                binding.btnAddRemovePhoto.text = resources.getString(R.string.add_photo)
                binding.btnAddRemovePhoto.setBackgroundResource(R.drawable.bg_sapphire)
            }
        }

        binding.btnPreviewSubmit.setOnClickListener {
            if (previewViewModel.image.value != null) {
                 classifyImage()
            }
        }
    }

    private fun setupObserver() {
        previewViewModel.image.observe(this) {
            if (it != null) {
                binding.ivImagePreview.setImageBitmap(it)
                binding.tvImagePreview.visibility = View.INVISIBLE
            } else {
                binding.ivImagePreview.setImageResource(R.drawable.dotted)
                binding.tvImagePreview.visibility = View.VISIBLE
            }
        }
    }

    private val launcherIntentCameraX = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == CAMERA_X_RESULT) {
            val result = it.data?.getStringExtra("picture")
            if (result != null) {
                val image = Uri.parse(result)
                val intent = Intent(this@PreviewActivity, PreprocessActivity::class.java)
                intent.putExtra("image", image.toString())
                launcherCropImage.launch(intent)
            }
        }
    }

    private val launcherCropImage = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == CROP_RESULT) {
            val result = it.data?.getStringExtra("crop")
            if (result != null) {
                val uri = Uri.parse(result)
                val bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, uri)
                previewViewModel.setImage(bitmap)
            }
        }
    }

    companion object {
        const val CAMERA_X_RESULT = 200
        const val CROP_RESULT = 101
        val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
        const val REQUEST_CODE_PERMISSIONS = 10
    }

    private fun classifyImage() {
        // Image preprocessing
        val preprocessing = ImageProcessor.Builder()
            .add(NormalizeOp(0.0f, 1.0f))
            .add(ResizeOp(224, 224, ResizeOp.ResizeMethod.BILINEAR))
            .build()
        val inputImage = TensorImage(DataType.FLOAT32).apply {
            load(previewViewModel.image.value)
        }.run {
            preprocessing.process(this)
        }

        // Create batik model, prepare input, output and do process step
        val model = Batik.newInstance(applicationContext)
        val input = TensorBuffer.createFixedSize(intArrayOf(1, 224, 224, 3), DataType.FLOAT32).apply {
            loadBuffer(inputImage.buffer)
        }
        val process = model.process(input)
        val output = process.outputFeature0AsTensorBuffer.floatArray

        // Get the result
        var bestIndex = 0
        var confidenceScore = 0.00f

        output.forEachIndexed { index, fl ->
            if (output[bestIndex] < fl) {
                bestIndex = index
                confidenceScore = fl
            }

            Log.d("classification_result", fl.toString())
        }

        Log.d("classification_result", labels[bestIndex])
        Log.d("classification_result", confidenceScore.toString())

        model.close()

        val predictResult = PredictResponse(bestIndex+1, (confidenceScore*100).toString(), previewViewModel.image.value!!)
        val intent = Intent(this@PreviewActivity, DetailActivity::class.java)
        intent.putExtra(DetailActivity.PREDICT_RESULT, predictResult)
        startActivity(intent)
    }
}