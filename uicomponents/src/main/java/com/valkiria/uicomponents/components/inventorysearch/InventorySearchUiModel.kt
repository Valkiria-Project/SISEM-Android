package com.valkiria.uicomponents.components.inventorysearch

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.Modifier
import com.valkiria.uicomponents.components.BodyRowModel
import com.valkiria.uicomponents.components.BodyRowType
import com.valkiria.uicomponents.components.label.TextUiModel

data class InventorySearchUiModel(
    override val identifier: String,
    val title: TextUiModel,
    val icon: String,
    val inventoryItems: List<InventoryItemUiModel>?,
    val visibility: Boolean = true,
    val required: Boolean = false,
    val arrangement: Arrangement.Horizontal,
    val modifier: Modifier
) : BodyRowModel {

    override val type: BodyRowType = BodyRowType.INVENTORY_SEARCH
}
