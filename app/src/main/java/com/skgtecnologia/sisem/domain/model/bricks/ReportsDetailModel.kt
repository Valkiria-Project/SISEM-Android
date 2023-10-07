package com.skgtecnologia.sisem.domain.model.bricks

import com.skgtecnologia.sisem.commons.extensions.biLet
import com.skgtecnologia.sisem.domain.model.body.HeaderModel
import com.valkiria.uicomponents.model.props.TextModel
import com.valkiria.uicomponents.model.ui.report.ReportsDetailUiModel

data class ReportsDetailModel(
    val header: HeaderModel,
    val details: List<ReportDetailModel>
)

fun ReportsDetailModel.mapToUiModel() = ReportsDetailUiModel(
    identifier = header.identifier,
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
        identifier = identifier,
        leftIcon = icon,
        title = TextModel(text = title, textStyle = titleTextStyle),
        subtitle = biLet(subtitle, subtitleTextStyle) { subtitle, subtitleTextStyle ->
            TextModel(
                text = subtitle,
                textStyle = subtitleTextStyle
            )
        },
        modifier = modifier
    ),
    details = details.map { it.mapToDomain() }
)
