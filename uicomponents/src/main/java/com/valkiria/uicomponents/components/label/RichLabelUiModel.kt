package com.valkiria.uicomponents.components.label

import com.valkiria.uicomponents.components.MarginsUiModel

data class RichLabelUiModel(
    val text: String,
    val style: LabelStyle,
    val margins: MarginsUiModel?
)