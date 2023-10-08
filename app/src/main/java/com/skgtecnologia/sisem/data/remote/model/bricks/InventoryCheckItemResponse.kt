package com.skgtecnologia.sisem.data.remote.model.bricks

import com.skgtecnologia.sisem.data.remote.model.body.RichLabelResponse
import com.skgtecnologia.sisem.data.remote.model.props.TextResponse
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import com.valkiria.uicomponents.components.inventorycheck.InventoryCheckItemUiModel

@JsonClass(generateAdapter = true)
data class InventoryCheckItemResponse(
    @Json(name = "name") val name: RichLabelResponse?,
    @Json(name = "registered_value") val registeredValue: TextResponse?,
    @Json(name = "received_value") val receivedValue: TextResponse?
)

fun InventoryCheckItemResponse.mapToUi(): InventoryCheckItemUiModel = InventoryCheckItemUiModel(
    name = name?.mapToUi() ?: error("InventoryCheckItem name cannot be null"),
    registeredValueText = registeredValue?.text
        ?: error("InventoryCheckItem registeredValueText cannot be null"),
    registeredValueTextStyle = registeredValue.textStyle
        ?: error("InventoryCheckItem registeredValueTextStyle cannot be null"),
    receivedValueText = receivedValue?.text
        ?: error("InventoryCheckItem receivedValueText cannot be null"),
    receivedValueTextStyle = receivedValue.textStyle
        ?: error("InventoryCheckItem receivedValueTextStyle cannot be null")
)
