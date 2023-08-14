package com.skgtecnologia.sisem.domain.model.body

import androidx.compose.ui.Modifier

data class InventoryCheckModel(
    val identifier: String,
    val modifier: Modifier = Modifier
) : BodyRowModel {

    override val type: BodyRowType = BodyRowType.INVENTORY_CHECK
}
