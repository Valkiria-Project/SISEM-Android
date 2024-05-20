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
import androidx.core.net.toUri
import com.valkiria.uicomponents.components.media.MediaItemUiModel
import id.zelory.compressor.Compressor
import id.zelory.compressor.constraint.size
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import kotlin.jvm.Throws

const val CONTENT_URI_SCHEME = "content"
const val BITMAP_COMPRESS_QUALITY = 80
const val FALLBACK_FILE_SIZE = 30_000_000L

fun Bitmap.encodeAsBase64(): String {
    val output = ByteArrayOutputStream()
    compress(Bitmap.CompressFormat.JPEG, BITMAP_COMPRESS_QUALITY, output)
    val bytes = output.toByteArray()
    return Base64.encodeToString(bytes, Base64.DEFAULT)
}

fun String.decodeAsBase64Bitmap(): Bitmap {
    val bytes = Base64.decode(this.substringAfter(","), Base64.DEFAULT)
    return BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
}

fun Uri.decodeAsBitmap(contentResolver: ContentResolver): Bitmap {
    val source = ImageDecoder.createSource(contentResolver, this)
    return ImageDecoder.decodeBitmap(source)
}

suspend fun Context.handleMediaUris(
    uris: List<String>,
    maxFileSizeKb: String? = null
): List<MediaItemUiModel> {
    return uris.map { uri ->
        var file = storeUriAsFileToCache(
            uri.toUri(),
        )

        runCatching {
            file = compressFile(file, maxFileSizeKb)

            MediaItemUiModel(
                uri = uri,
                file = file,
                name = file.name,
                isSizeValid = true
            )
        }.fold(
            onSuccess = { mediaItemUiModel -> mediaItemUiModel },
            onFailure = {
                MediaItemUiModel(
                    uri = uri,
                    file = file,
                    name = file.name,
                    isSizeValid = false
                )
            }
        )
    }
}

suspend fun Context.storeUriAsFileToCache(uri: Uri): File {
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

    return file
}

@Throws(IllegalStateException::class)
private suspend fun Context.compressFile(file: File, maxFileSizeKb: String? = null): File {
    val allowedFileSize = getFileAllowedSize(maxFileSizeKb)
    if (file.length() > allowedFileSize) {
        Timber.d("${file.length()} is larger than $allowedFileSize")
        error("The file size ${file.length()} is larger than the allowed $allowedFileSize")
    }

    return Compressor.compress(context = this, imageFile = file) {
        size(allowedFileSize)
    }
}


private fun getFileAllowedSize(maxFileSizeKb: String?): Long =
    maxFileSizeKb.orEmpty().toLongOrNull() ?: FALLBACK_FILE_SIZE

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
