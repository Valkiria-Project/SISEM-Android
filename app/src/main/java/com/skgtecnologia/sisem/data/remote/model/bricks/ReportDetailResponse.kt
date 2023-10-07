package com.skgtecnologia.sisem.data.remote.model.bricks

import androidx.compose.ui.Modifier
import com.skgtecnologia.sisem.data.remote.model.props.TextResponse
import com.skgtecnologia.sisem.data.remote.model.props.mapToUI
import com.valkiria.uicomponents.model.ui.report.ReportDetailUiModel
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

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
    title = title?.mapToUI() ?: error("Detail title cannot be null"),
    subtitle = subtitle?.mapToUI() ?: error("Detail subtitle cannot be null"),
    description = description?.mapToUI() ?: error("Detail description cannot be null"),
    modifier = modifier ?: error("Detail modifier cannot be null")
)
