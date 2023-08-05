package com.skgtecnologia.sisem.domain.model.body

data class DetailedInfoListModel(
    val identifier: String
) : BodyRowModel {

    override val type: BodyRowType = BodyRowType.DETAILED_INFO_LIST
}
