package com.valkiria.uicomponents.components.inventorycheck

import com.valkiria.uicomponents.components.richlabel.RichLabelUiModel
import com.valkiria.uicomponents.components.label.TextStyle

data class InventoryCheckItemUiModel(
    val name: RichLabelUiModel,
    val registeredValueText: String,
    val registeredValueTextStyle: TextStyle,
    val receivedValueText: String,
    val receivedValueTextStyle: TextStyle
)
