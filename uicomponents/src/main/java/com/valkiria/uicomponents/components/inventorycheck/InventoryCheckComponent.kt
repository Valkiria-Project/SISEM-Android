package com.valkiria.uicomponents.components.inventorycheck

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.valkiria.uicomponents.components.richlabel.RichLabelComponent
import com.valkiria.uicomponents.props.TabletWidth

@Composable
fun InventoryCheckComponent(
    uiModel: InventoryCheckUiModel,
    isTablet: Boolean = false,
    onAction: () -> Unit
) {
    Column(
        modifier = if (isTablet) {
            uiModel.modifier.width(TabletWidth)
        } else {
            uiModel.modifier.fillMaxWidth()
        },
    ) {
        uiModel.items.forEach { checkItemUiModel ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    modifier = Modifier
                        .weight(0.5f),
                    horizontalAlignment = Alignment.Start
                ) {
                    RichLabelComponent(uiModel = checkItemUiModel.name)
                }
                Column(
                    modifier = Modifier
                        .weight(0.25f),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(text = checkItemUiModel.registeredValueText)
                }
                Column(
                    modifier = Modifier
                        .weight(0.25f),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(text = checkItemUiModel.receivedValueText)
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ButtonComponentPreview() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.DarkGray)
    ) {
        // FIXME: Finish this
//        InventoryCheckComponent(uiModel = getLoginForgotButtonUiModel()) {
//            Timber.d("Button clicked")
//        }
//        InventoryCheckComponent(uiModel = getLoginButtonUiModel()) {
//            Timber.d("Button clicked")
//        }
    }
}
