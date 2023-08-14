package com.skgtecnologia.sisem.domain.model.body

import androidx.compose.ui.Modifier
import com.valkiria.uicomponents.components.finding.FindingUiModel

data class FindingModel(
    val identifier: String,
    val option: SegmentedSwitchModel,
    val modifier: Modifier = Modifier
) : BodyRowModel {

    override val type: BodyRowType = BodyRowType.FINDING
}

fun FindingModel.mapToUiModel() = FindingUiModel(
    option = option.mapToUiModel(),
    modifier = modifier
)
