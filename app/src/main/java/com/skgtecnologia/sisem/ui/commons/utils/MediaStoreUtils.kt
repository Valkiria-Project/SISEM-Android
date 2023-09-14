package com.skgtecnologia.sisem.ui.commons.utils

import android.content.ContentUris
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.provider.MediaStore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File

class MediaStoreUtils(private val context: Context) {

    val mediaStoreCollection: Uri? =
        MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL)

    private suspend fun getMediaStoreImageCursor(mediaStoreCollection: Uri): Cursor? {
        var cursor: Cursor?
        withContext(Dispatchers.IO) {
            val projection = arrayOf(IMAGE_DATA_COLUMN_INDEX, IMAGE_ID_COLUMN_INDEX)
            val sortOrder = "DATE_ADDED DESC"
            cursor = context.contentResolver.query(
                mediaStoreCollection, projection, null, null, sortOrder
            )
        }
        return cursor
    }

    @Suppress("ReturnCount")
    suspend fun getLatestImageFilename(): String? {
        var filename: String?
        if (mediaStoreCollection == null) return null

        getMediaStoreImageCursor(mediaStoreCollection).use { cursor ->
            if (cursor?.moveToFirst() != true) return null
            filename = cursor.getString(cursor.getColumnIndexOrThrow(IMAGE_DATA_COLUMN_INDEX))
        }

        return filename
    }

    suspend fun getImages(): MutableList<MediaStoreFile> {
        val files = mutableListOf<MediaStoreFile>()
        if (mediaStoreCollection == null) return files

        getMediaStoreImageCursor(mediaStoreCollection).use { cursor ->
            val imageDataColumn = cursor?.getColumnIndexOrThrow(IMAGE_DATA_COLUMN_INDEX)
            val imageIdColumn = cursor?.getColumnIndexOrThrow(IMAGE_ID_COLUMN_INDEX)

            if (cursor != null && imageDataColumn != null && imageIdColumn != null) {
                while (cursor.moveToNext()) {
                    val id = cursor.getLong(imageIdColumn)
                    val contentUri: Uri = ContentUris.withAppendedId(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        id
                    )
                    val contentFile = File(cursor.getString(imageDataColumn))
                    files.add(MediaStoreFile(contentUri, contentFile, id))
                }
            }
        }

        return files
    }

    companion object {
        // Suppress DATA index deprecation warning since we need the file location for the Glide library
        private const val IMAGE_DATA_COLUMN_INDEX = MediaStore.Images.Media.DATA
        private const val IMAGE_ID_COLUMN_INDEX = MediaStore.Images.Media._ID
    }
}

data class MediaStoreFile(val uri: Uri, val file: File, val id: Long)
