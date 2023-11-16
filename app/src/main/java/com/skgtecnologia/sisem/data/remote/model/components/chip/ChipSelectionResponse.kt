package com.skgtecnologia.sisem.data.remote.model.components.chip

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.Modifier
import com.skgtecnologia.sisem.data.remote.model.components.BodyRowResponse
import com.skgtecnologia.sisem.data.remote.model.components.label.TextResponse
import com.skgtecnologia.sisem.data.remote.model.components.label.mapToUi
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import com.valkiria.uicomponents.components.BodyRowType
import com.valkiria.uicomponents.components.chip.ChipSelectionUiModel

@JsonClass(generateAdapter = true)
data class ChipSelectionResponse(
    @Json(name = "identifier") val identifier: String?,
    @Json(name = "title") val title: TextResponse?,
    @Json(name = "items") val items: List<ChipSelectionItemResponse>?,
    @Json(name = "selected") val selected: String?,
    @Json(name = "arrangement") val arrangement: Arrangement.Horizontal?,
    @Json(name = "margins") val modifier: Modifier?
) : BodyRowResponse {

    override val type: BodyRowType = BodyRowType.CHIP_SELECTION

    override fun mapToUi(): ChipSelectionUiModel = ChipSelectionUiModel(
        identifier = identifier ?: error("ChipSelection identifier cannot be null"),
        title = title?.mapToUi(),
        items = items?.map { it.mapToUi() } ?: error("ChipSelection items cannot be null"),
        selected = selected,
        selectionVisibility = selectionVisibility,
        arrangement = arrangement ?: Arrangement.Center,
        modifier = modifier ?: Modifier
    )
}
