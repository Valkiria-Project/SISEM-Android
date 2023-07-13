package com.skgtecnologia.sisem.data.model.bodyrow

import com.skgtecnologia.sisem.data.model.props.MarginsResponse
import com.skgtecnologia.sisem.data.model.props.mapToDomain
import com.skgtecnologia.sisem.domain.model.bodyrow.BodyRowModel
import com.skgtecnologia.sisem.domain.model.bodyrow.BodyRowType
import com.skgtecnologia.sisem.domain.model.bodyrow.ButtonModel
import com.skgtecnologia.sisem.domain.model.props.ButtonSize
import com.skgtecnologia.sisem.domain.model.props.ButtonStyle
import com.skgtecnologia.sisem.domain.model.props.OnClick
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ButtonResponse(
    val identifier: String?,
    val label: String?,
    val style: ButtonStyle,
    @Json(name = "on_click") val onClick: OnClick,
    val size: ButtonSize,
    val margins: MarginsResponse?
) : BodyRowResponse {

    override val type: BodyRowType = BodyRowType.BUTTON

    override fun mapToDomain(): BodyRowModel = ButtonModel(
        identifier = identifier ?: error("Button identifier cannot be null"),
        label = label ?: error("Button label cannot be null"),
        style = style,
        onClick = onClick,
        size = size,
        margins = margins?.mapToDomain()
    )
}
