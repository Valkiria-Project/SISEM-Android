package com.skgtecnologia.sisem.data.myscreen.remote.model

import com.skgtecnologia.sisem.domain.myscreen.model.BodyRowModel
import com.skgtecnologia.sisem.domain.myscreen.model.BodyRowType
import com.skgtecnologia.sisem.domain.myscreen.model.CrossSellingModel

data class CrossSellingResponse(
    val text: String
) : BodyRowResponse {

    override val type: BodyRowType = BodyRowType.CROSS_SELLING

    override fun mapToDomain(): BodyRowModel = CrossSellingModel(
        text = text
    )
}
