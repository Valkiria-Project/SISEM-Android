package com.skgtecnologia.sisem.domain.model.bodyrow

import com.skgtecnologia.sisem.domain.model.bodyrow.BodyRowType.BUTTON
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize

@Parcelize
data class ButtonModel(
    val label: String
) : BodyRowModel {

    @IgnoredOnParcel
    override val type: BodyRowType = BUTTON
}
