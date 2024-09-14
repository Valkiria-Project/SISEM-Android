package com.skgtecnologia.sisem.core.data.remote.model.screen

import com.valkiria.uicomponents.components.BodyRowModel
import com.valkiria.uicomponents.components.BodyRowType

interface BodyRowResponse {
    val type: BodyRowType

    fun mapToUi(): BodyRowModel
}
