package com.valkiria.uicomponents.components.card

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.Modifier
import com.valkiria.uicomponents.bricks.banner.report.ReportsDetailUiModel
import com.valkiria.uicomponents.bricks.chip.ChipSectionUiModel
import com.valkiria.uicomponents.components.BodyRowModel
import com.valkiria.uicomponents.components.BodyRowType
import com.valkiria.uicomponents.components.label.TextUiModel

data class InfoCardUiModel(
    override val identifier: String,
    val icon: String,
    val title: TextUiModel,
    val pill: PillUiModel,
    val date: TextUiModel?,
    val chipSection: ChipSectionUiModel?,
    val reportsDetail: ReportsDetailUiModel?,
    val visibility: Boolean = true,
    val required: Boolean = false,
    val arrangement: Arrangement.Horizontal,
    val modifier: Modifier
) : BodyRowModel {

    override val type: BodyRowType = BodyRowType.INFO_CARD
}
