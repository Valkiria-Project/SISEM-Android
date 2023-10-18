package com.skgtecnologia.sisem.ui.commons.extensions

import android.content.ContentResolver
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import android.net.Uri
import android.provider.OpenableColumns
import android.util.Base64
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream

const val COMPRESSION_QUALITY = 60
const val CONTENT_URI_SCHEME = "content"

fun Bitmap.encodeAsBase64(): String {
    val output = ByteArrayOutputStream()
    this.compress(Bitmap.CompressFormat.JPEG, COMPRESSION_QUALITY, output)
    val bytes = output.toByteArray()
    return Base64.encodeToString(bytes, Base64.NO_WRAP)
}

fun String.decodeAsBase64Bitmap(): Bitmap {
    val bytes = Base64.decode(this, Base64.NO_WRAP)
    return BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
}

fun Uri.decodeAsBitmap(contentResolver: ContentResolver): Bitmap {
    val source = ImageDecoder.createSource(contentResolver, this)
    return ImageDecoder.decodeBitmap(source)
}

fun Bitmap.scale(width: Int, height: Int): Bitmap {
    return Bitmap.createScaledBitmap(this, width, height, true)
}

fun Bitmap.scaleByLargestSide(largestSideDimension: Int): Bitmap {
    val currentWidth = this.width
    val currentHeight = this.height
    val newWidth: Int
    val newHeight: Int
    if (currentWidth > currentHeight) {
        newWidth = largestSideDimension
        newHeight = newWidth * currentHeight / currentWidth
    } else {
        newHeight = largestSideDimension
        newWidth = newHeight * currentWidth / currentHeight
    }
    return scale(newWidth, newHeight)
}

fun Context.storeUriAsFileToCache(uri: Uri): File {
    val fileContents = try {
        contentResolver.openInputStream(uri)
    } catch (e: FileNotFoundException) {
        null
    }

    val file = when (uri.scheme) {
        CONTENT_URI_SCHEME -> File(cacheDir, contentResolver.getFileName(uri))
        else -> File(cacheDir, uri.lastPathSegment!!)
    }

    FileOutputStream(file).use {
        it.write(fileContents?.readBytes())
    }

    fileContents?.close()

    return file
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
