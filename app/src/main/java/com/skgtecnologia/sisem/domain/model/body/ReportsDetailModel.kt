package com.skgtecnologia.sisem.domain.model.body

import com.skgtecnologia.sisem.domain.model.header.HeaderModel
import com.skgtecnologia.sisem.domain.model.header.TextModel
import com.valkiria.uicomponents.components.crewmembercard.ReportsDetailUiModel

data class ReportsDetailModel(
    val header: HeaderModel,
    val details: List<DetailModel>
)

fun ReportsDetailModel.mapToUiModel() = ReportsDetailUiModel(
    icon = header.leftIcon,
    title = header.title.text,
    titleTextStyle = header.title.textStyle,
    subtitle = header.subtitle?.text,
    subtitleTextStyle = header.subtitle?.textStyle,
    modifier = header.modifier,
    details = details.map { it.mapToUiModel() }
)

fun ReportsDetailUiModel.mapToDomain() = ReportsDetailModel(
    header = HeaderModel(
        leftIcon = icon,
        title = TextModel(text = title, textStyle = titleTextStyle),
        subtitle = subtitle?.let { TextModel(text = it, textStyle = subtitleTextStyle!!) },
        modifier = modifier
    ),
    details = details.map { it.mapToDomain() }
)
