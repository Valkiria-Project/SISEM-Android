package com.skgtecnologia.sisem.data.remote.model.error

import com.skgtecnologia.sisem.domain.model.banner.BannerModel
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import com.valkiria.uicomponents.bricks.banner.DEFAULT_ICON_COLOR
import com.valkiria.uicomponents.components.footer.FooterUiModel

@JsonClass(generateAdapter = true)
data class ErrorResponse(
    @Json(name = "icon") val icon: String,
    @Json(name = "title") val title: String,
    @Json(name = "description") val description: String,
    // Sent by the backend on a 403 when the user already has a session open somewhere
    // else. It turns the banner into a question: the two buttons are attached in
    // NetworkApi, and answering yes closes the other session (SMA-753).
    @Json(name = "action_close_session") val actionCloseSession: Boolean = false
) {
    @Transient
    var footerModel: FooterUiModel? = null
}

fun ErrorResponse.mapToDomain() = BannerModel(
    icon = icon,
    iconColor = DEFAULT_ICON_COLOR,
    title = title,
    description = description,
    footerModel = footerModel
)
