package com.skgtecnologia.sisem.data.model.bodyrow

import com.skgtecnologia.sisem.data.model.props.MarginsResponse
import com.skgtecnologia.sisem.data.model.props.mapToDomain
import com.skgtecnologia.sisem.domain.model.bodyrow.BodyRowModel
import com.skgtecnologia.sisem.domain.model.bodyrow.BodyRowType
import com.skgtecnologia.sisem.domain.model.bodyrow.RichLabelModel
import com.skgtecnologia.sisem.domain.model.props.TextStyle
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class LabelResponse(
    @Json(name = "identifier") val identifier: String?,
    @Json(name = "text") val text: String?,
    @Json(name = "text_style") val textStyle: TextStyle?,
    @Json(name = "margins") val margins: MarginsResponse?
) : BodyRowResponse {

    override val type: BodyRowType = BodyRowType.RICH_LABEL

    override fun mapToDomain(): BodyRowModel = RichLabelModel(
        identifier = identifier ?: error("Identifier cannot be null"),
        text = text ?: error("Label text cannot be null"),
        textStyle = textStyle ?: error("Text style cannot be null"),
        margins = margins?.mapToDomain()
    )
}
