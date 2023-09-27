package com.skgtecnologia.sisem.domain.model.bricks

import androidx.compose.ui.Modifier
import com.skgtecnologia.sisem.domain.model.body.ImageButtonModel
import com.skgtecnologia.sisem.domain.model.body.mapToUiModel
import com.skgtecnologia.sisem.domain.model.props.TextModel
import com.valkiria.uicomponents.model.ui.button.ImageButtonOptionUiModel

data class ImageButtonOptionModel(
    val identifier: String,
    val title: TextModel,
    val options: List<ImageButtonModel>,
    val modifier: Modifier
)

fun ImageButtonOptionModel.mapToUiModel(): ImageButtonOptionUiModel = ImageButtonOptionUiModel(
    identifier = identifier,
    title = title.text,
    textStyle = title.textStyle,
    options = options.map { it.mapToUiModel() },
    modifier = modifier
)
