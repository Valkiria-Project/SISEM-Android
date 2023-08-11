package com.skgtecnologia.sisem.data.remote.model.body

import androidx.compose.ui.Modifier
import com.skgtecnologia.sisem.domain.model.body.BodyRowModel
import com.skgtecnologia.sisem.domain.model.body.BodyRowType
import com.skgtecnologia.sisem.domain.model.body.FiltersModel
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import com.valkiria.uicomponents.props.TextStyle

@JsonClass(generateAdapter = true)
data class FiltersResponse(
    @Json(name = "identifier") val identifier: String?,
    @Json(name = "options") val options: List<String>?,
    @Json(name = "text_style") val textStyle: TextStyle?,
    @Json(name = "margins") val modifier: Modifier?
) : BodyRowResponse {

    override val type: BodyRowType = BodyRowType.CHIP

    override fun mapToDomain(): BodyRowModel = FiltersModel(
        identifier = identifier ?: error("Filters identifier cannot be null"),
        options = options ?: error("Filters options cannot be null"),
        textStyle = textStyle ?: error("Filters textStyle cannot be null"),
        modifier = modifier ?: Modifier
    )
}
