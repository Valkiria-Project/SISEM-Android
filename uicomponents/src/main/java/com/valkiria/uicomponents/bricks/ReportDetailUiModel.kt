package com.valkiria.uicomponents.bricks

import androidx.compose.ui.Modifier
import com.valkiria.uicomponents.props.TextStyle

data class ReportDetailUiModel(
    val images: List<String>,
    val title: String,
    val titleTextStyle: TextStyle,
    val subtitle: String,
    val subtitleTextStyle: TextStyle,
    val description: String,
    val descriptionTextStyle: TextStyle,
    val modifier: Modifier
)