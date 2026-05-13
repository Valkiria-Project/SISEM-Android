package com.skgtecnologia.sisem.commons.resources

import android.annotation.SuppressLint
import android.content.Context
import android.provider.Settings
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.File
import javax.inject.Inject
import javax.inject.Singleton

private const val FILE_NAME = "android_id"

@Singleton
class AndroidIdProviderImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : AndroidIdProvider {

    @Volatile
    private var cachedAndroidId: String? = null

    @SuppressLint("HardwareIds")
    override fun getAndroidId(): String {
        cachedAndroidId?.let { return it }
        return synchronized(this) {
            cachedAndroidId ?: readOrInitializeAndroidId().also { cachedAndroidId = it }
        }
    }

    @SuppressLint("HardwareIds")
    private fun readOrInitializeAndroidId(): String {
        File(context.filesDir, FILE_NAME).createNewFile()

        val storedId = context.openFileInput(FILE_NAME).use { input ->
            input.readBytes().toString(Charsets.UTF_8)
        }
        if (storedId.isNotEmpty()) return storedId

        val fallback = Settings.Secure
            .getString(context.contentResolver, Settings.Secure.ANDROID_ID)
            .orEmpty()

        context.openFileOutput(FILE_NAME, Context.MODE_PRIVATE).use { output ->
            output.write(fallback.toByteArray())
        }
        return fallback
    }
}
