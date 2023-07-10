package com.skgtecnologia.sisem.data.myscreen.remote.model

import com.skgtecnologia.sisem.domain.myscreen.model.BodyRowModel
import com.skgtecnologia.sisem.domain.myscreen.model.BodyRowType
import com.skgtecnologia.sisem.domain.myscreen.model.ButtonModelNew
import com.skgtecnologia.sisem.domain.myscreen.model.ButtonStyle
import com.skgtecnologia.sisem.domain.myscreen.model.OnClickType

data class ButtonResponseNew(
    val identifier: String?,
    val label: String?,
    val style: ButtonStyle,
    val onClick: OnClickType,
    val margins: MarginsResponse?
) : BodyRowResponse {

    override val type: BodyRowType = BodyRowType.BUTTON

    override fun mapToDomain(): BodyRowModel = ButtonModelNew(
        identifier = identifier ?: error("Button identifier cannot be null"),
        label = label ?: error("Button label cannot be null"),
        style = style,
        onClick = onClick,
        margins = margins?.mapToDomain()
    )
}
