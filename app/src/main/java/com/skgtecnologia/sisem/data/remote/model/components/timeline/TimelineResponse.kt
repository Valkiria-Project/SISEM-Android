package com.skgtecnologia.sisem.data.remote.model.components.timeline

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.Modifier
import com.skgtecnologia.sisem.data.remote.model.components.BodyRowResponse
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import com.valkiria.uicomponents.components.BodyRowType
import com.valkiria.uicomponents.components.timeline.TimelineUiModel

@JsonClass(generateAdapter = true)
data class TimelineResponse(
    @Json(name = "identifier") val identifier: String?,
    @Json(name = "items") val items: List<TimelineItemResponse>?,
    @Json(name = "arrangement") val arrangement: Arrangement.Horizontal?,
    @Json(name = "margins") val modifier: Modifier?
) : BodyRowResponse {

    override val type: BodyRowType = BodyRowType.TIMELINE

    override fun mapToUi(): TimelineUiModel = TimelineUiModel(
        identifier = identifier ?: error("TimelineResponse identifier is required"),
        items = items?.map { it.toUiModel() } ?: error("TimelineResponse items is required"),
        arrangement = arrangement ?: error("TimelineResponse arrangement is required"),
        modifier = modifier ?: error("TimelineResponse modifier is required")
    )
}
