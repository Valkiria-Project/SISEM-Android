package com.valkiria.uicomponents.bricks.banner.report

import androidx.compose.ui.Modifier
import com.valkiria.uicomponents.components.label.TextUiModel

data class ReportDetailUiModel(
    val images: List<String>,
    val title: TextUiModel,
    val subtitle: TextUiModel,
    val description: TextUiModel,
    val modifier: Modifier
)
