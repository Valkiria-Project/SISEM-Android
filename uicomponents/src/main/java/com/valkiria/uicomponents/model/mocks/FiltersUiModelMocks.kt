package com.valkiria.uicomponents.model.mocks

import com.valkiria.uicomponents.model.props.TextStyle
import com.valkiria.uicomponents.model.ui.body.FiltersUiModel

fun getPreOperationalFiltersUiModel(): FiltersUiModel {
    return FiltersUiModel(
        identifier = "PRE_OP_FILTERS",
        options = listOf("Líquidos", "Sistema eléctrico", "Interior", "Exterior"),
        textStyle = TextStyle.BUTTON_1
    )
}
