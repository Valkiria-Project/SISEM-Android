package com.skgtecnologia.sisem.data.myscreen.remote.model

import com.skgtecnologia.sisem.domain.myscreen.model.BodyRowModel
import com.skgtecnologia.sisem.domain.myscreen.model.BodyRowType
import com.skgtecnologia.sisem.domain.myscreen.model.LabelModel
import com.skgtecnologia.sisem.domain.myscreen.model.LabelStyle

data class LabelResponse(
    val text: String?,
    val style: LabelStyle?,
    val margins: MarginsResponse?
) : BodyRowResponse {

    override val type: BodyRowType = BodyRowType.LABEL

    override fun mapToDomain(): BodyRowModel = LabelModel(
        text = text ?: error("Label text cannot be null"),
        style = style ?: error("Label style cannot be null"),
        margins = margins?.mapToDomain()
    )
}
