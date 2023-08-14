package com.skgtecnologia.sisem.domain.model.bricks

import com.skgtecnologia.sisem.domain.model.header.TextModel
import com.valkiria.uicomponents.components.crewmembercard.ChipSectionUiModel

data class ChipSectionModel(
    val title: TextModel,
    val listText: ListTextModel
)

fun ChipSectionModel.mapToUiModel() = ChipSectionUiModel(
    title = title.text,
    titleTextStyle = title.textStyle,
    listText = listText.texts,
    listTextStyle = listText.textStyle
)
