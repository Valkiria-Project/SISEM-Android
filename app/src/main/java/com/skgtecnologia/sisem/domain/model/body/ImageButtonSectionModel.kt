package com.skgtecnologia.sisem.domain.model.body

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.Modifier
import com.skgtecnologia.sisem.domain.model.bricks.ImageButtonOptionModel
import com.skgtecnologia.sisem.domain.model.bricks.mapToUiModel
import com.valkiria.uicomponents.model.ui.button.ImageButtonSectionUiModel

data class ImageButtonSectionModel(
    val identifier: String,
    val options: List<ImageButtonOptionModel>,
    val arrangement: Arrangement.Horizontal,
    val modifier: Modifier
) : BodyRowModel {

    override val type: BodyRowType = BodyRowType.IMAGE_BUTTON_SECTION
}

fun ImageButtonSectionModel.mapToUiModel(): ImageButtonSectionUiModel = ImageButtonSectionUiModel(
    identifier = identifier,
    options = options.map { it.mapToUiModel() },
    arrangement = arrangement,
    modifier = modifier
)
