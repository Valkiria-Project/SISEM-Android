package com.skgtecnologia.sisem.data.myscreen.remote.model

import com.skgtecnologia.sisem.domain.myscreen.model.BodyRowModel
import com.skgtecnologia.sisem.domain.myscreen.model.BodyRowType
import com.skgtecnologia.sisem.domain.myscreen.model.RichLabelModel
import com.skgtecnologia.sisem.domain.myscreen.model.LabelStyle
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class RichLabelResponse(
    val text: String?,
    val style: LabelStyle?,
    val margins: MarginsResponse?
) : BodyRowResponse {

    override val type: BodyRowType = BodyRowType.RICH_LABEL

    override fun mapToDomain(): BodyRowModel = RichLabelModel(
        text = text ?: error("Label text cannot be null"),
        style = style ?: error("Label style cannot be null"),
        margins = margins?.mapToDomain()
    )
}
