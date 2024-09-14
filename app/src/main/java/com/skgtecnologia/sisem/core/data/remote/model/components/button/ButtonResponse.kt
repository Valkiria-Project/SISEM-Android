package com.skgtecnologia.sisem.core.data.remote.model.components.button

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.Modifier
import com.skgtecnologia.sisem.core.data.remote.model.screen.BodyRowResponse
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import com.valkiria.uicomponents.components.BodyRowType
import com.valkiria.uicomponents.components.button.ButtonSize
import com.valkiria.uicomponents.components.button.ButtonStyle
import com.valkiria.uicomponents.components.button.ButtonUiModel
import com.valkiria.uicomponents.components.button.OnClick
import com.valkiria.uicomponents.components.label.TextStyle

@JsonClass(generateAdapter = true)
data class ButtonResponse(
    @Json(name = "identifier") val identifier: String?,
    @Json(name = "left_icon") val leftIcon: String?,
    @Json(name = "label") val label: String?,
    @Json(name = "style") val style: ButtonStyle?,
    @Json(name = "text_style") val textStyle: TextStyle?,
    @Json(name = "on_click") val onClick: OnClick?,
    @Json(name = "size") val size: ButtonSize?,
    @Json(name = "visibility") val visibility: Boolean?,
    @Json(name = "required") val required: Boolean?,
    @Json(name = "arrangement") val arrangement: Arrangement.Horizontal?,
    @Json(name = "margins") val modifier: Modifier?
) : BodyRowResponse {

    override val type: BodyRowType = BodyRowType.BUTTON

    override fun mapToUi(): ButtonUiModel = ButtonUiModel(
        identifier = identifier ?: error("Button identifier cannot be null"),
        label = label ?: error("Button label cannot be null"),
        leftIcon = leftIcon,
        style = style ?: error("Button style cannot be null"),
        textStyle = textStyle ?: error("Button textStyle cannot be null"),
        onClick = onClick ?: error("Button onClick cannot be null"),
        size = size ?: error("Button size cannot be null"),
        visibility = visibility ?: true,
        required = required ?: false,
        arrangement = arrangement ?: Arrangement.Center,
        modifier = modifier ?: Modifier
    )
}
