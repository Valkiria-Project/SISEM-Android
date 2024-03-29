package com.valkiria.uicomponents.components.inventorycheck

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.valkiria.uicomponents.bricks.textfield.DigitsTextFieldView
import com.valkiria.uicomponents.components.label.toTextStyle
import com.valkiria.uicomponents.components.richlabel.RichLabelComponent
import com.valkiria.uicomponents.components.textfield.InputUiModel
import com.valkiria.uicomponents.mocks.getPreOperationalInventoryCheckUiModel
import com.valkiria.uicomponents.utlis.FORTY_PERCENT_WEIGHT
import com.valkiria.uicomponents.utlis.THIRTY_PERCENT_WEIGHT
import timber.log.Timber

@Suppress("UnusedPrivateMember")
@Composable
fun InventoryCheckComponent(
    uiModel: InventoryCheckUiModel,
    validateFields: Boolean = false,
    onAction: (inputUiModel: InputUiModel) -> Unit
) {
    Column(
        modifier = uiModel.modifier.fillMaxWidth()
    ) {
        InventoryHeaderRow(uiModel)

        uiModel.items.forEach { checkItemUiModel ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .defaultMinSize(minHeight = 80.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    modifier = Modifier
                        .weight(FORTY_PERCENT_WEIGHT),
                    horizontalAlignment = Alignment.Start,
                    verticalArrangement = Arrangement.Top
                ) {
                    RichLabelComponent(uiModel = checkItemUiModel.name)
                }
                Column(
                    modifier = Modifier
                        .weight(THIRTY_PERCENT_WEIGHT),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = checkItemUiModel.registeredValueText,
                        style = checkItemUiModel.registeredValueTextStyle.toTextStyle()
                    )
                }
                Column(
                    modifier = Modifier
                        .weight(THIRTY_PERCENT_WEIGHT),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    DigitsTextFieldView(
                        identifier = checkItemUiModel.name.identifier,
                        value = checkItemUiModel.receivedValueText,
                        style = checkItemUiModel.registeredValueTextStyle.toTextStyle(),
                        validations = uiModel.validations,
                        validateFields = validateFields
                    ) { id, updatedValue, fieldValidated ->
                        onAction(InputUiModel(id, updatedValue, fieldValidated))
                    }
                }
            }
        }
    }
}

@Composable
private fun InventoryHeaderRow(uiModel: InventoryCheckUiModel) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Spacer(
            modifier = Modifier
                .weight(FORTY_PERCENT_WEIGHT)
        )
        Column(
            modifier = Modifier
                .weight(THIRTY_PERCENT_WEIGHT),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = uiModel.registered.text,
                style = uiModel.registered.textStyle.toTextStyle()
            )
        }
        Column(
            modifier = Modifier
                .weight(THIRTY_PERCENT_WEIGHT),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = uiModel.received.text,
                style = uiModel.received.textStyle.toTextStyle()
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun InventoryCheckComponentPreview() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.DarkGray)
    ) {
        InventoryCheckComponent(uiModel = getPreOperationalInventoryCheckUiModel()) { _ ->
            Timber.d("Inventory action")
        }
    }
}
