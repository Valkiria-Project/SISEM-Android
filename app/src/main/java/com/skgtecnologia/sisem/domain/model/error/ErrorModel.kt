package com.skgtecnologia.sisem.domain.model.error

import com.skgtecnologia.sisem.R
import com.skgtecnologia.sisem.commons.resources.StringProvider
import com.valkiria.uicomponents.model.ui.errorbanner.ErrorUiModel
import retrofit2.HttpException
import timber.log.Timber
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.inject.Inject

private const val FORBIDDEN_HTTP_STATUS_CODE = 403

data class ErrorModel(
    val icon: String,
    val title: String,
    val description: String
) : RuntimeException()

fun Throwable.mapToUi(): ErrorUiModel = (this as? ErrorModel)?.mapToUi() ?: ErrorUiModel(
    icon = "Default icon", // FIXME: Add Default
    title = "Default title", // FIXME: Add Default
    description = "Default description" // FIXME: Add Default
)

private fun ErrorModel.mapToUi() = ErrorUiModel(
    icon = icon,
    title = title,
    description = description
)

class ErrorModelFactory @Inject constructor(
    private val stringProvider: StringProvider
) {
    fun getErrorModel(error: Throwable): ErrorModel {
        Timber.e(error.localizedMessage)

        return when (error) {
            is ConnectException, is UnknownHostException -> ErrorModel(
                icon = "",
                title = stringProvider.getString(R.string.error_connectivity_title),
                description = ""
            )

            is HttpException -> {
                if (error.code() == FORBIDDEN_HTTP_STATUS_CODE) {
                    ErrorModel(
                        icon = "",
                        title = stringProvider.getString(R.string.error_forbidden_title),
                        description = ""
                    )
                } else {
                    ErrorModel(
                        icon = "",
                        title = error.message(),
                        description = ""
                    )
                }
            }

            is SocketTimeoutException -> ErrorModel(
                icon = "",
                title = stringProvider.getString(R.string.error_server_title),
                description = ""
            )

            else -> ErrorModel(
                icon = "",
                title = stringProvider.getString(R.string.error_general_title),
                description = ""
            )
        }
    }
}
