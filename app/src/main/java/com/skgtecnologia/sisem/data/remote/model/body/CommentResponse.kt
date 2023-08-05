package com.skgtecnologia.sisem.data.remote.model.body

import com.skgtecnologia.sisem.domain.model.body.BodyRowModel
import com.skgtecnologia.sisem.domain.model.body.BodyRowType
import com.skgtecnologia.sisem.domain.model.body.CommentsModel
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CommentResponse(
    @Json(name = "identifier") val identifier: String?,
) : BodyRowResponse {

    override val type: BodyRowType = BodyRowType.CONTENT_HEADER

    override fun mapToDomain(): BodyRowModel = CommentsModel(
        identifier = identifier ?: error("Comments identifier cannot be null"),
    )
}
