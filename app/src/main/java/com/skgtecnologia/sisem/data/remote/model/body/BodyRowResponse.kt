package com.skgtecnologia.sisem.data.remote.model.body

import com.valkiria.uicomponents.components.BodyRowModel
import com.valkiria.uicomponents.components.BodyRowType

sealed interface BodyRowResponse {
    val type: BodyRowType

    fun mapToUi(): BodyRowModel
}
