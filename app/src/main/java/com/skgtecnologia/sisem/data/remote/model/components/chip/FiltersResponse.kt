package com.skgtecnologia.sisem.data.remote.model.components.chip

import androidx.compose.ui.Modifier
import com.skgtecnologia.sisem.data.remote.model.components.BodyRowResponse
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import com.valkiria.uicomponents.components.BodyRowType
import com.valkiria.uicomponents.components.chip.FiltersUiModel
import com.valkiria.uicomponents.components.label.TextStyle

@JsonClass(generateAdapter = true)
data class FiltersResponse(
    @Json(name = "identifier") val identifier: String?,
    @Json(name = "options") val options: List<String>?,
    @Json(name = "text_style") val textStyle: TextStyle?,
    @Json(name = "visibility") val visibility: Boolean?,
    @Json(name = "required") val required: Boolean?,
    @Json(name = "margins") val modifier: Modifier?
) : BodyRowResponse {

    override val type: BodyRowType = BodyRowType.CHIP

    override fun mapToUi(): FiltersUiModel = FiltersUiModel(
        identifier = identifier ?: error("Filters identifier cannot be null"),
        options = options ?: error("Filters options cannot be null"),
        textStyle = textStyle ?: error("Filters textStyle cannot be null"),
        visibility = visibility ?: true,
        required = required ?: false,
        modifier = modifier ?: Modifier
    )
}
