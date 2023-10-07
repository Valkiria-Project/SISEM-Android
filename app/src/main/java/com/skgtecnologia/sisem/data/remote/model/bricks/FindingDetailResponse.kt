package com.skgtecnologia.sisem.data.remote.model.bricks

import androidx.compose.ui.Modifier
import com.skgtecnologia.sisem.data.remote.model.props.TextResponse
import com.skgtecnologia.sisem.data.remote.model.props.mapToUI
import com.valkiria.uicomponents.model.ui.finding.FindingDetailModel
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class FindingDetailResponse(
    @Json(name = "images") val images: List<String>?,
    @Json(name = "description") val description: TextResponse?,
    @Json(name = "reporter") val reporter: TextResponse?,
    @Json(name = "margins") val modifier: Modifier?
)

fun FindingDetailResponse.mapToUi(): FindingDetailModel = FindingDetailModel(
    images = images ?: error("FindingDetail images cannot be null"),
    description = description?.mapToUI() ?: error("FindingDetail description cannot be null"),
    reporter = reporter?.mapToUI() ?: error("FindingDetail reporter cannot be null"),
    modifier = modifier ?: error("FindingDetail modifier cannot be null")
)
