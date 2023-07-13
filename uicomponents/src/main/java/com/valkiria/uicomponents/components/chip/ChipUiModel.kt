package com.valkiria.uicomponents.components.chip

import com.valkiria.uicomponents.props.ChipStyleUi
import com.valkiria.uicomponents.props.MarginsUiModel
import com.valkiria.uicomponents.props.TextStyleUi

data class ChipUiModel(
    val icon: String?,
    val text: String?,
    val textStyle: TextStyleUi,
    val style: ChipStyleUi,
    val margins: MarginsUiModel?
)
