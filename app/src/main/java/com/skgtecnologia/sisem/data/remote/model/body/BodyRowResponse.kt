package com.skgtecnologia.sisem.data.remote.model.body

import com.valkiria.uicomponents.components.body.BodyRowModel
import com.valkiria.uicomponents.components.BodyRowType

sealed interface BodyRowResponse {
    val type: BodyRowType

    fun mapToUi(): com.valkiria.uicomponents.components.body.BodyRowModel
}
