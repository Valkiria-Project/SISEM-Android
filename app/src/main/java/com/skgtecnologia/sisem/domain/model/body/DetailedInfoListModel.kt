package com.skgtecnologia.sisem.domain.model.body

import androidx.compose.ui.Modifier
import com.valkiria.uicomponents.components.detailedinfolist.DetailedInfoListUiModel
import com.valkiria.uicomponents.components.detailedinfolist.DetailedInfoUiModel
import com.valkiria.uicomponents.props.TextStyle

data class DetailedInfoListModel(
    val identifier: String,
    val details: List<DetailedInfoUiModel>,
    val labelTextStyle: TextStyle,
    val textTextStyle: TextStyle,
    val modifier: Modifier = Modifier
) : BodyRowModel {

    override val type: BodyRowType = BodyRowType.DETAILED_INFO_LIST
}

fun DetailedInfoListModel.mapToUiModel() = DetailedInfoListUiModel(
    details = details,
    labelTextStyle = labelTextStyle,
    textTextStyle = textTextStyle,
    modifier = modifier
)
