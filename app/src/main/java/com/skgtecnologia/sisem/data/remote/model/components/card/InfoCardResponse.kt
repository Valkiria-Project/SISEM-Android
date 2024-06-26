package com.skgtecnologia.sisem.data.remote.model.components.card

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.Modifier
import com.skgtecnologia.sisem.data.remote.model.bricks.banner.report.ReportsDetailResponse
import com.skgtecnologia.sisem.data.remote.model.bricks.banner.report.mapToUi
import com.skgtecnologia.sisem.data.remote.model.bricks.chip.ChipSectionResponse
import com.skgtecnologia.sisem.data.remote.model.bricks.chip.mapToUi
import com.skgtecnologia.sisem.data.remote.model.components.BodyRowResponse
import com.skgtecnologia.sisem.data.remote.model.components.label.TextResponse
import com.skgtecnologia.sisem.data.remote.model.components.label.mapToUi
import com.skgtecnologia.sisem.di.operation.OperationRole
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import com.valkiria.uicomponents.components.BodyRowType
import com.valkiria.uicomponents.components.card.InfoCardUiModel

@JsonClass(generateAdapter = true)
data class InfoCardResponse(
    @Json(name = "identifier") val identifier: String?,
    @Json(name = "icon") val icon: String?,
    @Json(name = "title") val title: TextResponse?,
    @Json(name = "pill") val pill: PillResponse?,
    @Json(name = "date") val date: TextResponse?,
    @Json(name = "time") val time: TextResponse?,
    @Json(name = "chip_section") val chipSection: ChipSectionResponse?,
    @Json(name = "reports_detail") val reportsDetail: ReportsDetailResponse?,
    @Json(name = "visibility") val visibility: Boolean?,
    @Json(name = "required") val required: Boolean?,
    @Json(name = "arrangement") val arrangement: Arrangement.Horizontal?,
    @Json(name = "margins") val modifier: Modifier?
) : BodyRowResponse {

    override val type: BodyRowType = BodyRowType.INFO_CARD

    override fun mapToUi(): InfoCardUiModel = InfoCardUiModel(
        identifier = identifier ?: error("InfoCard identifier cannot be null"),
        icon = icon ?: error("InfoCard icon cannot be null"),
        title = title?.mapToUi(getHumanizedRoleName()) ?: error("InfoCard title cannot be null"),
        pill = pill?.mapToUi(),
        date = date?.mapToUi(),
        time = time?.mapToUi(),
        chipSection = chipSection?.mapToUi(),
        reportsDetail = reportsDetail?.mapToUi(),
        visibility = visibility ?: true,
        required = required ?: false,
        arrangement = arrangement ?: Arrangement.Center,
        modifier = modifier ?: Modifier
    )

    private fun getHumanizedRoleName() =
        OperationRole.getRoleByName(title?.text.orEmpty())?.humanizedName ?: title?.text.orEmpty()
}
