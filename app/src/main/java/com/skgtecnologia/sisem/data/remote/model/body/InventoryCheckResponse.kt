package com.skgtecnologia.sisem.data.remote.model.body

import androidx.compose.ui.Modifier
import com.skgtecnologia.sisem.domain.model.body.BodyRowModel
import com.skgtecnologia.sisem.domain.model.body.BodyRowType
import com.skgtecnologia.sisem.domain.model.body.InventoryCheckModel
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class InventoryCheckResponse(
    @Json(name = "identifier") val identifier: String?,
    @Json(name = "margins") val modifier: Modifier?
) : BodyRowResponse {

    override val type: BodyRowType = BodyRowType.INVENTORY_CHECK

    override fun mapToDomain(): InventoryCheckModel = InventoryCheckModel(
        identifier = identifier ?: error("InventoryCheck identifier cannot be null"),
        modifier = modifier ?: Modifier
    )
}
