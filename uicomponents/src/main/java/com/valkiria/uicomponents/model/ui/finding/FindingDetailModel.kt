package com.valkiria.uicomponents.model.ui.finding

import androidx.compose.ui.Modifier
import com.valkiria.uicomponents.model.props.TextModel

data class FindingDetailModel(
    val images: List<String>,
    val description: TextModel,
    val reporter: TextModel,
    val modifier: Modifier
)

fun FindingDetailModel.mapToUiModel() = FindingDetailUiModel(
    images = images,
    descriptionText = description.text,
    descriptionTextStyle = description.textStyle,
    reporterText = reporter.text,
    reporterTextStyle = reporter.textStyle,
    modifier = modifier
)

fun FindingDetailUiModel.mapToDomain() = FindingDetailModel(
    images = images,
    description = TextModel(text = descriptionText, textStyle = descriptionTextStyle),
    reporter = TextModel(text = reporterText, textStyle = reporterTextStyle),
    modifier = modifier
)
