package com.rickyandrean.batikclassification.presentation.preview

import android.Manifest
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.ViewModelProvider
import com.rickyandrean.batikclassification.R
import com.rickyandrean.batikclassification.databinding.ActivityPreviewBinding
import com.rickyandrean.batikclassification.helper.uriToFile
import com.rickyandrean.batikclassification.presentation.camera.CameraActivity
import com.rickyandrean.batikclassification.presentation.preprocess.PreprocessActivity
import kotlinx.coroutines.launch

class PreviewActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPreviewBinding
    private lateinit var previewViewModel: PreviewViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPreviewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        previewViewModel = ViewModelProvider(this, PreviewViewModelFactory.getInstance())[PreviewViewModel::class.java]

        setupView()
        setListener()
        setupObserver()
    }

    private fun setupView() {
        supportActionBar?.hide()
        binding.btnAddRemovePhoto.setBackgroundResource(R.drawable.bg_sapphire)
        binding.btnPreviewSubmit.setBackgroundResource(R.drawable.bg_sapphire)
    }

    private fun setListener() {
        binding.btnAddRemovePhoto.setOnClickListener {
            val intent = Intent(this@PreviewActivity, CameraActivity::class.java)
            startActivity(intent)
        }

//        with(binding.imagePicker) {
//            ibAdd.setOnClickListener {
//                val intent = Intent(requireActivity(), CameraActivity::class.java)
//                launcherIntentCameraX.launch(intent)
//            }
//            ibDelete.setOnClickListener {
//                scanViewModel.setImage(null)
//            }
//        }

//        binding.btnSubmit.setOnClickListener {
//            lifecycleScope.launchWhenStarted {
//                launch {
//                    if (scanViewModel.image.value != null) {
//                        setupSubmitAction(false)
//                        setupLoadingAnimation(true)
//
//                        scanViewModel.uploadImage().collect { response ->
//                            response.onSuccess {
//                                it.image = scanViewModel.image.value!!
//
//                                val intent = Intent(requireActivity(), PredictActivity::class.java)
//                                intent.putExtra(PredictActivity.PREDICT_RESPONSE, it)
//                                intent.putExtra(PredictActivity.PREDICT_IMAGE, scanViewModel.image.value!!)
//                                startActivity(intent)
//                            }
//
//                            response.onFailure { error ->
//                                setupSubmitAction(true)
//                                setupLoadingAnimation(false)
//                                Toast.makeText(requireActivity(), "Terjadi kesalahan pada aplikasi", Toast.LENGTH_SHORT).show()
//                                Log.e("debugging_error", error.message.toString())
//                            }
//                        }
//                    } else {
//                        Toast.makeText(requireActivity(), "Silahkan unggah gambar!", Toast.LENGTH_SHORT).show()
//                    }
//                }
//            }
//        }
    }

    private fun setupObserver() {
        previewViewModel.image.observe(this) {
//            if (it != null) {
//                binding.imagePicker.ivImagePreview.setImageBitmap(BitmapFactory.decodeFile(it?.path))
//                binding.imagePicker.previewText.visibility = View.INVISIBLE
//            } else {
//                binding.imagePicker.ivImagePreview.setImageResource(R.drawable.dotted)
//                binding.imagePicker.previewText.visibility = View.VISIBLE
//            }
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