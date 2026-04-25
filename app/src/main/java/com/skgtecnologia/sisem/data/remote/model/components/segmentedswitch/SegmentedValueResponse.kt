package com.skgtecnologia.sisem.data.remote.model.components.segmentedswitch

import com.skgtecnologia.sisem.data.remote.model.components.label.TextResponse
import com.skgtecnologia.sisem.data.remote.model.components.label.mapToUi
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import com.valkiria.uicomponents.components.segmentedswitch.SegmentedValueUiModel

@JsonClass(generateAdapter = true)
data class SegmentedValueResponse(
    @Json(name = "title") val title: TextResponse?,
    @Json(name = "pill_color") val color: String?
)

fun SegmentedValueResponse.mapToUi(): SegmentedValueUiModel = SegmentedValueUiModel(
    title = title?.mapToUi() ?: error("SegmentedValue title cannot be null"),
    color = color
)
