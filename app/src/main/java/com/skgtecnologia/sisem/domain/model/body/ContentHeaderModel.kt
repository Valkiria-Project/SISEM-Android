package com.skgtecnologia.sisem.domain.model.body

import androidx.compose.ui.Modifier
import com.skgtecnologia.sisem.domain.model.header.HeaderModel
import com.skgtecnologia.sisem.domain.model.header.TextModel
import com.valkiria.uicomponents.props.TextStyle

data class ContentHeaderModel(
    val identifier: String,
    val text: String,
    val leftIcon: String?,
    val textStyle: TextStyle,
    val modifier: Modifier = Modifier
) : BodyRowModel {

    override val type: BodyRowType = BodyRowType.CONTENT_HEADER
}

fun ContentHeaderModel.mapToHeaderModel() = HeaderModel(
    title = TextModel(
        text = text,
        textStyle = textStyle
    ),
    subtitle = null,
    leftIcon = leftIcon,
    rightIcon = null,
    modifier = modifier
)
