package com.skgtecnologia.sisem.data.remote.model.body

import com.skgtecnologia.sisem.domain.model.body.BodyRowModel
import com.skgtecnologia.sisem.domain.model.body.BodyRowType
import com.skgtecnologia.sisem.domain.model.body.DetailedInfoListModel
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class DetailedInfoListResponse(
    @Json(name = "identifier") val identifier: String?,
) : BodyRowResponse {

    override val type: BodyRowType = BodyRowType.DETAILED_INFO_LIST

    override fun mapToDomain(): BodyRowModel = DetailedInfoListModel(
        identifier = identifier ?: error("Detailed info list identifier cannot be null"),
    )
}
