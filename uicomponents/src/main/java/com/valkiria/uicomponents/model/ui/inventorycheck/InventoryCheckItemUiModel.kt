package com.valkiria.uicomponents.model.ui.inventorycheck

import com.valkiria.uicomponents.model.ui.richlabel.RichLabelUiModel
import com.valkiria.uicomponents.model.props.TextStyle

data class InventoryCheckItemUiModel(
    val name: RichLabelUiModel,
    val registeredValueText: String,
    val registeredValueTextStyle: TextStyle,
    val receivedValueText: String,
    val receivedValueTextStyle: TextStyle
)