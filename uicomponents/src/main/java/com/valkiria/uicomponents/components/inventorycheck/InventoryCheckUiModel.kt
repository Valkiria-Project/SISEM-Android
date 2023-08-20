package com.valkiria.uicomponents.components.inventorycheck

import androidx.compose.ui.Modifier
import com.valkiria.uicomponents.props.TextStyle

data class InventoryCheckUiModel(
    val identifier: String,
    val registeredText: String,
    val registeredTextStyle: TextStyle,
    val receivedText: String,
    val receivedTextStyle: TextStyle,
    val items: List<InventoryCheckItemUiModel>,
    val modifier: Modifier = Modifier
)
