package com.skgtecnologia.sisem.domain.model.banner

import com.skgtecnologia.sisem.R
import com.skgtecnologia.sisem.commons.resources.StringProvider
import com.valkiria.uicomponents.model.ui.body.mapToUiModel
import com.skgtecnologia.sisem.domain.model.footer.FooterModel
import com.valkiria.uicomponents.model.ui.banner.DEFAULT_ICON_COLOR
import com.valkiria.uicomponents.model.ui.banner.BannerUiModel
import retrofit2.HttpException
import timber.log.Timber
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.inject.Inject

private const val FORBIDDEN_HTTP_STATUS_CODE = 403

data class BannerModel(
    val icon: String,
    val iconColor: String = DEFAULT_ICON_COLOR,
    val title: String,
    val description: String,
    val footerModel: FooterModel? = null
) : RuntimeException()

fun Throwable.mapToUi(): BannerUiModel = (this as? BannerModel)?.mapToUi() ?: BannerUiModel(
    icon = "Default icon", // FIXME: Add Default
    title = "Default title", // FIXME: Add Default
    description = "Default description" // FIXME: Add Default
)

private fun BannerModel.mapToUi() = BannerUiModel(
    icon = icon,
    iconColor = iconColor,
    title = title,
    description = description,
    leftButton = footerModel?.leftButton?.mapToUiModel(),
    rightButton = footerModel?.rightButton?.mapToUiModel()
)

class ErrorModelFactory @Inject constructor(
    private val stringProvider: StringProvider
) {
    fun getErrorModel(error: Throwable): BannerModel {
        Timber.e(error.localizedMessage)

        return when (error) {
            is ConnectException, is UnknownHostException -> BannerModel(
                icon = "",
                title = stringProvider.getString(R.string.error_connectivity_title),
                description = ""
            )

            is HttpException -> {
                if (error.code() == FORBIDDEN_HTTP_STATUS_CODE) {
                    BannerModel(
                        icon = "",
                        title = stringProvider.getString(R.string.error_forbidden_title),
                        description = ""
                    )
                } else {
                    BannerModel(
                        icon = "",
                        title = error.message(),
                        description = ""
                    )
                }
            }

            is SocketTimeoutException -> BannerModel(
                icon = "",
                title = stringProvider.getString(R.string.error_server_title),
                description = ""
            )

            else -> BannerModel(
                icon = "",
                title = stringProvider.getString(R.string.error_general_title),
                description = ""
            )
        }
    }
}
