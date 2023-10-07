package com.skgtecnologia.sisem.data.remote.model.body

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.Modifier
import com.skgtecnologia.sisem.data.remote.model.bricks.InventoryCheckItemResponse
import com.skgtecnologia.sisem.data.remote.model.bricks.ValidationResponse
import com.skgtecnologia.sisem.data.remote.model.bricks.mapToUi
import com.skgtecnologia.sisem.data.remote.model.props.TextResponse
import com.skgtecnologia.sisem.data.remote.model.props.mapToDomain
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import com.valkiria.uicomponents.model.ui.body.BodyRowType
import com.valkiria.uicomponents.model.ui.body.InventoryCheckUiModel

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
        registered = registered?.mapToDomain()
            ?: error("InventoryCheck registered cannot be null"),
        received = received?.mapToDomain() ?: error("InventoryCheck received cannot be null"),
        items = items?.map { it.mapToUi() } ?: error("InventoryCheck items cannot be null"),
        validations = validations?.map { it.mapToUi() }
            ?: error("InventoryCheck validations cannot be null"),
        arrangement = arrangement ?: Arrangement.Center,
        modifier = modifier ?: Modifier
    )
}
