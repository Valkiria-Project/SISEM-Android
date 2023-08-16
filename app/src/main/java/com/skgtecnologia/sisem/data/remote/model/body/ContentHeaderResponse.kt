package com.skgtecnologia.sisem.data.remote.model.body

import androidx.compose.ui.Modifier
import com.skgtecnologia.sisem.data.remote.model.props.TextResponse
import com.skgtecnologia.sisem.data.remote.model.props.mapToDomain
import com.skgtecnologia.sisem.domain.model.body.BodyRowType
import com.skgtecnologia.sisem.domain.model.body.ContentHeaderModel
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ContentHeaderResponse(
    @Json(name = "identifier") val identifier: String?,
    @Json(name = "title") val title: TextResponse?,
    @Json(name = "left_icon") val leftIcon: String?,
    @Json(name = "margins") val modifier: Modifier?
) : BodyRowResponse {

    override val type: BodyRowType = BodyRowType.CONTENT_HEADER

    override fun mapToDomain(): ContentHeaderModel = ContentHeaderModel(
        identifier = identifier ?: error("Content Header identifier cannot be null"),
        title = title?.mapToDomain() ?: error("Content Header title cannot be null"),
        leftIcon = leftIcon,
        modifier = modifier ?: Modifier
    )
}
