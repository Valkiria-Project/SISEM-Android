package com.skgtecnologia.sisem.data.remote.model.bricks.banner.finding

import androidx.compose.ui.Modifier
import com.skgtecnologia.sisem.data.remote.model.components.label.TextResponse
import com.skgtecnologia.sisem.data.remote.model.components.label.mapToUi
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import com.valkiria.uicomponents.bricks.banner.finding.FindingDetailUiModel

@JsonClass(generateAdapter = true)
data class FindingDetailResponse(
    @Json(name = "images") val images: List<String>?,
    @Json(name = "description") val description: TextResponse?,
    @Json(name = "reporter") val reporter: TextResponse?,
    @Json(name = "margins") val modifier: Modifier?
)

fun FindingDetailResponse.mapToUi(): FindingDetailUiModel = FindingDetailUiModel(
    images = images ?: error("FindingDetail images cannot be null"),
    description = description?.mapToUi() ?: error("FindingDetail description cannot be null"),
    reporter = reporter?.mapToUi() ?: error("FindingDetail reporter cannot be null"),
    modifier = modifier ?: error("FindingDetail modifier cannot be null")
)
