package com.skgtecnologia.sisem.data.remote.model.body

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.Modifier
import com.skgtecnologia.sisem.data.remote.model.props.TextResponse
import com.skgtecnologia.sisem.data.remote.model.props.mapToDomain
import com.skgtecnologia.sisem.domain.model.body.HeaderModel
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import com.valkiria.uicomponents.model.ui.body.BodyRowType

@JsonClass(generateAdapter = true)
data class HeaderResponse(
    @Json(name = "identifier") val identifier: String?,
    @Json(name = "title") val title: TextResponse?,
    @Json(name = "subtitle") val subtitle: TextResponse?,
    @Json(name = "left_icon") val leftIcon: String?,
    @Json(name = "right_icon") val rightIcon: String?,
    @Json(name = "badge_count") val badgeCount: String?,
    @Json(name = "margins") val modifier: Modifier?
) : BodyRowResponse {

    override val type: BodyRowType = BodyRowType.HEADER

    override fun mapToDomain(): HeaderModel = HeaderModel(
        identifier = identifier ?: error("Header identifier cannot be null"),
        title = title?.mapToDomain() ?: error("Header title cannot be null"),
        subtitle = subtitle?.mapToDomain(),
        leftIcon = leftIcon,
        rightIcon = rightIcon,
        badgeCount = badgeCount,
        arrangement = Arrangement.Center,
        modifier = modifier ?: Modifier
    )
}
