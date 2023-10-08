package com.skgtecnologia.sisem.data.remote.model.body

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.Modifier
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import com.valkiria.uicomponents.components.label.TextStyle
import com.valkiria.uicomponents.components.BodyRowType
import com.valkiria.uicomponents.components.richlabel.RichLabelUiModel

@JsonClass(generateAdapter = true)
data class RichLabelResponse(
    @Json(name = "identifier") val identifier: String?,
    @Json(name = "text") val text: String?,
    @Json(name = "text_style") val textStyle: TextStyle?,
    @Json(name = "arrangement") val arrangement: Arrangement.Horizontal?,
    @Json(name = "margins") val modifier: Modifier?
) : BodyRowResponse {

    override val type: BodyRowType = BodyRowType.RICH_LABEL

    override fun mapToUi(): RichLabelUiModel = RichLabelUiModel(
        identifier = identifier ?: error("RichLabel identifier cannot be null"),
        text = text ?: error("RichLabel text cannot be null"),
        textStyle = textStyle ?: error("RichLabel textStyle cannot be null"),
        arrangement = arrangement ?: Arrangement.Center,
        modifier = modifier ?: Modifier
    )
}
