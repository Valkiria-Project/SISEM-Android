package com.skgtecnologia.sisem.data.remote.model.components.chip

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.Modifier
import com.skgtecnologia.sisem.data.remote.model.components.BodyRowResponse
import com.skgtecnologia.sisem.data.remote.model.components.label.TextResponse
import com.skgtecnologia.sisem.data.remote.model.components.label.mapToUi
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import com.valkiria.uicomponents.components.BodyRowType
import com.valkiria.uicomponents.components.chip.ChipOptionsUiModel

@JsonClass(generateAdapter = true)
data class ChipOptionsResponse(
    @Json(name = "identifier") val identifier: String?,
    @Json(name = "title") val title: TextResponse?,
    @Json(name = "items") val items: List<ChipOptionResponse>?,
    @Json(name = "required") val required: Boolean?,
    @Json(name = "visibility") val visibility: Boolean?,
    @Json(name = "arrangement") val arrangement: Arrangement.Horizontal?,
    @Json(name = "margins") val modifier: Modifier?
) : BodyRowResponse {

    override val type: BodyRowType = BodyRowType.CHIP_OPTIONS

    override fun mapToUi(): ChipOptionsUiModel = ChipOptionsUiModel(
        identifier = identifier ?: error("ChipOptions identifier cannot be null"),
        title = title?.mapToUi(),
        items = items?.map { it.mapToUi() } ?: error("ChipOptions items cannot be null"),
        required = required ?: false,
        visibility = visibility ?: true,
        arrangement = arrangement ?: Arrangement.Center,
        modifier = modifier ?: Modifier
    )
}
