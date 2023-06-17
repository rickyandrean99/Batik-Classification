package com.rickyandrean.batikclassification.model

import android.content.res.AssetFileDescriptor
import android.content.res.AssetManager
import android.util.Log
import org.tensorflow.lite.DataType
import java.io.FileInputStream
import java.nio.MappedByteBuffer
import java.nio.channels.FileChannel
import org.tensorflow.lite.Interpreter
import org.tensorflow.lite.gpu.GpuDelegate
import org.tensorflow.lite.nnapi.NnApiDelegate
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer
import java.io.File
import java.nio.ByteBuffer
import java.nio.ByteOrder

class ImageClassifier(private val assetManager: AssetManager) {
//    private lateinit var interpreter: Interpreter
//
//    fun initializeModel() {
//        val options = Interpreter.Options()
//        val delegate = GpuDelegate()
//        options.addDelegate(delegate)
//        interpreter = Interpreter(loadModelFile(), options)
//        interpreter = Interpreter(loadModelFile())
//    }

//    private fun loadModelFile(): MappedByteBuffer {
//        val fileDescriptor = assetManager.openFd("model.tflite")
//        val inputStream = FileInputStream(fileDescriptor.fileDescriptor)
//        val fileChannel = inputStream.channel
//        val startOffset = fileDescriptor.startOffset
//        val declaredLength = fileDescriptor.declaredLength
//
//        Log.d("test", fileDescriptor.toString())
//        Log.d("test", inputStream.toString())
//        Log.d("test", fileChannel.toString())
//        Log.d("test", startOffset.toString())
//        Log.d("test", declaredLength.toString())
//
//        return fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength)
//    }
//
//    fun classify(input: FloatArray): FloatArray {
//        val output = Array(1) { FloatArray(NUM_CLASSES) }
//        interpreter.run(input, output)
//        return output[0]
//    }
//
//    companion object {
//        private const val NUM_CLASSES = 10
//    }

    private lateinit var interpreter: Interpreter
    private lateinit var inputImageBuffer: TensorImage
    private lateinit var outputProbabilityBuffer: TensorBuffer
    private var gpuDelegate: GpuDelegate? = null
//    private var nnApiDelegate: NnApiDelegate? = null

    init {
        val options = Interpreter.Options()
        gpuDelegate = GpuDelegate()
        options.addDelegate(gpuDelegate)

//        nnApiDelegate = NnApiDelegate()
//        options.addDelegate(nnApiDelegate)


        val modelFileDescriptor: AssetFileDescriptor = assetManager.openFd("model.tflite")
        val modelInputStream = FileInputStream(modelFileDescriptor.fileDescriptor)
        val fileChannel: FileChannel = modelInputStream.channel
        val startOffset = modelFileDescriptor.startOffset
        val declaredLength = modelFileDescriptor.declaredLength
        val mappedByteBuffer: MappedByteBuffer = fileChannel.map(
            FileChannel.MapMode.READ_ONLY,
            startOffset,
            declaredLength
        )

        interpreter = Interpreter(mappedByteBuffer, options)

//        val inputShape = interpreter.getInputTensor(0).shape()
//        val inputDataType = interpreter.getInputTensor(0).dataType()
//
//        val inputSize = inputShape[1]
//        val inputChannels = inputShape[3]
//
//        inputImageBuffer = TensorImage(inputDataType)
//        outputProbabilityBuffer = TensorBuffer.createFixedSize(intArrayOf(1, 3), DataType.FLOAT32)
    }
}