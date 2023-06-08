package com.rickyandrean.batikclassification.presentation.preview

import android.Manifest
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.ViewModelProvider
import com.rickyandrean.batikclassification.R
import com.rickyandrean.batikclassification.databinding.ActivityPreviewBinding
import com.rickyandrean.batikclassification.helper.uriToFile
import com.rickyandrean.batikclassification.presentation.camera.CameraActivity
import com.rickyandrean.batikclassification.presentation.detail.DetailActivity
import com.rickyandrean.batikclassification.presentation.preprocess.PreprocessActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

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
            CoroutineScope(Dispatchers.Main).launch {
                if (previewViewModel.image.value != null) {
                    previewViewModel.classifyImage().collect { response ->
                        response.onSuccess {
                            it.image = previewViewModel.image.value!!
                            Toast.makeText(this@PreviewActivity, "Gambar batik berhasil diklasifikasikan!", Toast.LENGTH_SHORT).show()

                            val intent = Intent(this@PreviewActivity, DetailActivity::class.java)
//                            intent.putExtra(DetailActivity.PREDICT_RESPONSE, it)
//                            intent.putExtra(DetailActivity.PREDICT_IMAGE, previewViewModel.image.value!!)
                            startActivity(intent)
                        }

                        response.onFailure {
                            Toast.makeText(this@PreviewActivity, "Terjadi kesalahan pada aplikasi", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }
    }

    private fun setupObserver() {
        previewViewModel.image.observe(this) {
            if (it != null) {
                binding.ivImagePreview.setImageBitmap(BitmapFactory.decodeFile(it?.path))
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
                val file = uriToFile(uri!!, this)
                previewViewModel.setImage(file)
            }
        }
    }

    companion object {
        const val CAMERA_X_RESULT = 200
        const val CROP_RESULT = 101
        val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
        const val REQUEST_CODE_PERMISSIONS = 10
    }
}