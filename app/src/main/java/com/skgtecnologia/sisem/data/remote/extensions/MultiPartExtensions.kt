package com.skgtecnologia.sisem.data.remote.extensions

import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

const val IMAGE_MEDIA_TYPE = "image/jpeg"
private const val MULTI_PART_FORM_DATA_MEDIA_TYPE = "multipart/form-data"

fun File.createRequestBody(): RequestBody = asRequestBody(IMAGE_MEDIA_TYPE.toMediaType())

fun String.createRequestBody(): RequestBody =
    toRequestBody(MULTI_PART_FORM_DATA_MEDIA_TYPE.toMediaTypeOrNull())
