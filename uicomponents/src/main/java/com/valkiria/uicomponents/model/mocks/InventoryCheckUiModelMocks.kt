@file:Suppress("MagicNumber")

package com.valkiria.uicomponents.model.mocks

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.Modifier
import com.valkiria.uicomponents.components.body.InventoryCheckUiModel
import com.valkiria.uicomponents.model.props.TextModel
import com.valkiria.uicomponents.model.props.TextStyle
import com.valkiria.uicomponents.model.ui.inventorycheck.InventoryCheckItemUiModel
import com.valkiria.uicomponents.model.ui.textfield.ValidationUiModel
import kotlin.random.Random

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
        identifier = Random(100).toString(),
        registered = TextModel(
            "Registrado",
            TextStyle.BODY_1,
        ),
        received = TextModel(
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
