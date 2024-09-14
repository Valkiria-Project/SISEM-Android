package com.skgtecnologia.sisem.core.data.remote.model.components.button

import androidx.compose.ui.Modifier
import com.skgtecnologia.sisem.core.data.remote.model.components.label.TextResponse
import com.skgtecnologia.sisem.core.data.remote.model.components.label.mapToUi
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import com.valkiria.uicomponents.components.button.ImageButtonOptionUiModel

@JsonClass(generateAdapter = true)
data class ImageButtonOptionResponse(
    @Json(name = "identifier") val identifier: String?,
    @Json(name = "title") val title: TextResponse?,
    @Json(name = "options") val options: List<ImageButtonResponse>?,
    @Json(name = "margins") val modifier: Modifier?
)

fun ImageButtonOptionResponse.mapToUi(): ImageButtonOptionUiModel = ImageButtonOptionUiModel(
    identifier = identifier ?: error("ImageButtonOption identifier cannot be null"),
    title = title?.mapToUi() ?: error("ImageButtonOption title cannot be null"),
    options = options?.map { it.mapToUi() }
        ?: error("ImageButtonOption options cannot be null"),
    modifier = modifier ?: Modifier
)
