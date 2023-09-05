package com.skgtecnologia.sisem.domain.model.body

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.Modifier
import com.skgtecnologia.sisem.domain.model.header.HeaderModel
import com.skgtecnologia.sisem.domain.model.header.TextModel
import com.valkiria.uicomponents.model.props.TextStyle

data class ContentHeaderModel(
    val identifier: String,
    val text: String,
    val textStyle: TextStyle,
    val leftIcon: String?,
    val arrangement: Arrangement.Horizontal,
    val modifier: Modifier = Modifier
) : BodyRowModel {

    override val type: BodyRowType = BodyRowType.CONTENT_HEADER
}

fun ContentHeaderModel.mapToHeaderModel() = HeaderModel(
    title = TextModel(text, textStyle),
    leftIcon = leftIcon,
    arrangement = arrangement,
    modifier = modifier
)
