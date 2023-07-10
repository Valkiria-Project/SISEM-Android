package com.valkiria.uicomponents.components.button

import com.valkiria.uicomponents.components.MarginsUiModel

data class ButtonUiModel(
    val label: String,
    val style: ButtonStyle,
    val onClick: OnClickType,
    val margins: MarginsUiModel?
)
