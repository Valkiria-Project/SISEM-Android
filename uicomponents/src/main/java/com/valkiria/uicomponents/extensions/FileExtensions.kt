@file:Suppress("SwallowedException")

package com.valkiria.uicomponents.extensions

import android.content.ContentResolver
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import android.net.Uri
import android.provider.OpenableColumns
import android.util.Base64
import id.zelory.compressor.Compressor
import id.zelory.compressor.constraint.size
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream

const val CONTENT_URI_SCHEME = "content"
const val BITMAP_COMPRESS_QUALITY = 80
const val ONE_MB_DECIMAL = 1_000_00L
const val ONE_MB_BINARY = 1_048_576L
const val FALLBACK_MB_BINARY_FILE_SIZE = 2_097_152L

fun Bitmap.encodeAsBase64(): String {
    val output = ByteArrayOutputStream()
    compress(Bitmap.CompressFormat.JPEG, BITMAP_COMPRESS_QUALITY, output)
    val bytes = output.toByteArray()
    return Base64.encodeToString(bytes, Base64.DEFAULT)
}

fun String.decodeAsBase64Bitmap(): Bitmap {
    // FIXME: Temporal hack substringAfter
    val bytes = Base64.decode(this.substringAfter(","), Base64.DEFAULT)
    return BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
}

fun Uri.decodeAsBitmap(contentResolver: ContentResolver): Bitmap {
    val source = ImageDecoder.createSource(contentResolver, this)
    return ImageDecoder.decodeBitmap(source)
}

suspend fun Context.storeUriAsFileToCache(uri: Uri, maxFileSizeKb: String?): File {
    val fileContents = try {
        contentResolver.openInputStream(uri)
    } catch (e: FileNotFoundException) {
        null
    }

    val file = when (uri.scheme) {
        CONTENT_URI_SCHEME -> File(cacheDir, contentResolver.getFileName(uri))
        else -> File(cacheDir, uri.lastPathSegment!!)
    }

    withContext(Dispatchers.IO) {
        FileOutputStream(file).use {
            it.write(fileContents?.readBytes())
        }

        fileContents?.close()
    }

    return Compressor.compress(context = this, imageFile = file) {
        size(getImageAllowedSize(maxFileSizeKb))
    }
}

private fun getImageAllowedSize(maxFileSizeKb: String?): Long {
    val fileSize = maxFileSizeKb.orEmpty().toLongOrNull()

    return if (fileSize != null) {
        ONE_MB_BINARY.times(fileSize / ONE_MB_DECIMAL)
    } else {
        FALLBACK_MB_BINARY_FILE_SIZE
    }
}

private fun ContentResolver.getFileName(fileUri: Uri): String {
    val returnCursor = query(fileUri, null, null, null, null)
    return buildString {
        if (returnCursor != null) {
            val nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
            returnCursor.moveToFirst()
            append(returnCursor.getString(nameIndex))
            returnCursor.close()
        }
    }
}
