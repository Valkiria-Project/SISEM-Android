package com.skgtecnologia.sisem.domain.model.body

import androidx.compose.ui.Modifier
import com.skgtecnologia.sisem.domain.model.header.TextModel
import com.valkiria.uicomponents.components.crewmembercard.DetailUiModel

data class DetailModel(
    val images: List<String>,
    val title: TextModel,
    val subtitle: TextModel,
    val description: TextModel,
    val modifier: Modifier
)

fun DetailModel.mapToUiModel() = DetailUiModel(
    images = images,
    title = title.text,
    titleTextStyle = title.textStyle,
    subtitle = subtitle.text,
    subtitleTextStyle = subtitle.textStyle,
    description = description.text,
    descriptionTextStyle = description.textStyle,
    modifier = modifier
)

fun DetailUiModel.mapToDomain() = DetailModel(
    images = images,
    title = TextModel(text = title, textStyle = titleTextStyle),
    subtitle = TextModel(text = subtitle, textStyle = subtitleTextStyle),
    description = TextModel(text = description, textStyle = descriptionTextStyle),
    modifier = modifier
)
