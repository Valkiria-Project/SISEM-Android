package com.valkiria.uicomponents.components.inventorysearch

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.valkiria.uicomponents.components.label.TextStyle
import com.valkiria.uicomponents.components.label.TextUiModel
import com.valkiria.uicomponents.components.label.toTextStyle
import com.valkiria.uicomponents.components.richlabel.RichLabelComponent
import com.valkiria.uicomponents.components.richlabel.RichLabelUiModel
import com.valkiria.uicomponents.utlis.DefType
import com.valkiria.uicomponents.utlis.getResourceIdByName

private const val MAX_SIZE = 0.5f

@Suppress("LongMethod")
@Composable
fun InventorySearchComponent(
    uiModel: InventorySearchUiModel
) {
    var text by rememberSaveable(stateSaver = TextFieldValue.Saver) {
        mutableStateOf(TextFieldValue())
    }

    val iconResourceId = LocalContext.current.getResourceIdByName(
        uiModel.icon, DefType.DRAWABLE
    )

    Column(
        modifier = uiModel.modifier.fillMaxWidth(),
    ) {
        OutlinedTextField(
            value = text,
            onValueChange = { updatedValue ->
                text = updatedValue
            },
            modifier = Modifier
                .fillMaxWidth()
                .imePadding(),
            leadingIcon = {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.List,
                    contentDescription = "Lista de ${uiModel.title.text}"
                )
            },
            trailingIcon = {
                Icon(
                    imageVector = Icons.Filled.Search,
                    contentDescription = "Buscar ${uiModel.title.text}"
                )
            },
            maxLines = 1
        )

        Row(
            modifier = Modifier.padding(top = 36.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            iconResourceId?.let {
                Icon(
                    imageVector = ImageVector.vectorResource(id = iconResourceId),
                    contentDescription = uiModel.title.text,
                    modifier = Modifier
                        .padding(end = 16.dp)
                        .size(32.dp),
                    tint = MaterialTheme.colorScheme.primary,
                )
            }

            Text(
                text = uiModel.title.text,
                style = uiModel.title.textStyle.toTextStyle(),
            )
        }

        Row(
            modifier = Modifier
                .padding(top = 16.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {
            Text(
                modifier = Modifier.padding(end = 32.dp),
                text = "Asignada",
                style = TextStyle.BODY_1.toTextStyle(),
            )

            Text(
                text = "Stock",
                style = TextStyle.BODY_1.toTextStyle(),
            )
        }

        if (text.text.isNotEmpty()) {
            uiModel.inventoryItems?.filter { inventoryItem ->
                inventoryItem.name.text.contains(text.text, ignoreCase = true)
            }?.forEach { inventoryItem ->
                InventoryItemView(inventoryItem)
            }
        } else {
            uiModel.inventoryItems?.forEach { inventoryItem ->
                InventoryItemView(inventoryItem)
            }
        }
    }
}

@Composable
private fun InventoryItemView(inventoryItem: InventoryItemUiModel) {
    Row(
        modifier = Modifier
            .padding(top = 16.dp)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        RichLabelComponent(
            uiModel = inventoryItem.name.copy(
                modifier = inventoryItem.name.modifier.fillMaxWidth(MAX_SIZE)
            )
        )

        Text(
            modifier = Modifier.padding(end = 32.dp),
            text = inventoryItem.assigned.text,
            style = inventoryItem.assigned.textStyle.toTextStyle(),
        )

        Text(
            modifier = Modifier.padding(end = 16.dp),
            text = inventoryItem.stock.text,
            style = inventoryItem.stock.textStyle.toTextStyle(),
        )
    }
}

@Suppress("LongMethod")
@Preview(showBackground = true)
@Composable
fun InventorySearchComponentPreview() {
    InventorySearchComponent(
        uiModel = InventorySearchUiModel(
            identifier = "identifier",
            title = TextUiModel(
                text = "Herramientas",
                textStyle = TextStyle.HEADLINE_3
            ),
            icon = "ic_tools",
            inventoryItems = listOf(
                InventoryItemUiModel(
                    name = RichLabelUiModel(
                        identifier = "identifier",
                        text = "<font color=\"#000000\">Martillo de carpintero <b>SISEM</b></font>",
                        textStyle = TextStyle.BODY_1,
                        arrangement = Arrangement.Start,
                        modifier = Modifier.padding(
                            start = 20.dp,
                            top = 20.dp,
                            end = 20.dp,
                            bottom = 0.dp
                        )
                    ),
                    assigned = TextUiModel(
                        text = "9",
                        textStyle = TextStyle.BUTTON_2
                    ),
                    stock = TextUiModel(
                        text = "7",
                        textStyle = TextStyle.BUTTON_2
                    )
                ),
                InventoryItemUiModel(
                    name = RichLabelUiModel(
                        identifier = "identifier",
                        text = "<font color=\"#000000\">Martillo de carpintero <b>SISEM</b></font>",
                        textStyle = TextStyle.BODY_1,
                        arrangement = Arrangement.Start,
                        modifier = Modifier.padding(
                            start = 20.dp,
                            top = 20.dp,
                            end = 20.dp,
                            bottom = 0.dp
                        )
                    ),
                    assigned = TextUiModel(
                        text = "9",
                        textStyle = TextStyle.BUTTON_2
                    ),
                    stock = TextUiModel(
                        text = "7",
                        textStyle = TextStyle.BUTTON_2
                    )
                )
            ),
            arrangement = Arrangement.Center,
            modifier = Modifier.padding(horizontal = 20.dp)
        )
    )
}
