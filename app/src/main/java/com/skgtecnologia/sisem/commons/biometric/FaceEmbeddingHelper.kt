package com.skgtecnologia.sisem.commons.biometric

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.Matrix
import com.google.mlkit.vision.face.Face
import dagger.hilt.android.qualifiers.ApplicationContext
import org.tensorflow.lite.Interpreter
import org.tensorflow.lite.support.common.FileUtil
import timber.log.Timber
import java.nio.ByteBuffer
import java.nio.ByteOrder
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.math.sqrt

private const val MODEL_FILE = "facenet_512.tflite"
private const val INPUT_SIZE = 160
private const val EMBEDDING_SIZE = 512
private const val CHANNELS = 3
private const val FLOAT_BYTES = 4
private const val FACE_PADDING_FACTOR = 0.15f
private const val NUM_THREADS = 2
private const val NORM_MEAN = 127.5f
private const val NORM_STD = 128.0f
private const val LOG_SAMPLE = 6

/**
 * On-device face embedding extractor backed by a FaceNet 512 TFLite model.
 *
 * Pipeline:
 *  1. Crop the face region (+ 15 % padding) from the supplied bitmap.
 *  2. Resize to 160 × 160 px.
 *  3. Normalize each channel: (pixel − 127.5) / 128 → [−1, 1].
 *  4. Run FaceNet inference → 512-dim float vector.
 *  5. L2-normalize the vector.
 *
 * Embeddings are compared via cosine similarity (threshold: 0.70).
 * The bitmap supplied to [extractEmbedding] must already be rotated to display
 * orientation so its coordinate space matches ML Kit's bounding boxes.
 *
 * Model file: place `facenet.tflite` in `app/src/main/assets/`.
 */
@Singleton
class FaceEmbeddingHelper @Inject constructor(
    @ApplicationContext private val context: Context
) {

    companion object {
        /** Minimum cosine similarity to accept a match. */
        const val MATCH_THRESHOLD = 0.70f

        /** Face bounding-box must be wider than this (in camera-image pixels) before capture. */
        const val MIN_FACE_PX = 100

        private const val EPSILON = 1e-8f

        /** Cosine similarity in [−1, 1]; 1 = identical direction. */
        fun similarity(a: FloatArray, b: FloatArray): Float {
            if (a.size != b.size) return 0f
            var dot = 0f
            var normA = 0f
            var normB = 0f
            for (j in a.indices) {
                dot += a[j] * b[j]
                normA += a[j] * a[j]
                normB += b[j] * b[j]
            }
            val denom = sqrt(normA) * sqrt(normB)
            return if (denom < EPSILON) 0f else dot / denom
        }
    }

    private val interpreter: Interpreter? by lazy {
        runCatching {
            val model = FileUtil.loadMappedFile(context, MODEL_FILE)
            Interpreter(model, Interpreter.Options().apply { setNumThreads(NUM_THREADS) })
        }.onFailure {
            Timber.e(it, "[FaceEmbed] Failed to load $MODEL_FILE — place it in app/src/main/assets/")
        }.getOrNull()
    }

    /**
     * Returns a 512-dim L2-normalized FaceNet embedding, or null if the model is
     * not loaded or the face crop is invalid.
     */
    @Suppress("ReturnCount")
    fun extractEmbedding(face: Face, bitmap: Bitmap): FloatArray? {
        val interp = interpreter ?: return null
        val cropped = cropFace(bitmap, face) ?: return null
        val resized = Bitmap.createScaledBitmap(cropped, INPUT_SIZE, INPUT_SIZE, true)
        val input = preprocessBitmap(resized)
        val output = Array(1) { FloatArray(EMBEDDING_SIZE) }
        runCatching { interp.run(input, output) }.onFailure {
            Timber.e(it, "[FaceEmbed] TFLite inference failed")
            return null
        }
        val embedding = normalize(output[0])
        Timber.d(
            "[FaceEmbed] FaceNet size=${embedding.size} " +
                "sample=${embedding.take(LOG_SAMPLE).joinToString { "%.4f".format(it) }}"
        )
        return embedding
    }

    private fun cropFace(bitmap: Bitmap, face: Face): Bitmap? {
        val box = face.boundingBox
        val pad = (box.width() * FACE_PADDING_FACTOR).toInt()
        val left = (box.left - pad).coerceAtLeast(0)
        val top = (box.top - pad).coerceAtLeast(0)
        val right = (box.right + pad).coerceAtMost(bitmap.width)
        val bottom = (box.bottom + pad).coerceAtMost(bitmap.height)
        if (left >= right || top >= bottom) return null
        return Bitmap.createBitmap(bitmap, left, top, right - left, bottom - top)
    }

    @Suppress("MagicNumber")
    private fun preprocessBitmap(bitmap: Bitmap): ByteBuffer {
        val buffer = ByteBuffer
            .allocateDirect(INPUT_SIZE * INPUT_SIZE * CHANNELS * FLOAT_BYTES)
            .apply { order(ByteOrder.nativeOrder()) }
        for (y in 0 until INPUT_SIZE) {
            for (x in 0 until INPUT_SIZE) {
                val pixel = bitmap.getPixel(x, y)
                buffer.putFloat((Color.red(pixel) - NORM_MEAN) / NORM_STD)
                buffer.putFloat((Color.green(pixel) - NORM_MEAN) / NORM_STD)
                buffer.putFloat((Color.blue(pixel) - NORM_MEAN) / NORM_STD)
            }
        }
        buffer.rewind()
        return buffer
    }

    private fun normalize(v: FloatArray): FloatArray {
        val norm = sqrt(v.map { it * it }.sum()).coerceAtLeast(EPSILON)
        return FloatArray(v.size) { v[it] / norm }
    }
}

/** Rotates this bitmap [degrees] clockwise, returning the same instance if degrees == 0. */
fun Bitmap.rotateTo(degrees: Int): Bitmap {
    if (degrees == 0) return this
    val matrix = Matrix().apply { postRotate(degrees.toFloat()) }
    return Bitmap.createBitmap(this, 0, 0, width, height, matrix, false)
}
