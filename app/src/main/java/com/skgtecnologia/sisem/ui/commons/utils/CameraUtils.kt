package com.skgtecnologia.sisem.ui.commons.utils

import android.content.ContentValues
import android.content.Context
import android.provider.MediaStore
import androidx.camera.core.ImageCapture
import com.skgtecnologia.sisem.R
import com.skgtecnologia.sisem.ui.commons.constants.MULTI_PART_FORM_DATA
import java.text.SimpleDateFormat
import java.util.Locale

private const val FILENAME = "yyyy-MM-dd-HH-mm-ss-SSS"

class CameraUtils(private val context: Context) {

    private val name = SimpleDateFormat(FILENAME, Locale.US).format(System.currentTimeMillis())

    fun getOutputOptions() = ImageCapture.OutputFileOptions.Builder(
        context.contentResolver,
        MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
        getContentValues()
    ).build()

    private fun getContentValues() = ContentValues().apply {
        put(MediaStore.MediaColumns.DISPLAY_NAME, name)
        put(MediaStore.MediaColumns.MIME_TYPE, MULTI_PART_FORM_DATA)
        val appName = context.resources.getString(R.string.app_name)
        put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/$appName")
    }
}
