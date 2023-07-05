package com.skgtecnologia.sisem.data.myscreen.remote.model

import com.skgtecnologia.sisem.domain.myscreen.model.BodyRowModel
import com.skgtecnologia.sisem.domain.myscreen.model.BodyRowType
import com.skgtecnologia.sisem.domain.myscreen.model.NestedBodyModel

data class SectionResponse(
    val sectionTitle: String,
    val bodyRow: List<BodyRowResponse>
) : BodyRowResponse {

    override val type: BodyRowType = BodyRowType.SECTION

    override fun mapToDomain(): BodyRowModel = NestedBodyModel(
        sectionTitle = sectionTitle,
        bodyRowModel = bodyRow.map { it.mapToDomain() }
    )
}
