package com.skgtecnologia.sisem.data.remote.model.body

import androidx.compose.ui.Modifier
import com.skgtecnologia.sisem.domain.model.body.BodyRowModel
import com.skgtecnologia.sisem.domain.model.body.BodyRowType
import com.skgtecnologia.sisem.domain.model.body.CommentsModel
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import com.valkiria.uicomponents.props.TextStyle

@JsonClass(generateAdapter = true)
data class CommentResponse(
    @Json(name = "identifier") val identifier: String?,
    @Json(name = "thread") val thread: List<String>?,
    @Json(name = "text_style") val textStyle: TextStyle?,
    @Json(name = "reply_icon") val replyIcon: String?,
    @Json(name = "margins") val modifier: Modifier?
) : BodyRowResponse {

    override val type: BodyRowType = BodyRowType.CONTENT_HEADER

    override fun mapToDomain(): BodyRowModel = CommentsModel(
        identifier = identifier ?: error("Comments identifier cannot be null"),
        thread = thread ?: error("Comments thread cannot be null"),
        textStyle = textStyle ?: error("Comments textStyle cannot be null"),
        replyIcon = replyIcon ?: error("Comments replyIcon cannot be null"),
        modifier = modifier ?: Modifier
    )
}
