package com.skgtecnologia.sisem.data.remote.model.bodyrow

import com.skgtecnologia.sisem.domain.model.bodyrow.BodyRowModel
import com.skgtecnologia.sisem.domain.model.bodyrow.BodyRowType

sealed interface BodyRowResponse {
    val type: BodyRowType

    fun mapToDomain(): BodyRowModel
}
