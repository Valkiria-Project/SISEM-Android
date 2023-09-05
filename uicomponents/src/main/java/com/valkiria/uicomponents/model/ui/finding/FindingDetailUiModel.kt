package com.valkiria.uicomponents.model.ui.finding

import androidx.compose.ui.Modifier
import com.valkiria.uicomponents.model.props.TextStyle

data class FindingDetailUiModel(
    val images: List<String>,
    val descriptionText: String,
    val descriptionTextStyle: TextStyle,
    val reporterText: String,
    val reporterTextStyle: TextStyle,
    val modifier: Modifier
)
