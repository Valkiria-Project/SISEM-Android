package com.skgtecnologia.sisem.domain.model.body

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.Modifier
import com.valkiria.uicomponents.components.inventorycheck.InventoryCheckItemUiModel
import com.valkiria.uicomponents.components.inventorycheck.InventoryCheckUiModel

data class InventoryCheckModel(
    val identifier: String,
    val items: List<InventoryCheckItemUiModel>,
    val arrangement: Arrangement.Horizontal,
    val modifier: Modifier = Modifier
) : BodyRowModel {

    override val type: BodyRowType = BodyRowType.INVENTORY_CHECK
}

fun InventoryCheckModel.mapToUiModel() = InventoryCheckUiModel(
    identifier = identifier,
    items = items,
    modifier = modifier
)
