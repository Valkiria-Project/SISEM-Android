package com.skgtecnologia.sisem.data.remote.model.body

import androidx.compose.ui.Modifier
import com.skgtecnologia.sisem.domain.model.body.BodyRowModel
import com.skgtecnologia.sisem.domain.model.body.BodyRowType
import com.skgtecnologia.sisem.domain.model.body.ButtonModel
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import com.valkiria.uicomponents.components.button.OnClick
import com.valkiria.uicomponents.props.ButtonSize
import com.valkiria.uicomponents.props.ButtonStyle

@JsonClass(generateAdapter = true)
data class ButtonResponse(
    @Json(name = "identifier") val identifier: String?,
    @Json(name = "label") val label: String?,
    @Json(name = "style") val style: ButtonStyle,
    @Json(name = "on_click") val onClick: OnClick,
    @Json(name = "size") val size: ButtonSize,
    @Json(name = "margins") val modifier: Modifier?
) : BodyRowResponse {

    override val type: BodyRowType = BodyRowType.BUTTON

    override fun mapToDomain(): BodyRowModel = ButtonModel(
        identifier = identifier ?: error("Button identifier cannot be null"),
        label = label ?: error("Button label cannot be null"),
        style = style,
        onClick = onClick,
        size = size,
        modifier = modifier ?: Modifier
    )
}
