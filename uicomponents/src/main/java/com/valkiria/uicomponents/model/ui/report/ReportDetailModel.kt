package com.valkiria.uicomponents.model.ui.report

import androidx.compose.ui.Modifier
import com.valkiria.uicomponents.model.props.TextModel

data class ReportDetailModel(
    val images: List<String>,
    val title: TextModel,
    val subtitle: TextModel,
    val description: TextModel,
    val modifier: Modifier
)

fun ReportDetailModel.mapToUiModel() = ReportDetailUiModel(
    images = images,
    title = title.text,
    titleTextStyle = title.textStyle,
    subtitle = subtitle.text,
    subtitleTextStyle = subtitle.textStyle,
    description = description.text,
    descriptionTextStyle = description.textStyle,
    modifier = modifier
)

fun ReportDetailUiModel.mapToDomain() = ReportDetailModel(
    images = images,
    title = TextModel(text = title, textStyle = titleTextStyle),
    subtitle = TextModel(text = subtitle, textStyle = subtitleTextStyle),
    description = TextModel(text = description, textStyle = descriptionTextStyle),
    modifier = modifier
)
