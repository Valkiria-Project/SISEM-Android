package com.skgtecnologia.sisem.core.data.remote.model.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.Modifier
import com.skgtecnologia.sisem.core.data.remote.model.components.label.TextResponse
import com.skgtecnologia.sisem.core.data.remote.model.components.label.mapToUi
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import com.valkiria.uicomponents.components.BodyRowType
import com.valkiria.uicomponents.components.header.HeaderUiModel

@JsonClass(generateAdapter = true)
data class HeaderResponse(
    @Json(name = "identifier") val identifier: String?,
    @Json(name = "title") val title: TextResponse?,
    @Json(name = "subtitle") val subtitle: TextResponse?,
    @Json(name = "left_icon") val leftIcon: String?,
    @Json(name = "right_icon") val rightIcon: String?,
    @Json(name = "badge_count") val badgeCount: String?,
    @Json(name = "visibility") val visibility: Boolean?,
    @Json(name = "required") val required: Boolean?,
    @Json(name = "margins") val modifier: Modifier?
) : BodyRowResponse {

    override val type: BodyRowType = BodyRowType.HEADER

    override fun mapToUi(): HeaderUiModel = HeaderUiModel(
        identifier = identifier ?: title?.text ?: error("Header identifier cannot be null"),
        title = title?.mapToUi() ?: error("Header title cannot be null"),
        subtitle = subtitle?.mapToUi(),
        leftIcon = leftIcon,
        rightIcon = rightIcon,
        badgeCount = badgeCount,
        visibility = visibility ?: true,
        required = required ?: false,
        arrangement = Arrangement.Center,
        modifier = modifier ?: Modifier
    )
}
