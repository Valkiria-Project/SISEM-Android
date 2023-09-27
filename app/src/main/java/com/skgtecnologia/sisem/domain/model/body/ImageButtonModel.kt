package com.skgtecnologia.sisem.domain.model.body

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.Modifier
import com.skgtecnologia.sisem.domain.model.props.TextModel
import com.valkiria.uicomponents.model.ui.button.ImageButtonUiModel2

data class ImageButtonModel(
    val identifier: String,
    val title: TextModel?,
    val image: String,
    val selected: Boolean,
    val arrangement: Arrangement.Horizontal,
    val modifier: Modifier
) : BodyRowModel {

    override val type: BodyRowType = BodyRowType.IMAGE_BUTTON
}

fun ImageButtonModel.mapToUiModel(): ImageButtonUiModel2 = ImageButtonUiModel2(
    identifier = identifier,
    title = title?.text,
    textStyle = title?.textStyle,
    image = image,
    selected = selected,
    arrangement = arrangement,
    modifier = modifier
)
