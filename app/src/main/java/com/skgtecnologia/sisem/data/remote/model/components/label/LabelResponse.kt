package com.skgtecnologia.sisem.data.remote.model.components.label

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.Modifier
import com.skgtecnologia.sisem.data.remote.model.components.BodyRowResponse
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import com.valkiria.uicomponents.components.label.TextStyle
import com.valkiria.uicomponents.components.BodyRowType
import com.valkiria.uicomponents.components.label.LabelUiModel

@JsonClass(generateAdapter = true)
data class LabelResponse(
    @Json(name = "identifier") val identifier: String?,
    @Json(name = "text") val text: String?,
    @Json(name = "text_style") val textStyle: TextStyle?,
    @Json(name = "color") val textColor: String?,
    @Json(name = "visibility") val visibility: Boolean?,
    @Json(name = "required") val required: Boolean?,
    @Json(name = "arrangement") val arrangement: Arrangement.Horizontal?,
    @Json(name = "margins") val modifier: Modifier?
) : BodyRowResponse {

    override val type: BodyRowType = BodyRowType.LABEL

    override fun mapToUi(): LabelUiModel = LabelUiModel(
        identifier = identifier ?: error("Label identifier cannot be null"),
        text = text ?: error("Label text cannot be null"),
        textStyle = textStyle ?: error("Label textStyle cannot be null"),
        textColor = textColor ?: error("Label textColor cannot be null"),
        visibility = visibility ?: true,
        required = required ?: false,
        arrangement = arrangement ?: Arrangement.Center,
        modifier = modifier ?: Modifier
    )
}
