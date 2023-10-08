package com.valkiria.uicomponents.model.ui.finding

import androidx.compose.ui.Modifier
import com.valkiria.uicomponents.model.props.TextModel

data class FindingDetailUiModel(
    val images: List<String>,
    val description: TextModel,
    val reporter: TextModel,
    val modifier: Modifier
)
