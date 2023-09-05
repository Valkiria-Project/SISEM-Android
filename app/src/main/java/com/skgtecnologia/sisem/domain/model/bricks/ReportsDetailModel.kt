package com.skgtecnologia.sisem.domain.model.bricks

import com.skgtecnologia.sisem.commons.extensions.biLet
import com.skgtecnologia.sisem.domain.model.header.HeaderModel
import com.skgtecnologia.sisem.domain.model.header.TextModel
import com.valkiria.uicomponents.model.ui.report.ReportsDetailUiModel

data class ReportsDetailModel(
    val header: HeaderModel,
    val details: List<ReportDetailModel>
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
