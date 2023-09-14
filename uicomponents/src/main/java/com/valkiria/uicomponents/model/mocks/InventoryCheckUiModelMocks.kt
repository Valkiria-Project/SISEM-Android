@file:Suppress("MagicNumber")

package com.valkiria.uicomponents.model.mocks

import androidx.compose.ui.Modifier
import com.valkiria.uicomponents.model.ui.inventorycheck.InventoryCheckItemUiModel
import com.valkiria.uicomponents.model.ui.inventorycheck.InventoryCheckUiModel
import com.valkiria.uicomponents.model.props.TextStyle
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
        registeredText = "Registrado",
        registeredTextStyle = TextStyle.BODY_1,
        receivedText = "Recibido",
        receivedTextStyle = TextStyle.BODY_1,
        items = items,
        modifier = Modifier
    )
}
