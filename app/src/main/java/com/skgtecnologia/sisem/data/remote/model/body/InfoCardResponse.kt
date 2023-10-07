package com.skgtecnologia.sisem.data.remote.model.body

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.Modifier
import com.skgtecnologia.sisem.data.remote.model.bricks.ChipSectionResponse
import com.skgtecnologia.sisem.data.remote.model.bricks.PillResponse
import com.skgtecnologia.sisem.data.remote.model.bricks.ReportsDetailResponse
import com.skgtecnologia.sisem.data.remote.model.bricks.mapToDomain
import com.skgtecnologia.sisem.data.remote.model.props.TextResponse
import com.skgtecnologia.sisem.data.remote.model.props.mapToDomain
import com.skgtecnologia.sisem.domain.model.body.InfoCardModel
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import com.valkiria.uicomponents.model.ui.body.BodyRowType

@JsonClass(generateAdapter = true)
data class InfoCardResponse(
    @Json(name = "identifier") val identifier: String?,
    @Json(name = "icon") val icon: String?,
    @Json(name = "title") val title: TextResponse?,
    @Json(name = "pill") val pill: PillResponse?,
    @Json(name = "date") val date: TextResponse?,
    @Json(name = "chip_section") val chipSection: ChipSectionResponse?,
    @Json(name = "reports_detail") val reportsDetail: ReportsDetailResponse?,
    @Json(name = "arrangement") val arrangement: Arrangement.Horizontal?,
    @Json(name = "margins") val modifier: Modifier?
) : BodyRowResponse {

    override val type: BodyRowType = BodyRowType.INFO_CARD

    override fun mapToDomain(): InfoCardModel = InfoCardModel(
        identifier = identifier ?: error("InfoCard identifier cannot be null"),
        icon = icon ?: error("InfoCard icon cannot be null"),
        title = title?.mapToDomain() ?: error("InfoCard title cannot be null"),
        pill = pill?.mapToDomain() ?: error("InfoCard pill cannot be null"),
        date = date?.mapToDomain(),
        chipSection = chipSection?.mapToDomain(),
        reportsDetail = reportsDetail?.mapToDomain(),
        arrangement = arrangement ?: Arrangement.Center,
        modifier = modifier ?: Modifier
    )
}
