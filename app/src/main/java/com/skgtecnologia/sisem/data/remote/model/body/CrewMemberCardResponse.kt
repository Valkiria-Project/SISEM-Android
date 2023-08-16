package com.skgtecnologia.sisem.data.remote.model.body

import androidx.compose.ui.Modifier
import com.skgtecnologia.sisem.data.remote.model.bricks.ChipSectionResponse
import com.skgtecnologia.sisem.data.remote.model.bricks.PillResponse
import com.skgtecnologia.sisem.data.remote.model.bricks.ReportsDetailResponse
import com.skgtecnologia.sisem.data.remote.model.bricks.mapToDomain
import com.skgtecnologia.sisem.data.remote.model.props.TextResponse
import com.skgtecnologia.sisem.data.remote.model.props.mapToDomain
import com.skgtecnologia.sisem.domain.model.body.BodyRowType
import com.skgtecnologia.sisem.domain.model.body.CrewMemberCardModel
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CrewMemberCardResponse(
    @Json(name = "identifier") val identifier: String?,
    @Json(name = "icon") val icon: String?,
    @Json(name = "title") val title: TextResponse?,
    @Json(name = "pill") val pill: PillResponse?,
    @Json(name = "date") val date: TextResponse?,
    @Json(name = "chip_section") val chipSection: ChipSectionResponse?,
    @Json(name = "reports_detail") val reportsDetail: ReportsDetailResponse?,
    @Json(name = "margins") val modifier: Modifier?
) : BodyRowResponse {

    override val type: BodyRowType = BodyRowType.CREW_MEMBER_CARD

    override fun mapToDomain(): CrewMemberCardModel = CrewMemberCardModel(
        identifier = identifier ?: error("CrewMemberCard identifier cannot be null"),
        icon = icon ?: error("CrewMemberCard icon cannot be null"),
        title = title?.mapToDomain() ?: error("CrewMemberCard title cannot be null"),
        pill = pill?.mapToDomain() ?: error("CrewMemberCard pill cannot be null"),
        date = date?.mapToDomain() ?: error("CrewMemberCard date cannot be null"),
        chipSection = chipSection?.mapToDomain(),
        reportsDetail = reportsDetail?.mapToDomain(),
        modifier = modifier ?: Modifier
    )
}
