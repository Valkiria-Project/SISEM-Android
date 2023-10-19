package com.skgtecnologia.sisem.data.remote.extensions

import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

const val MULTI_PART_FORM_DATA = "multipart/form-data"
const val MEDIA_TYPE_MULTIPART = "image/jpeg"

fun File.createRequestBody(): RequestBody = asRequestBody(MEDIA_TYPE_MULTIPART.toMediaType())

fun String.createRequestBody(): RequestBody =
    toRequestBody(MULTI_PART_FORM_DATA.toMediaTypeOrNull())
