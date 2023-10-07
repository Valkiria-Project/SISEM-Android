package com.skgtecnologia.sisem.data.remote.model.body

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.Modifier
import com.skgtecnologia.sisem.data.remote.model.props.TextResponse
import com.skgtecnologia.sisem.data.remote.model.props.mapToUI
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import com.valkiria.uicomponents.components.BodyRowType
import com.valkiria.uicomponents.components.headerbody.HeaderUiModel

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

    override fun mapToUi(): HeaderUiModel = HeaderUiModel(
        identifier = identifier ?: error("Header identifier cannot be null"),
        title = title?.mapToUI() ?: error("Header title cannot be null"),
        subtitle = subtitle?.mapToUI(),
        leftIcon = leftIcon,
        rightIcon = rightIcon,
        badgeCount = badgeCount,
        arrangement = Arrangement.Center,
        modifier = modifier ?: Modifier
    )
}
