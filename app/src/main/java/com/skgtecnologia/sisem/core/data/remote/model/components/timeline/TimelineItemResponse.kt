package com.skgtecnologia.sisem.core.data.remote.model.components.timeline

import com.skgtecnologia.sisem.data.remote.model.components.label.TextResponse
import com.skgtecnologia.sisem.data.remote.model.components.label.mapToUi
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import com.valkiria.uicomponents.components.timeline.TimelineItemUiModel

@JsonClass(generateAdapter = true)
data class TimelineItemResponse(
    @Json(name = "title") val title: TextResponse?,
    @Json(name = "description") val description: TextResponse?,
    @Json(name = "color") val color: String?,
    @Json(name = "icon") val icon: String?
)

fun TimelineItemResponse.toUiModel() = TimelineItemUiModel(
    title = title?.mapToUi() ?: error("TimelineItemResponse title is required"),
    description = description?.mapToUi() ?: error("TimelineItemResponse description is required"),
    color = color ?: error("TimelineItemResponse color is required"),
    icon = icon
)
