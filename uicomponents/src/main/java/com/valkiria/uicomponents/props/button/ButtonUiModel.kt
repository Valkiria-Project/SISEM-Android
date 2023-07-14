package com.valkiria.uicomponents.props.button

import com.valkiria.uicomponents.props.MarginsUiModel

data class ButtonUiModel(
    val label: String,
    val style: ButtonStyle,
    val onClick: OnClick,
    val size: ButtonSize,
    val margins: MarginsUiModel?
)
