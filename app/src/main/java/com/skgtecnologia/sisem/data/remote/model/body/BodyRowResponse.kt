package com.skgtecnologia.sisem.data.remote.model.body

import com.skgtecnologia.sisem.domain.model.body.BodyRowModel
import com.skgtecnologia.sisem.domain.model.body.BodyRowType

sealed interface BodyRowResponse {
    val type: BodyRowType

    fun mapToDomain(): BodyRowModel
}
