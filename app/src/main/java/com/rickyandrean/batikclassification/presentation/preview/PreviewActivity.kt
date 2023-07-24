package com.rickyandrean.batikclassification.presentation.preview

import android.Manifest
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.rickyandrean.batikclassification.R
import com.rickyandrean.batikclassification.databinding.ActivityPreviewBinding
import com.rickyandrean.batikclassification.ml.Batik
import com.rickyandrean.batikclassification.model.PredictResponse
import com.rickyandrean.batikclassification.presentation.camera.CameraActivity
import com.rickyandrean.batikclassification.presentation.detail.DetailActivity
import com.rickyandrean.batikclassification.presentation.preprocess.PreprocessActivity
import org.tensorflow.lite.DataType
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer
import java.nio.ByteBuffer
import java.nio.ByteOrder

class PreviewActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPreviewBinding
    private lateinit var previewViewModel: PreviewViewModel

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
                 classifyImage(previewViewModel.image.value!!)
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

    private fun classifyImage(image: Bitmap) {
        // Preprocess the image (rescaling)
        val byteBuffer: ByteBuffer = ByteBuffer.allocateDirect(4 * 224 * 224 * 3)
        byteBuffer.order(ByteOrder.nativeOrder())
        val intValues = IntArray(224 * 224)
        image.getPixels(intValues, 0, image.width, 0, 0, image.width, image.height)
        var pixel = 0

        for (i in 0 until 224) {
            for (j in 0 until 224) {
                val `val` = intValues[pixel++] // RGB
                byteBuffer.putFloat((`val` shr 16 and 0xFF) * (1f / 255))
                byteBuffer.putFloat((`val` shr 8 and 0xFF) * (1f / 255))
                byteBuffer.putFloat((`val` and 0xFF) * (1f / 255))
            }
        }

        // Create batik model, prepare input, do classification and get the output
        val model = Batik.newInstance(applicationContext)
        val input = TensorBuffer.createFixedSize(intArrayOf(1, 224, 224, 3), DataType.FLOAT32)
        input.loadBuffer(byteBuffer)

        val process = model.process(input)
        val output = process.outputFeature0AsTensorBuffer.floatArray
        model.close()

        // Get The Result
        var bestIndex = 0
        var bestConfidenceScore = 0.00f

        output.forEachIndexed { index, fl ->
            if (fl >= output[bestIndex]) {
                bestIndex = index
                bestConfidenceScore = fl
            }
        }

        val confidenceScore = bestConfidenceScore * 100

        if (confidenceScore >= 90.0F) {
            val predictResult = PredictResponse(bestIndex+1, String.format("%.2f", confidenceScore), previewViewModel.image.value!!)
            val intent = Intent(this@PreviewActivity, DetailActivity::class.java)
            intent.putExtra(DetailActivity.PREDICT_RESULT, predictResult)
            startActivity(intent)
        } else {
            Log.d("prediction", "Score hanya " + String.format("%.2f", confidenceScore) + "%")
            Toast.makeText(this, "Maaf, bukan batik!\n(Score: " + String.format("%.2f", confidenceScore) + "%)", Toast.LENGTH_LONG).show()
        }
    }
}