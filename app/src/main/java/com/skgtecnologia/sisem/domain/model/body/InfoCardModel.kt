package com.skgtecnologia.sisem.domain.model.body

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.Modifier
import com.skgtecnologia.sisem.di.operation.OperationRole
import com.skgtecnologia.sisem.domain.model.bricks.ChipSectionModel
import com.skgtecnologia.sisem.domain.model.bricks.PillModel
import com.skgtecnologia.sisem.domain.model.bricks.ReportsDetailModel
import com.skgtecnologia.sisem.domain.model.bricks.mapToUiModel
import com.valkiria.uicomponents.model.props.TextModel
import com.valkiria.uicomponents.model.ui.body.BodyRowType
import com.valkiria.uicomponents.model.ui.card.InfoCardUiModel

data class InfoCardModel(
    val identifier: String,
    val icon: String,
    val title: TextModel,
    val pill: PillModel,
    val date: TextModel?,
    val chipSection: ChipSectionModel?,
    val reportsDetail: ReportsDetailModel?,
    val arrangement: Arrangement.Horizontal,
    val modifier: Modifier
) : BodyRowModel {

    override val type: BodyRowType = BodyRowType.INFO_CARD
}

fun InfoCardModel.mapToUiModel() = InfoCardUiModel(
    identifier = identifier,
    icon = icon,
    titleText = OperationRole.getRoleByName(title.text)?.humanizedName ?: title.text,
    titleTextStyle = title.textStyle,
    pillText = pill.title.text,
    pillTextStyle = pill.title.textStyle,
    pillColor = pill.color,
    dateText = date?.text,
    dateTextStyle = date?.textStyle,
    chipSection = chipSection?.mapToUiModel(),
    reportsDetail = reportsDetail?.mapToUiModel(),
    arrangement = arrangement,
    modifier = modifier
)
