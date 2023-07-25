package com.skgtecnologia.sisem.data.remote.model.error

import com.skgtecnologia.sisem.domain.model.error.ErrorModel
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ErrorResponse(
    @Json(name = "icon") val icon: String? = null,
    @Json(name = "title") val title: String? = null,
    @Json(name = "description") val description: String? = null
)

fun ErrorResponse.mapToDomain() = ErrorModel(
    icon = icon ?: error("Error icon cannot be null"),
    title = title ?: error("Error title cannot be null"),
    description = description ?: error("Error description cannot be null")
)
