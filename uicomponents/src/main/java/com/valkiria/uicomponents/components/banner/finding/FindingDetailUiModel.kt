package com.valkiria.uicomponents.components.banner.finding

import androidx.compose.ui.Modifier
import com.valkiria.uicomponents.components.label.TextUiModel

data class FindingDetailUiModel(
    val images: List<String>,
    val description: TextUiModel,
    val reporter: TextUiModel,
    val modifier: Modifier
)
