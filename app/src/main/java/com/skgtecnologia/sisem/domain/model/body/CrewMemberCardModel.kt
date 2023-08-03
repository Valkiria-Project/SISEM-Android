package com.skgtecnologia.sisem.domain.model.body

import androidx.compose.ui.Modifier
import com.skgtecnologia.sisem.domain.model.header.TextModel
import com.valkiria.uicomponents.components.crewmembercard.CrewMemberCardUiModel

data class CrewMemberCardModel(
    val identifier: String,
    val icon: String,
    val title: TextModel,
    val pill: PillModel,
    val date: TextModel,
    //val chipSection: ChipSectionModel?,
    val reportsDetail: ReportsDetailModel?,
    val modifier: Modifier
) : BodyRowModel {

    override val type: BodyRowType = BodyRowType.CREW_MEMBER_CARD
}

fun CrewMemberCardModel.mapToUiModel() = CrewMemberCardUiModel(
    identifier = identifier,
    icon = icon,
    titleText = title.text,
    titleTextStyle = title.textStyle,
    pillText = pill.title.text,
    pillTextStyle = pill.title.textStyle,
    pillColor = pill.color,
    dateText = date.text,
    dateTextStyle = date.textStyle,
    //chipSection = chipSection?.mapToUiModel(),
    reportsDetail = reportsDetail?.mapToUiModel(),
    modifier = modifier
)
