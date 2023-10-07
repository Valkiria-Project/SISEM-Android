package com.skgtecnologia.sisem.data.remote.model.body

import com.valkiria.uicomponents.model.ui.body.BodyRowModel
import com.valkiria.uicomponents.model.ui.body.BodyRowType

sealed interface BodyRowResponse {
    val type: BodyRowType

    fun mapToUi(): BodyRowModel
}
