package com.valkiria.uicomponents.mocks

import com.valkiria.uicomponents.components.filters.FiltersUiModel
import com.valkiria.uicomponents.props.TextStyle

fun getPreOperationalFiltersUiModel(): FiltersUiModel {
    return FiltersUiModel(
        options = listOf("Líquidos", "Sistema eléctrico", "Interior", "Exterior"),
        textStyle = TextStyle.BUTTON_1
    )
}
