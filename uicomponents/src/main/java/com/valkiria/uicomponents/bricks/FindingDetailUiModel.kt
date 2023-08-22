package com.valkiria.uicomponents.bricks

import androidx.compose.ui.Modifier
import com.valkiria.uicomponents.props.TextStyle

data class FindingDetailUiModel(
    val images: List<String>,
    val descriptionText: String,
    val descriptionTextStyle: TextStyle,
    val reporterText: String,
    val reporterTextStyle: TextStyle,
    val modifier: Modifier
)
