package com.skgtecnologia.sisem.data.model.bodyrow

import com.skgtecnologia.sisem.data.model.props.MarginsResponse
import com.skgtecnologia.sisem.data.model.props.mapToDomain
import com.skgtecnologia.sisem.domain.model.bodyrow.BodyRowModel
import com.skgtecnologia.sisem.domain.model.bodyrow.BodyRowType
import com.skgtecnologia.sisem.domain.model.bodyrow.ChipModel
import com.skgtecnologia.sisem.domain.model.props.ChipStyle
import com.skgtecnologia.sisem.domain.model.props.TextStyle
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ChipResponse(
    @Json(name = "icon") val icon: String?,
    @Json(name = "text") val text: String?,
    @Json(name = "text_style") val textStyle: TextStyle?,
    @Json(name = "style") val style: ChipStyle?,
    @Json(name = "margins") val margins: MarginsResponse?
) : BodyRowResponse {

    override val type: BodyRowType = BodyRowType.CHIP

    override fun mapToDomain(): BodyRowModel = ChipModel(
        icon = icon,
        text = text ?: error("Chip text cannot be null"),
        textStyle = textStyle ?: error("Text style cannot be null"),
        style = style ?: error("Chip style cannot be null"),
        margins = margins?.mapToDomain()
    )
}
