package com.skgtecnologia.sisem.data.remote.model.body

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.Modifier
import com.skgtecnologia.sisem.data.remote.model.bricks.ChipOptionResponse
import com.skgtecnologia.sisem.data.remote.model.bricks.mapToUi
import com.skgtecnologia.sisem.data.remote.model.props.TextResponse
import com.skgtecnologia.sisem.data.remote.model.props.mapToDomain
import com.skgtecnologia.sisem.domain.model.body.BodyRowType
import com.skgtecnologia.sisem.domain.model.body.ChipOptionsModel
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ChipOptionsResponse(
    @Json(name = "identifier") val identifier: String?,
    @Json(name = "title") val title: TextResponse?,
    @Json(name = "items") val items: List<ChipOptionResponse>?,
    @Json(name = "arrangement") val arrangement: Arrangement.Horizontal?,
    @Json(name = "margins") val modifier: Modifier?
) : BodyRowResponse {

    override val type: BodyRowType = BodyRowType.CHIP_OPTIONS

    override fun mapToDomain(): ChipOptionsModel = ChipOptionsModel(
        identifier = identifier ?: error("ChipOptions identifier cannot be null"),
        title = title?.mapToDomain() ?: error("ChipOptions title cannot be null"),
        items = items?.map { it.mapToUi() } ?: error("ChipOptions items cannot be null"),
        arrangement = arrangement ?: Arrangement.Center,
        modifier = modifier ?: Modifier
    )
}
