package com.skgtecnologia.sisem.data.remote.model.body

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.Modifier
import com.skgtecnologia.sisem.data.remote.model.bricks.ChipSectionItemResponse
import com.skgtecnologia.sisem.data.remote.model.bricks.mapToUi
import com.skgtecnologia.sisem.data.remote.model.props.TextResponse
import com.skgtecnologia.sisem.data.remote.model.props.mapToDomain
import com.skgtecnologia.sisem.domain.model.body.BodyRowType
import com.skgtecnologia.sisem.domain.model.body.ChipSelectionModel
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ChipSelectionResponse(
    @Json(name = "identifier") val identifier: String?,
    @Json(name = "title") val title: TextResponse?,
    @Json(name = "items") val items: List<ChipSectionItemResponse>?,
    @Json(name = "selected") val selected: String?,
    @Json(name = "arrangement") val arrangement: Arrangement.Horizontal?,
    @Json(name = "margins") val modifier: Modifier?
) : BodyRowResponse {

    override val type: BodyRowType = BodyRowType.CHIP_SELECTION

    override fun mapToDomain(): ChipSelectionModel = ChipSelectionModel(
        identifier = identifier ?: error("DropDown identifier cannot be null"),
        title = title?.mapToDomain() ?: error("DropDown title cannot be null"),
        items = items?.map { it.mapToUi() } ?: error("DropDown items cannot be null"),
        selected = selected,
        arrangement = arrangement ?: Arrangement.Center,
        modifier = modifier ?: Modifier
    )
}
