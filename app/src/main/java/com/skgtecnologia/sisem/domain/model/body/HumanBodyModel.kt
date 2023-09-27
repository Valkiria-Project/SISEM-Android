package com.skgtecnologia.sisem.domain.model.body

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.Modifier

data class HumanBodyModel(
    val identifier: String,
    val arrangement: Arrangement.Horizontal,
    val modifier: Modifier,
) : BodyRowModel {

    override val type: BodyRowType = BodyRowType.HUMAN_BODY
}
