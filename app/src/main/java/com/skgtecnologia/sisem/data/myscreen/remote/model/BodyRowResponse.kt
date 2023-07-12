package com.skgtecnologia.sisem.data.myscreen.remote.model

import com.skgtecnologia.sisem.domain.core.model.bodyrow.BodyRowModel
import com.skgtecnologia.sisem.domain.core.model.bodyrow.BodyRowType

sealed interface BodyRowResponse {
    val type: BodyRowType

    fun mapToDomain(): BodyRowModel
}
