package com.skgtecnologia.sisem.data.remote.model.body

import androidx.compose.ui.Modifier
import com.skgtecnologia.sisem.domain.model.body.BodyRowModel
import com.skgtecnologia.sisem.domain.model.body.BodyRowType
import com.skgtecnologia.sisem.domain.model.body.FilterChipsModel
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import com.valkiria.uicomponents.props.TextStyle

@JsonClass(generateAdapter = true)
data class FilterChipsResponse(
    @Json(name = "identifier") val identifier: String?,
    @Json(name = "chips") val chips: List<String>?,
    @Json(name = "text_style") val textStyle: TextStyle?,
    @Json(name = "margins") val modifier: Modifier?
) : BodyRowResponse {

    override val type: BodyRowType = BodyRowType.CHIP

    override fun mapToDomain(): BodyRowModel = FilterChipsModel(
        identifier = identifier ?: error("Filter Chips identifier cannot be null"),
        chips = chips.orEmpty(),
        textStyle = textStyle ?: error("Chip textStyle cannot be null"),
        modifier = modifier ?: Modifier
    )
}
