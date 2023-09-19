package com.valkiria.uicomponents.model.ui.chip

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.Modifier
import com.valkiria.uicomponents.model.props.TextStyle

data class ChipSelectionUiModel(
    val identifier: String,
    val titleText: String? = null,
    val titleTextStyle: TextStyle? = null,
    val items: List<ChipSelectionItemUiModel>,
    val selected: String? = null,
    val arrangement: Arrangement.Horizontal,
    val modifier: Modifier
)
