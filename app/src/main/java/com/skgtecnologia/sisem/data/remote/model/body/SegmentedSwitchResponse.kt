package com.skgtecnologia.sisem.data.remote.model.body

import androidx.compose.ui.Modifier
import com.skgtecnologia.sisem.data.remote.model.bricks.OptionResponse
import com.skgtecnologia.sisem.data.remote.model.bricks.mapToUi
import com.skgtecnologia.sisem.domain.model.body.BodyRowType
import com.skgtecnologia.sisem.domain.model.body.SegmentedSwitchModel
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import com.valkiria.uicomponents.props.TextStyle

@JsonClass(generateAdapter = true)
data class SegmentedSwitchResponse(
    @Json(name = "identifier") val identifier: String?,
    @Json(name = "text") val text: String?,
    @Json(name = "text_style") val textStyle: TextStyle?,
    @Json(name = "options") val options: List<OptionResponse>?,
    @Json(name = "margins") val modifier: Modifier?
) : BodyRowResponse {

    override val type: BodyRowType = BodyRowType.SEGMENTED_SWITCH

    override fun mapToDomain(): SegmentedSwitchModel = SegmentedSwitchModel(
        identifier = identifier ?: error("SegmentedSwitch identifier cannot be null"),
        text = text ?: error("SegmentedSwitch text cannot be null"),
        textStyle = textStyle ?: error("SegmentedSwitch textStyle cannot be null"),
        options = options?.map { it.mapToUi() } ?: error("SegmentedSwitch options cannot be null"),
        modifier = modifier ?: Modifier
    )
}
