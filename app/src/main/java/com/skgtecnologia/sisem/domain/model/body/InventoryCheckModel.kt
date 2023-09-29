package com.skgtecnologia.sisem.domain.model.body

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.Modifier
import com.skgtecnologia.sisem.domain.model.props.TextModel
import com.valkiria.uicomponents.model.ui.inventorycheck.InventoryCheckItemUiModel
import com.valkiria.uicomponents.model.ui.inventorycheck.InventoryCheckUiModel
import com.valkiria.uicomponents.model.ui.textfield.ValidationUiModel

data class InventoryCheckModel(
    val identifier: String,
    val registered: TextModel,
    val received: TextModel,
    val items: List<InventoryCheckItemUiModel>,
    val validations: List<ValidationUiModel>,
    val arrangement: Arrangement.Horizontal,
    val modifier: Modifier = Modifier
) : BodyRowModel {

    override val type: BodyRowType = BodyRowType.INVENTORY_CHECK
}

fun InventoryCheckModel.mapToUiModel() = InventoryCheckUiModel(
    identifier = identifier,
    registeredText = registered.text,
    registeredTextStyle = registered.textStyle,
    receivedText = received.text,
    receivedTextStyle = received.textStyle,
    items = items,
    validations = validations,
    modifier = modifier
)
