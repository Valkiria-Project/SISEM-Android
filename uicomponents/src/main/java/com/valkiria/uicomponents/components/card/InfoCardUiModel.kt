package com.valkiria.uicomponents.components.card

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.Modifier
import com.valkiria.uicomponents.components.BodyRowModel
import com.valkiria.uicomponents.components.BodyRowType
import com.valkiria.uicomponents.model.props.TextModel
import com.valkiria.uicomponents.bricks.chip.ChipSectionUiModel
import com.valkiria.uicomponents.model.ui.report.ReportsDetailUiModel

data class InfoCardUiModel(
    val identifier: String,
    val icon: String,
    val title: TextModel,
    val pill: PillUiModel,
    val date: TextModel?,
    val chipSection: ChipSectionUiModel?,
    val reportsDetail: ReportsDetailUiModel?,
    val arrangement: Arrangement.Horizontal,
    val modifier: Modifier
) : BodyRowModel {

    override val type: BodyRowType = BodyRowType.INFO_CARD
}
