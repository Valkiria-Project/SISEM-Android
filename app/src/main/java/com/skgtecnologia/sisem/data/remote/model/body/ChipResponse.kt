package com.skgtecnologia.sisem.data.remote.model.body

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.Modifier
import com.valkiria.uicomponents.components.chip.ChipUiModel
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import com.valkiria.uicomponents.model.props.ChipStyle
import com.valkiria.uicomponents.model.props.TextStyle
import com.valkiria.uicomponents.components.BodyRowType

@JsonClass(generateAdapter = true)
data class ChipResponse(
    @Json(name = "identifier") val identifier: String?,
    @Json(name = "icon") val icon: String?,
    @Json(name = "text") val text: String?,
    @Json(name = "text_style") val textStyle: TextStyle?,
    @Json(name = "style") val style: ChipStyle?,
    @Json(name = "arrangement") val arrangement: Arrangement.Horizontal?,
    @Json(name = "margins") val modifier: Modifier?
) : BodyRowResponse {

    override val type: BodyRowType = BodyRowType.CHIP

    override fun mapToUi(): ChipUiModel = ChipUiModel(
            identifier = identifier ?: error("Chip identifier cannot be null"),
            icon = icon,
            text = text ?: error("Chip text cannot be null"),
            textStyle = textStyle ?: error("Chip textStyle cannot be null"),
            style = style ?: error("Chip style cannot be null"),
            arrangement = arrangement ?: Arrangement.Center,
            modifier = modifier ?: Modifier
        )
}
