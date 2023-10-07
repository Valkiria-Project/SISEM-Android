package com.skgtecnologia.sisem.data.remote.model.body

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.Modifier
import com.skgtecnologia.sisem.data.remote.model.bricks.FindingsDetailResponse
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import com.valkiria.uicomponents.components.BodyRowType
import com.valkiria.uicomponents.components.body.FindingUiModel

@JsonClass(generateAdapter = true)
data class FindingResponse(
    @Json(name = "identifier") val identifier: String?,
    @Json(name = "segmented_switch") val segmentedSwitch: SegmentedSwitchResponse?,
    @Json(name = "finding_detail") val findingDetail: FindingsDetailResponse?,
    @Json(name = "arrangement") val arrangement: Arrangement.Horizontal?,
    @Json(name = "margins") val modifier: Modifier?
) : BodyRowResponse {

    override val type: BodyRowType = BodyRowType.FINDING

    override fun mapToUi(): com.valkiria.uicomponents.components.body.FindingUiModel =
        com.valkiria.uicomponents.components.body.FindingUiModel(
            identifier = identifier ?: error("Finding identifier cannot be null"),
            segmentedSwitchUiModel = segmentedSwitch?.mapToUi()
                ?: error("Finding segmentedSwitchModel cannot be null"),
            arrangement = arrangement ?: Arrangement.Center,
            modifier = modifier ?: Modifier
        )
}
