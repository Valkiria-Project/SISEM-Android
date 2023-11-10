package com.skgtecnologia.sisem.data.remote.model.components.inventorysearch

import com.skgtecnologia.sisem.data.remote.model.components.label.TextResponse
import com.skgtecnologia.sisem.data.remote.model.components.label.mapToUi
import com.skgtecnologia.sisem.data.remote.model.components.richlabel.RichLabelResponse
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import com.valkiria.uicomponents.components.inventorysearch.InventoryItemUiModel

@JsonClass(generateAdapter = true)
data class InventoryItemResponse(
    @Json(name = "name") val name: RichLabelResponse?,
    @Json(name = "assigned") val assigned: TextResponse?,
    @Json(name = "stock") val stock: TextResponse?
)

fun InventoryItemResponse.mapToUi(): InventoryItemUiModel = InventoryItemUiModel(
    name = name?.mapToUi() ?: error("InventoryItem name cannot be null"),
    assigned = assigned?.mapToUi() ?: error("InventoryItem assigned cannot be null"),
    stock = stock?.mapToUi() ?: error("InventoryItem stock cannot be null"),
)
