package com.skgtecnologia.sisem.data.remote.model.body

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.Modifier
import com.skgtecnologia.sisem.domain.model.body.BodyRowType
import com.skgtecnologia.sisem.domain.model.body.ButtonModel
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import com.valkiria.uicomponents.model.ui.button.OnClick
import com.valkiria.uicomponents.model.props.ButtonSize
import com.valkiria.uicomponents.model.props.ButtonStyle
import com.valkiria.uicomponents.model.props.TextStyle

@JsonClass(generateAdapter = true)
data class ButtonResponse(
    @Json(name = "identifier") val identifier: String?,
    @Json(name = "label") val label: String?,
    @Json(name = "style") val style: ButtonStyle?,
    @Json(name = "text_style") val textStyle: TextStyle?,
    @Json(name = "on_click") val onClick: OnClick?,
    @Json(name = "size") val size: ButtonSize?,
    @Json(name = "arrangement") val arrangement: Arrangement.Horizontal?,
    @Json(name = "margins") val modifier: Modifier?
) : BodyRowResponse {

    override val type: BodyRowType = BodyRowType.BUTTON

    override fun mapToDomain(): ButtonModel = ButtonModel(
        identifier = identifier ?: error("Button identifier cannot be null"),
        label = label ?: error("Button label cannot be null"),
        style = style ?: error("Button style cannot be null"),
        textStyle = textStyle ?: error("Button textStyle cannot be null"),
        onClick = onClick ?: error("Button onClick cannot be null"),
        size = size ?: error("Button size cannot be null"),
        arrangement = arrangement ?: Arrangement.Center,
        modifier = modifier ?: Modifier
    )
}
