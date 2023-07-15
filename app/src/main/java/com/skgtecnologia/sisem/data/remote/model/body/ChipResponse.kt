package com.skgtecnologia.sisem.data.remote.model.body

import androidx.compose.ui.Modifier
import com.skgtecnologia.sisem.domain.model.body.BodyRowModel
import com.skgtecnologia.sisem.domain.model.body.BodyRowType
import com.skgtecnologia.sisem.domain.model.body.ChipModel
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import com.valkiria.uicomponents.props.ChipStyle
import com.valkiria.uicomponents.props.TextStyle

@JsonClass(generateAdapter = true)
data class ChipResponse(
    @Json(name = "identifier") val identifier: String?,
    @Json(name = "icon") val icon: String?,
    @Json(name = "text") val text: String?,
    @Json(name = "text_style") val textStyle: TextStyle?,
    @Json(name = "style") val style: ChipStyle?,
    @Json(name = "margins") val modifier: Modifier?
) : BodyRowResponse {

    override val type: BodyRowType = BodyRowType.CHIP

    override fun mapToDomain(): BodyRowModel = ChipModel(
        identifier = identifier ?: error("Identifier cannot be null"),
        icon = icon,
        text = text ?: error("Chip text cannot be null"),
        textStyle = textStyle ?: error("Text style cannot be null"),
        style = style ?: error("Chip style cannot be null"),
        modifier = modifier ?: Modifier
    )
}
