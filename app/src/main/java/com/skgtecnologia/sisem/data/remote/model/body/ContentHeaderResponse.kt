package com.skgtecnologia.sisem.data.remote.model.body

import androidx.compose.ui.Modifier
import com.skgtecnologia.sisem.domain.model.body.BodyRowModel
import com.skgtecnologia.sisem.domain.model.body.BodyRowType
import com.skgtecnologia.sisem.domain.model.body.ContentHeaderModel
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import com.valkiria.uicomponents.props.TextStyle

@JsonClass(generateAdapter = true)
data class ContentHeaderResponse(
    @Json(name = "identifier") val identifier: String?,
    @Json(name = "text") val text: String?,
    @Json(name = "left_icon") val leftIcon: String?,
    @Json(name = "text_style") val textStyle: TextStyle?,
    @Json(name = "margins") val modifier: Modifier?
) : BodyRowResponse {

    override val type: BodyRowType = BodyRowType.CONTENT_HEADER

    override fun mapToDomain(): BodyRowModel = ContentHeaderModel(
        identifier = identifier ?: error("Content Header identifier cannot be null"),
        text = text ?: error("Content Header text cannot be null"),
        leftIcon = leftIcon,
        textStyle = textStyle ?: error("Content Header textStyle cannot be null"),
        modifier = modifier ?: Modifier
    )
}
