package com.valkiria.uicomponents.components.inventorycheck

import androidx.compose.ui.Modifier

data class InventoryCheckUiModel(
    val identifier: String,
    val items: List<InventoryCheckItemUiModel>,
    val modifier: Modifier = Modifier
)
