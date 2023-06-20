package com.rickyandrean.batikclassification.model

import android.content.res.AssetFileDescriptor
import android.content.res.AssetManager
import android.graphics.Bitmap
import org.tensorflow.lite.DataType
import java.io.FileInputStream
import java.nio.MappedByteBuffer
import java.nio.channels.FileChannel
import org.tensorflow.lite.Interpreter
import org.tensorflow.lite.gpu.GpuDelegate
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer

class ImageClassifier(private val assetManager: AssetManager) {
    private var interpreter: Interpreter
    private var inputImageBuffer: TensorImage
    private var outputProbabilityBuffer: TensorBuffer
    private var gpuDelegate: GpuDelegate? = null

    init {
        val options = Interpreter.Options()
        gpuDelegate = GpuDelegate()
        options.addDelegate(gpuDelegate)

        val modelFileDescriptor: AssetFileDescriptor = assetManager.openFd("effnetb7_3.tflite")
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

        val inputDataType = interpreter.getInputTensor(0).dataType()
        inputImageBuffer = TensorImage(inputDataType)
        outputProbabilityBuffer = TensorBuffer.createFixedSize(intArrayOf(1, 10), DataType.FLOAT32)
    }

    fun classifyImage(bitmap: Bitmap): FloatArray {
        inputImageBuffer.load(bitmap)
        interpreter.run(inputImageBuffer.buffer, outputProbabilityBuffer.buffer)
        return outputProbabilityBuffer.floatArray
    }
}