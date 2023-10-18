package com.skgtecnologia.sisem.data.remote.extensions

import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File


const val MULTI_PART_FORM_DATA = "multipart/form-data"

fun File.createRequestBody(): RequestBody = asRequestBody(MULTI_PART_FORM_DATA.toMediaTypeOrNull())

fun String.createRequestBody(): RequestBody =
    toRequestBody(MULTI_PART_FORM_DATA.toMediaTypeOrNull())
