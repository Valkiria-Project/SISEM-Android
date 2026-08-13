package com.skgtecnologia.sisem.data.biometric.remote

import android.util.Base64
import java.nio.ByteBuffer
import java.nio.ByteOrder

object BiometricSerializer {

    fun floatArrayToBase64(floats: FloatArray): String {
        val buffer = ByteBuffer
            .allocate(floats.size * Float.SIZE_BYTES)
            .apply { order(ByteOrder.nativeOrder()) }
        floats.forEach { buffer.putFloat(it) }
        return Base64.encodeToString(buffer.array(), Base64.NO_WRAP)
    }

    fun base64ToFloatArray(base64: String): FloatArray {
        val bytes = Base64.decode(base64, Base64.NO_WRAP)
        val buffer = ByteBuffer.wrap(bytes).apply { order(ByteOrder.nativeOrder()) }
        return FloatArray(bytes.size / Float.SIZE_BYTES) { buffer.getFloat() }
    }
}
