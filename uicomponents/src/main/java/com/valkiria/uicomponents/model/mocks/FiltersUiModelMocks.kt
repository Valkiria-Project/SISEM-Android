package com.valkiria.uicomponents.model.mocks

import com.valkiria.uicomponents.components.filters.FiltersUiModel
import com.valkiria.uicomponents.model.props.TextStyle

fun getPreOperationalFiltersUiModel(): FiltersUiModel {
    return FiltersUiModel(
        options = listOf("Líquidos", "Sistema eléctrico", "Interior", "Exterior"),
        textStyle = TextStyle.BUTTON_1
    )
}
