package com.skgtecnologia.sisem.data.remote.model.bodyrow

import androidx.compose.ui.Modifier
import com.skgtecnologia.sisem.domain.model.bodyrow.BodyRowModel
import com.skgtecnologia.sisem.domain.model.bodyrow.BodyRowType
import com.skgtecnologia.sisem.domain.model.bodyrow.ButtonModel
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import com.valkiria.uicomponents.components.button.OnClick
import com.valkiria.uicomponents.props.ButtonSize
import com.valkiria.uicomponents.props.ButtonStyle

@JsonClass(generateAdapter = true)
data class ButtonResponse(
    val identifier: String?,
    val label: String?,
    val style: ButtonStyle,
    @Json(name = "on_click") val onClick: OnClick,
    val size: ButtonSize,
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
