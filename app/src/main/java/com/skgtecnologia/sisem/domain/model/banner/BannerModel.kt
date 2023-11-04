package com.skgtecnologia.sisem.domain.model.banner

import com.skgtecnologia.sisem.R
import com.skgtecnologia.sisem.commons.resources.StringProvider
import com.skgtecnologia.sisem.data.remote.extensions.HTTP_FORBIDDEN_STATUS_CODE
import com.skgtecnologia.sisem.data.remote.extensions.HTTP_UNAUTHORIZED_STATUS_CODE
import com.valkiria.uicomponents.bricks.banner.BannerUiModel
import com.valkiria.uicomponents.bricks.banner.DEFAULT_ICON_COLOR
import com.valkiria.uicomponents.components.footer.FooterUiModel
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.inject.Inject
import retrofit2.HttpException
import timber.log.Timber

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

class ErrorModelFactory @Inject constructor(
    private val stringProvider: StringProvider
) {
    fun getErrorModel(error: Throwable): BannerModel {
        Timber.e(error.localizedMessage)

        return when (error) {
            is ConnectException, is UnknownHostException -> BannerModel(
                icon = stringProvider.getString(R.string.alert_icon),
                title = stringProvider.getString(R.string.error_connectivity_title),
                description = ""
            )

            is HttpException -> {
                if (error.code() == HTTP_FORBIDDEN_STATUS_CODE ||
                    error.code() == HTTP_UNAUTHORIZED_STATUS_CODE
                ) {
                    BannerModel(
                        icon = stringProvider.getString(R.string.alert_icon),
                        title = stringProvider.getString(R.string.error_unauthorized_title),
                        description = stringProvider.getString(
                            R.string.error_unauthorized_description
                        )
                    )
                } else {
                    BannerModel(
                        icon = stringProvider.getString(R.string.alert_icon),
                        title = error.message(),
                        description = ""
                    )
                }
            }

            is SocketTimeoutException -> BannerModel(
                icon = stringProvider.getString(R.string.alert_icon),
                title = stringProvider.getString(R.string.error_server_title),
                description = stringProvider.getString(R.string.error_server_description)
            )

            else -> BannerModel(
                icon = stringProvider.getString(R.string.alert_icon),
                title = stringProvider.getString(R.string.error_general_title),
                description = ""
            )
        }
    }
}
