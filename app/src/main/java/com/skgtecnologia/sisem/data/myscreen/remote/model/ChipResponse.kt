package com.skgtecnologia.sisem.data.myscreen.remote.model

import com.skgtecnologia.sisem.domain.myscreen.model.BodyRowModel
import com.skgtecnologia.sisem.domain.myscreen.model.BodyRowType
import com.skgtecnologia.sisem.domain.myscreen.model.ChipModel
import com.skgtecnologia.sisem.domain.myscreen.model.ChipStyle

data class ChipResponse(
    val icon: String?,
    val text: String?,
    val style: ChipStyle?,
    val margins: MarginsResponse?
) : BodyRowResponse {

        override val type: BodyRowType = BodyRowType.CHIP

        override fun mapToDomain(): BodyRowModel = ChipModel(
            icon = icon,
            text = text ?: error("Chip text cannot be null"),
            style = style ?: error("Chip style cannot be null"),
            margins = margins?.mapToDomain()
        )
}
