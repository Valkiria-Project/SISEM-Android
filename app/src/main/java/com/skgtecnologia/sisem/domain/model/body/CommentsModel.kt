package com.skgtecnologia.sisem.domain.model.body

data class CommentsModel(
    val identifier: String
) : BodyRowModel {

    override val type: BodyRowType = BodyRowType.COMMENTS
}
