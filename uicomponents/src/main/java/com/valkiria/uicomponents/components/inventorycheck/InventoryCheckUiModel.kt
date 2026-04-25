package com.valkiria.uicomponents.components.inventorycheck

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.Modifier
import com.valkiria.uicomponents.components.BodyRowModel
import com.valkiria.uicomponents.components.BodyRowType
import com.valkiria.uicomponents.components.label.TextUiModel
import com.valkiria.uicomponents.components.textfield.ValidationUiModel

data class InventoryCheckUiModel(
    override val identifier: String,
    val registered: TextUiModel,
    val received: TextUiModel,
    val items: List<InventoryCheckItemUiModel>,
    val validations: List<ValidationUiModel>,
    val visibility: Boolean = true,
    val required: Boolean = false,
    val arrangement: Arrangement.Horizontal,
    val modifier: Modifier = Modifier
) : BodyRowModel {

    override val type: BodyRowType = BodyRowType.INVENTORY_CHECK
}
