package com.valkiria.uicomponents.components.inventorysearch

import com.valkiria.uicomponents.components.label.TextUiModel
import com.valkiria.uicomponents.components.richlabel.RichLabelUiModel

data class InventoryItemUiModel(
    val name: RichLabelUiModel,
    val assigned: TextUiModel,
    val stock: TextUiModel
)
