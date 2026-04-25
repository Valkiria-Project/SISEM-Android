package com.valkiria.uicomponents.model.ui.card

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.Modifier
import com.valkiria.uicomponents.model.ui.report.ReportsDetailUiModel
import com.valkiria.uicomponents.model.ui.chip.ChipSectionUiModel
import com.valkiria.uicomponents.model.props.TextStyle

data class InfoCardUiModel(
    val identifier: String,
    val icon: String,
    val titleText: String,
    val titleTextStyle: TextStyle,
    val pillText: String,
    val pillTextStyle: TextStyle,
    val pillColor: String,
    val dateText: String?,
    val dateTextStyle: TextStyle?,
    val chipSection: ChipSectionUiModel? = null,
    val reportsDetail: ReportsDetailUiModel?,
    val arrangement: Arrangement.Horizontal,
    val modifier: Modifier
)
