package com.valkiria.uicomponents.mocks

import com.valkiria.uicomponents.components.label.TextStyle
import com.valkiria.uicomponents.components.chip.FiltersUiModel

fun getPreOperationalFiltersUiModel(): FiltersUiModel {
    return FiltersUiModel(
        identifier = "PRE_OP_FILTERS",
        options = listOf("Líquidos", "Sistema eléctrico", "Interior", "Exterior"),
        textStyle = TextStyle.BUTTON_1
    )
}
