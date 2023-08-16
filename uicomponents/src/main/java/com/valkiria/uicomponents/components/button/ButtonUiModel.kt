package com.valkiria.uicomponents.components.button

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.Modifier
import com.valkiria.uicomponents.props.ButtonSize
import com.valkiria.uicomponents.props.ButtonStyle
import com.valkiria.uicomponents.props.TextStyle

data class ButtonUiModel(
    val label: String,
    val style: ButtonStyle,
    val textStyle: TextStyle,
    val onClick: OnClick,
    val size: ButtonSize,
    val arrangement: Arrangement.Horizontal,
    val modifier: Modifier
)
