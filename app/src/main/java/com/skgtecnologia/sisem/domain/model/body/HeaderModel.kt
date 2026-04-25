package com.skgtecnologia.sisem.domain.model.body

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.Modifier

data class HeaderModel(
    val identifier: String,
    val title: TextModel,
    val subtitle: TextModel? = null,
    val leftIcon: String? = null,
    val rightIcon: String? = null,
    val badgeCount: String? = null,
    val arrangement: Arrangement.Horizontal = Arrangement.Center,
    val modifier: Modifier
) : BodyRowModel {

    override val type: BodyRowType = BodyRowType.HEADER
}
