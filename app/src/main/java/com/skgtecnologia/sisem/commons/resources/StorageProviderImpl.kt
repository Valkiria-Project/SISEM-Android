package com.skgtecnologia.sisem.commons.resources

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.File
import javax.inject.Inject

const val ANDROID_NETWORKING_FILE_NAME = "android_networking"

class StorageProviderImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : StorageProvider {

    override fun storeContent(filename: String, mode: Int, content: ByteArray) {
        File(context.filesDir, filename).createNewFile()

        context.openFileOutput(filename, mode).use { output ->
            output.write(content)
        }
    }
}
