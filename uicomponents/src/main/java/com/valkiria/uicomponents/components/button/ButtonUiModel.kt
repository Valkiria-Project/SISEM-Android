package com.valkiria.uicomponents.components.button

import androidx.compose.ui.Modifier
import com.valkiria.uicomponents.props.ButtonSize
import com.valkiria.uicomponents.props.ButtonStyle

data class ButtonUiModel(
    val label: String,
    val style: ButtonStyle,
    val onClick: OnClick,
    val size: ButtonSize,
    val margins: Modifier
)
