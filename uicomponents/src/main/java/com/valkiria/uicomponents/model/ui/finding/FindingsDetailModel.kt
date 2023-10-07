package com.valkiria.uicomponents.model.ui.finding

import com.valkiria.uicomponents.components.body.HeaderUiModel

data class FindingsDetailModel(
    val header: HeaderUiModel,
    val details: List<FindingDetailModel>
)

fun FindingsDetailModel.mapToUiModel() = FindingsDetailUiModel(
    icon = header.leftIcon,
    title = header.title.text,
    titleTextStyle = header.title.textStyle,
    subtitle = header.subtitle?.text,
    subtitleTextStyle = header.subtitle?.textStyle,
    modifier = header.modifier,
    details = details.map { it.mapToUiModel() }
)
