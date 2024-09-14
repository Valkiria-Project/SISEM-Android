package com.skgtecnologia.sisem.core.data.remote.model.bricks.banner.report

import androidx.compose.ui.Modifier
import com.skgtecnologia.sisem.data.remote.model.components.label.TextResponse
import com.skgtecnologia.sisem.data.remote.model.components.label.mapToUi
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import com.valkiria.uicomponents.bricks.banner.report.ReportDetailUiModel

@JsonClass(generateAdapter = true)
data class ReportDetailResponse(
    @Json(name = "images") val images: List<String>?,
    @Json(name = "title") val title: TextResponse?,
    @Json(name = "subtitle") val subtitle: TextResponse?,
    @Json(name = "hour") val description: TextResponse?,
    @Json(name = "margins") val modifier: Modifier?
)

fun ReportDetailResponse.mapToUi(): ReportDetailUiModel = ReportDetailUiModel(
    images = images ?: error("Detail images cannot be null"),
    title = title?.mapToUi() ?: error("Detail title cannot be null"),
    subtitle = subtitle?.mapToUi() ?: error("Detail subtitle cannot be null"),
    description = description?.mapToUi() ?: error("Detail description cannot be null"),
    modifier = modifier ?: error("Detail modifier cannot be null")
)
