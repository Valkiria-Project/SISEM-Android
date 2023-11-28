package com.valkiria.uicomponents.bricks.chip

import com.valkiria.uicomponents.components.label.ListPatientUiModel
import com.valkiria.uicomponents.components.label.ListTextUiModel
import com.valkiria.uicomponents.components.label.TextUiModel

data class ChipSectionUiModel(
    val title: TextUiModel,
    val listText: ListTextUiModel? = null,
    val listPatient: ListPatientUiModel? = null
)
