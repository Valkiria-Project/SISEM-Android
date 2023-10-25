package com.skgtecnologia.sisem.data.remote.model.components.inventorycheck

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.Modifier
import com.skgtecnologia.sisem.data.remote.model.components.BodyRowResponse
import com.skgtecnologia.sisem.data.remote.model.components.label.TextResponse
import com.skgtecnologia.sisem.data.remote.model.components.label.mapToUi
import com.skgtecnologia.sisem.data.remote.model.components.textfield.ValidationResponse
import com.skgtecnologia.sisem.data.remote.model.components.textfield.mapToUi
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import com.valkiria.uicomponents.components.BodyRowType
import com.valkiria.uicomponents.components.inventorycheck.InventoryCheckUiModel

@JsonClass(generateAdapter = true)
data class InventoryCheckResponse(
    @Json(name = "identifier") val identifier: String?,
    @Json(name = "registered") val registered: TextResponse?,
    @Json(name = "received") val received: TextResponse?,
    @Json(name = "items") val items: List<InventoryCheckItemResponse>?,
    @Json(name = "validations") val validations: List<ValidationResponse>?,
    @Json(name = "arrangement") val arrangement: Arrangement.Horizontal?,
    @Json(name = "margins") val modifier: Modifier?
) : BodyRowResponse {

    override val type: BodyRowType = BodyRowType.INVENTORY_CHECK

    override fun mapToUi(): InventoryCheckUiModel = InventoryCheckUiModel(
        identifier = identifier ?: error("InventoryCheck identifier cannot be null"),
        registered = registered?.mapToUi()
            ?: error("InventoryCheck registered cannot be null"),
        received = received?.mapToUi() ?: error("InventoryCheck received cannot be null"),
        items = items?.map { it.mapToUi() } ?: error("InventoryCheck items cannot be null"),
        validations = validations?.map { it.mapToUi() }
            ?: error("InventoryCheck validations cannot be null"),
        arrangement = arrangement ?: Arrangement.Center,
        modifier = modifier ?: Modifier
    )
}
