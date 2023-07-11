package com.skgtecnologia.sisem.data.myscreen.remote.model

import com.skgtecnologia.sisem.domain.myscreen.model.BodyRowModel
import com.skgtecnologia.sisem.domain.myscreen.model.BodyRowType
import com.skgtecnologia.sisem.domain.myscreen.model.ButtonModelNew
import com.skgtecnologia.sisem.domain.myscreen.model.ButtonStyle
import com.skgtecnologia.sisem.domain.myscreen.model.OnClickType
import com.skgtecnologia.sisem.domain.myscreen.model.ButtonSize
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
