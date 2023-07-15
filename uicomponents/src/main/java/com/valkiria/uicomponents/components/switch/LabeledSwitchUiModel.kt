package com.valkiria.uicomponents.components.switch

import androidx.compose.ui.Modifier
import com.valkiria.uicomponents.props.TextStyle

data class LabeledSwitchUiModel(
    val text: String,
    val textStyle: TextStyle,
    val modifier: Modifier = Modifier
)
