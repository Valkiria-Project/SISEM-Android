package com.valkiria.uicomponents.components.body

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.Modifier
import com.valkiria.uicomponents.components.BodyRowType
import com.valkiria.uicomponents.model.props.TextModel
import com.valkiria.uicomponents.model.ui.inventorycheck.InventoryCheckItemUiModel
import com.valkiria.uicomponents.model.ui.textfield.ValidationUiModel

data class InventoryCheckUiModel(
    val identifier: String,
    val registered: TextModel,
    val received: TextModel,
    val items: List<InventoryCheckItemUiModel>,
    val validations: List<ValidationUiModel>,
    val arrangement: Arrangement.Horizontal,
    val modifier: Modifier = Modifier
) : com.valkiria.uicomponents.components.body.BodyRowModel {

    override val type: BodyRowType = BodyRowType.INVENTORY_CHECK
}
