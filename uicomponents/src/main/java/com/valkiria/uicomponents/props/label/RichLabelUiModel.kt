package com.valkiria.uicomponents.props.label

import com.valkiria.uicomponents.props.MarginsUiModel
import com.valkiria.uicomponents.props.TextStyleUi

data class RichLabelUiModel(
    val text: String,
    val textStyle: TextStyleUi,
    val margins: MarginsUiModel?
)
