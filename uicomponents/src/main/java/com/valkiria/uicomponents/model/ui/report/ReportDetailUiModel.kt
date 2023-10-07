package com.valkiria.uicomponents.model.ui.report

import androidx.compose.ui.Modifier
import com.valkiria.uicomponents.model.props.TextModel

data class ReportDetailUiModel(
    val images: List<String>,
    val title: TextModel,
    val subtitle: TextModel,
    val description: TextModel,
    val modifier: Modifier
)
