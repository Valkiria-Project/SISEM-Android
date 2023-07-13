package com.valkiria.uicomponents.props.label

import com.valkiria.uicomponents.props.MarginsUiModel

data class LabelUiModel(
    val text: String,
    val style: LabelStyle,
    val margins: MarginsUiModel?
)
