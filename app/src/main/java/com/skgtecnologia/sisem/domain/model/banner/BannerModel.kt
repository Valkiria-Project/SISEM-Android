package com.skgtecnologia.sisem.domain.model.banner

import com.valkiria.uicomponents.bricks.banner.BannerUiModel
import com.valkiria.uicomponents.bricks.banner.DEFAULT_ICON_COLOR
import com.valkiria.uicomponents.components.footer.FooterUiModel

data class BannerModel(
    val icon: String,
    val iconColor: String = DEFAULT_ICON_COLOR,
    val title: String,
    val description: String,
    val footerModel: FooterUiModel? = null
) : RuntimeException()

fun Throwable.mapToUi(): BannerUiModel = (this as? BannerModel)?.mapToUi() ?: BannerUiModel(
    icon = "ic_alert",
    title = "Error en servidor",
    description = ""
)

private fun BannerModel.mapToUi() = BannerUiModel(
    icon = icon,
    iconColor = iconColor,
    title = title,
    description = description,
    leftButton = footerModel?.leftButton,
    rightButton = footerModel?.rightButton
)
