package com.skgtecnologia.sisem.domain.model.bricks

import com.skgtecnologia.sisem.domain.model.header.HeaderModel
import com.valkiria.uicomponents.model.ui.finding.FindingsDetailUiModel

data class FindingsDetailModel(
    val header: HeaderModel,
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
