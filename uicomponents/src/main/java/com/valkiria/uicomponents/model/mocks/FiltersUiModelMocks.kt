package com.valkiria.uicomponents.model.mocks

import com.valkiria.uicomponents.model.props.TextStyle
import com.valkiria.uicomponents.components.body.FiltersUiModel

fun getPreOperationalFiltersUiModel(): com.valkiria.uicomponents.components.body.FiltersUiModel {
    return com.valkiria.uicomponents.components.body.FiltersUiModel(
        identifier = "PRE_OP_FILTERS",
        options = listOf("Líquidos", "Sistema eléctrico", "Interior", "Exterior"),
        textStyle = TextStyle.BUTTON_1
    )
}
