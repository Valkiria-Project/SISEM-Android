package com.skgtecnologia.sisem.data.remote.model.components

import com.valkiria.uicomponents.components.BodyRowModel
import com.valkiria.uicomponents.components.BodyRowType

interface BodyRowResponse {
    val type: BodyRowType

    fun mapToUi(): BodyRowModel
}
