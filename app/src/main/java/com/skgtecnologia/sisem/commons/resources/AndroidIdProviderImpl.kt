package com.skgtecnologia.sisem.commons.resources

import android.annotation.SuppressLint
import android.content.Context
import android.provider.Settings
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.File
import javax.inject.Inject

private const val FILE_NAME = "android_id"

class AndroidIdProviderImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : AndroidIdProvider {

    @SuppressLint("HardwareIds")
    override fun getAndroidId(): String {
        var androidId: String

        File(context.filesDir, FILE_NAME).createNewFile()

        context.openFileInput(FILE_NAME).use { input ->
            androidId = input.readBytes().toString(Charsets.UTF_8)
        }

        if (androidId.isEmpty()) {
            androidId = Settings.Secure
                .getString(context.contentResolver, Settings.Secure.ANDROID_ID)
                .orEmpty()

            context.openFileOutput(FILE_NAME, Context.MODE_PRIVATE).use { output ->
                output.write(androidId.toByteArray())
            }
        }

        return androidId
    }
}
