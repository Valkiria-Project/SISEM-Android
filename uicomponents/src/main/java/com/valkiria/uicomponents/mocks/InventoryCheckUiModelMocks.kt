package com.valkiria.uicomponents.mocks

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.Modifier
import com.valkiria.uicomponents.components.inventorycheck.InventoryCheckItemUiModel
import com.valkiria.uicomponents.components.inventorycheck.InventoryCheckUiModel
import com.valkiria.uicomponents.components.label.TextStyle
import com.valkiria.uicomponents.components.label.TextUiModel
import com.valkiria.uicomponents.components.textfield.ValidationUiModel

fun getPreOperationalInventoryCheckUiModel(): InventoryCheckUiModel {
    val items = listOf(
        InventoryCheckItemUiModel(
            name = getAlprazolamRichLabelUiModel(),
            registeredValueText = "7",
            registeredValueTextStyle = TextStyle.HEADLINE_6,
            receivedValueText = "",
            receivedValueTextStyle = TextStyle.HEADLINE_6,
        ),
        InventoryCheckItemUiModel(
            name = getDiazepamRichLabelUiModel(),
            registeredValueText = "7",
            registeredValueTextStyle = TextStyle.HEADLINE_6,
            receivedValueText = "",
            receivedValueTextStyle = TextStyle.HEADLINE_6,
        )
    )

    return InventoryCheckUiModel(
        identifier = "PRE_OP_INVENTORY_CHECK",
        registered = TextUiModel(
            "Registrado",
            TextStyle.BODY_1,
        ),
        received = TextUiModel(
            "Recibido",
            TextStyle.BODY_1,
        ),
        items = items,
        validations = listOf(
            ValidationUiModel(
                regex = "^(?!\\s*$).+",
                message = "El campo no debe estar vacío"
            )
        ),
        arrangement = Arrangement.Center,
        modifier = Modifier
    )
}
