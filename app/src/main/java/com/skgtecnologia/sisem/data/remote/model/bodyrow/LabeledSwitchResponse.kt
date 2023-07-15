package com.skgtecnologia.sisem.data.remote.model.bodyrow

import androidx.compose.ui.Modifier
import com.skgtecnologia.sisem.domain.model.bodyrow.BodyRowModel
import com.skgtecnologia.sisem.domain.model.bodyrow.BodyRowType
import com.skgtecnologia.sisem.domain.model.bodyrow.LabeledSwitchModel
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import com.valkiria.uicomponents.props.TextStyle

@JsonClass(generateAdapter = true)
data class LabeledSwitchResponse(
    @Json(name = "identifier") val identifier: String?,
    @Json(name = "text") val text: String?,
    @Json(name = "text_style") val textStyle: TextStyle?,
    @Json(name = "margins") val modifier: Modifier?
) : BodyRowResponse {

    override val type: BodyRowType = BodyRowType.RICH_LABEL

    override fun mapToDomain(): BodyRowModel = LabeledSwitchModel(
        identifier = identifier ?: error("Identifier cannot be null"),
        text = text ?: error("Label text cannot be null"),
        textStyle = textStyle ?: error("Text style cannot be null"),
        modifier = modifier ?: Modifier
    )
}
