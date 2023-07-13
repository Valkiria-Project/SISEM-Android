package com.valkiria.uicomponents.components.richlabel

import com.valkiria.uicomponents.props.MarginsUiModel
import com.valkiria.uicomponents.props.TextStyleUi

data class RichLabelUiModel(
    val text: String,
    val textStyle: TextStyleUi,
    val margins: MarginsUiModel?
)
