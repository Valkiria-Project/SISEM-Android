package com.skgtecnologia.sisem.data.model.bodyrow

import com.skgtecnologia.sisem.domain.model.bodyrow.BodyRowModel
import com.skgtecnologia.sisem.domain.model.bodyrow.BodyRowType
import com.skgtecnologia.sisem.domain.model.bodyrow.ButtonModel

data class ButtonResponse(
    val label: String? = null
) : BodyRowResponse {

    override val type: BodyRowType = BodyRowType.BUTTON

    override fun mapToDomain(): BodyRowModel = ButtonModel(
        label = label ?: error("label cannot be null")
    )
}
