package com.valkiria.uicomponents.model.ui.body

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.Modifier
import com.valkiria.uicomponents.model.props.TextModel
import com.valkiria.uicomponents.model.ui.chip.ChipSectionUiModel
import com.valkiria.uicomponents.model.ui.pill.PillUiModel

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
