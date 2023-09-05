package com.valkiria.uicomponents.model.ui.inventorycheck

import androidx.compose.ui.Modifier
import com.valkiria.uicomponents.model.props.TextStyle
import com.valkiria.uicomponents.model.ui.inventorycheck.InventoryCheckItemUiModel

data class InventoryCheckUiModel(
    val identifier: String,
    val registeredText: String,
    val registeredTextStyle: TextStyle,
    val receivedText: String,
    val receivedTextStyle: TextStyle,
    val items: List<InventoryCheckItemUiModel>,
    val modifier: Modifier = Modifier
)
