package com.skgtecnologia.sisem.ui.commons.extensions

import android.content.ContentResolver
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import android.net.Uri
import android.util.Base64
import java.io.ByteArrayOutputStream

fun Bitmap.encodeAsBase64(): String {
    val output = ByteArrayOutputStream()
    this.compress(Bitmap.CompressFormat.JPEG, 80, output)
    val bytes = output.toByteArray()
    return Base64.encodeToString(bytes, Base64.DEFAULT)
}

fun String.decodeAsBase64Bitmap(): Bitmap {
    val bytes = Base64.decode(this, Base64.DEFAULT)
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
