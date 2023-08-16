package com.skgtecnologia.sisem.domain.model.body

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.Modifier
import com.skgtecnologia.sisem.domain.model.header.HeaderModel
import com.skgtecnologia.sisem.domain.model.header.TextModel

data class ContentHeaderModel(
    val identifier: String,
    val title: TextModel,
    val leftIcon: String?,
    val arrangement: Arrangement.Horizontal,
    val modifier: Modifier = Modifier
) : BodyRowModel {

    override val type: BodyRowType = BodyRowType.CONTENT_HEADER
}

fun ContentHeaderModel.mapToHeaderModel() = HeaderModel(
    title = title,
    leftIcon = leftIcon,
    arrangement = arrangement,
    modifier = modifier
)
