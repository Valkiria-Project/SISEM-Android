package com.skgtecnologia.sisem.core.data.remote.model.components.finding

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.Modifier
import com.skgtecnologia.sisem.core.data.remote.model.bricks.banner.finding.FindingsDetailResponse
import com.skgtecnologia.sisem.core.data.remote.model.bricks.banner.finding.mapToUi
import com.skgtecnologia.sisem.core.data.remote.model.components.segmentedswitch.SegmentedSwitchResponse
import com.skgtecnologia.sisem.core.data.remote.model.components.segmentedswitch.SegmentedValueResponse
import com.skgtecnologia.sisem.core.data.remote.model.components.segmentedswitch.mapToUi
import com.skgtecnologia.sisem.core.data.remote.model.screen.BodyRowResponse
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import com.valkiria.uicomponents.components.BodyRowType
import com.valkiria.uicomponents.components.finding.FindingUiModel

@JsonClass(generateAdapter = true)
data class FindingResponse(
    @Json(name = "identifier") val identifier: String?,
    @Json(name = "segmented_switch") val segmentedSwitch: SegmentedSwitchResponse?,
    @Json(name = "segmented_value") val segmentedValue: SegmentedValueResponse?,
    @Json(name = "finding_detail") val findingDetail: FindingsDetailResponse?,
    @Json(name = "read_only") val readOnly: Boolean?,
    @Json(name = "visibility") val visibility: Boolean?,
    @Json(name = "required") val required: Boolean?,
    @Json(name = "arrangement") val arrangement: Arrangement.Horizontal?,
    @Json(name = "margins") val modifier: Modifier?
) : BodyRowResponse {

    override val type: BodyRowType = BodyRowType.FINDING

    override fun mapToUi(): FindingUiModel = FindingUiModel(
        identifier = identifier ?: error("Finding identifier cannot be null"),
        segmentedSwitchUiModel = segmentedSwitch?.mapToUi(),
        segmentedValueUiModel = segmentedValue?.mapToUi(),
        readOnly = readOnly ?: false,
        findingDetail = findingDetail?.mapToUi(),
        visibility = visibility ?: true,
        required = required ?: false,
        arrangement = arrangement ?: Arrangement.Center,
        modifier = modifier ?: Modifier
    )
}
