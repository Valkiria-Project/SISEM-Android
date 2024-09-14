package com.skgtecnologia.sisem.core.data.remote.model.components.inventorysearch

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.Modifier
import com.skgtecnologia.sisem.core.data.remote.model.components.label.TextResponse
import com.skgtecnologia.sisem.core.data.remote.model.components.label.mapToUi
import com.skgtecnologia.sisem.core.data.remote.model.screen.BodyRowResponse
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import com.valkiria.uicomponents.components.BodyRowType
import com.valkiria.uicomponents.components.inventorysearch.InventorySearchUiModel

@JsonClass(generateAdapter = true)
data class InventorySearchResponse(
    @Json(name = "identifier") val identifier: String?,
    @Json(name = "title") val title: TextResponse?,
    @Json(name = "icon") val icon: String?,
    @Json(name = "inventory_items") val inventoryItems: List<InventoryItemResponse>?,
    @Json(name = "visibility") val visibility: Boolean?,
    @Json(name = "required") val required: Boolean?,
    @Json(name = "arrangement") val arrangement: Arrangement.Horizontal?,
    @Json(name = "margins") val modifier: Modifier?
) : BodyRowResponse {

    override val type: BodyRowType = BodyRowType.INVENTORY_SEARCH

    override fun mapToUi(): InventorySearchUiModel = InventorySearchUiModel(
        identifier = identifier ?: error("InventorySearch identifier cannot be null"),
        title = title?.mapToUi() ?: error("InventorySearch title cannot be null"),
        icon = icon ?: error("InventorySearch icon cannot be null"),
        inventoryItems = inventoryItems?.map { it.mapToUi() },
        visibility = visibility ?: true,
        required = required ?: false,
        arrangement = arrangement ?: Arrangement.Center,
        modifier = modifier ?: Modifier
    )
}
