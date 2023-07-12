package com.skgtecnologia.sisem.data.myscreen.remote.model

import com.skgtecnologia.sisem.domain.core.model.bodyrow.BodyRowModel
import com.skgtecnologia.sisem.domain.core.model.bodyrow.BodyRowType
import com.skgtecnologia.sisem.domain.core.model.bodyrow.ButtonModelNew
import com.skgtecnologia.sisem.domain.core.model.props.ButtonStyle
import com.skgtecnologia.sisem.domain.core.model.props.OnClickType
import com.skgtecnologia.sisem.domain.core.model.props.ButtonSize
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ButtonResponseNew(
    val identifier: String?,
    val label: String?,
    val style: ButtonStyle,
    @Json(name = "on_click") val onClick: OnClickType,
    val size: ButtonSize,
    val margins: MarginsResponse?
) : BodyRowResponse {

    override val type: BodyRowType = BodyRowType.BUTTON

    override fun mapToDomain(): BodyRowModel = ButtonModelNew(
        identifier = identifier ?: error("Button identifier cannot be null"),
        label = label ?: error("Button label cannot be null"),
        style = style,
        onClick = onClick,
        size = size,
        margins = margins?.mapToDomain()
    )
}
