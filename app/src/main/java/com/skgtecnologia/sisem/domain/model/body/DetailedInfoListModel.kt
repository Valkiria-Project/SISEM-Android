package com.skgtecnologia.sisem.domain.model.body

import com.valkiria.uicomponents.components.detailedinfolist.DetailedInfoListUiModel
import com.valkiria.uicomponents.components.detailedinfolist.DetailedInfoUiModel

data class DetailedInfoListModel(
    val identifier: String,
    val details: List<DetailedInfoUiModel>
) : BodyRowModel {

    override val type: BodyRowType = BodyRowType.DETAILED_INFO_LIST
}

fun DetailedInfoListModel.mapToUiModel() = DetailedInfoListUiModel(
    details = details
)
