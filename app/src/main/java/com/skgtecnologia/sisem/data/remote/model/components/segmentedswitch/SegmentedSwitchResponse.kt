package com.skgtecnologia.sisem.data.remote.model.components.segmentedswitch

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.Modifier
import com.skgtecnologia.sisem.data.remote.model.components.BodyRowResponse
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import com.valkiria.uicomponents.components.BodyRowType
import com.valkiria.uicomponents.components.segmentedswitch.SegmentedSwitchUiModel
import com.valkiria.uicomponents.components.label.TextStyle

@JsonClass(generateAdapter = true)
data class SegmentedSwitchResponse(
    @Json(name = "identifier") val identifier: String?,
    @Json(name = "text") val text: String?,
    @Json(name = "text_style") val textStyle: TextStyle?,
    @Json(name = "options") val options: List<OptionResponse>?,
    @Json(name = "selected") val selected: Boolean?,
    @Json(name = "selection_visibility") val selectionVisibility: Map<String, List<String>>?,
    @Json(name = "visibility") val visibility: Boolean?,
    @Json(name = "required") val required: Boolean?,
    @Json(name = "arrangement") val arrangement: Arrangement.Horizontal?,
    @Json(name = "margins") val modifier: Modifier?
) : BodyRowResponse {

    override val type: BodyRowType = BodyRowType.SEGMENTED_SWITCH

    override fun mapToUi(): SegmentedSwitchUiModel = SegmentedSwitchUiModel(
        identifier = identifier ?: error("SegmentedSwitch identifier cannot be null"),
        text = text ?: error("SegmentedSwitch text cannot be null"),
        textStyle = textStyle ?: error("SegmentedSwitch textStyle cannot be null"),
        options = options?.map { it.mapToUi() } ?: error("SegmentedSwitch options cannot be null"),
        selected = selected ?: true,
        selectionVisibility = selectionVisibility,
        visibility = visibility ?: true,
        required = required ?: false,
        arrangement = arrangement ?: Arrangement.Center,
        modifier = modifier ?: Modifier
    )
}
