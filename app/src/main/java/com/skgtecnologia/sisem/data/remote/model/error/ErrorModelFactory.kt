package com.skgtecnologia.sisem.data.remote.model.error

import com.skgtecnologia.sisem.data.remote.extensions.HTTP_FORBIDDEN_STATUS_CODE
import com.skgtecnologia.sisem.data.remote.extensions.HTTP_UNAUTHORIZED_STATUS_CODE
import com.skgtecnologia.sisem.domain.model.banner.BannerModel
import retrofit2.HttpException
import retrofit2.Response
import timber.log.Timber
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

private const val GENERIC_ERROR_MESSAGE = "Response is not successful and/or body is empty"

object ErrorModelFactory {

    fun genericError(message: String? = GENERIC_ERROR_MESSAGE) = ErrorResponse(errorMessage = message.orEmpty())

    fun <T> getErrorModel(response: Response<T>): ErrorResponse {
        Timber.e(response.code().toString())

        return genericError(response.code().toString())
//        return when (error) {
//            is HttpException -> {
//                if (error.code() == HTTP_FORBIDDEN_STATUS_CODE ||
//                    error.code() == HTTP_UNAUTHORIZED_STATUS_CODE
//                ) {
//                    BannerModel(
//                        icon = stringProvider.getString(R.string.alert_icon),
//                        title = stringProvider.getString(R.string.error_unauthorized_title),
//                        description = stringProvider.getString(
//                            R.string.error_unauthorized_description
//                        )
//                    )
//                } else {
//                    BannerModel(
//                        icon = stringProvider.getString(R.string.alert_icon),
//                        title = error.message(),
//                        description = ""
//                    )
//                }
//            }
//
//            is ConnectException, is UnknownHostException -> BannerModel(
//                icon = stringProvider.getString(R.string.alert_icon),
//                title = stringProvider.getString(R.string.error_connectivity_title),
//                description = ""
//            )
//
//            is SocketTimeoutException -> BannerModel(
//                icon = stringProvider.getString(R.string.alert_icon),
//                title = stringProvider.getString(R.string.error_server_title),
//                description = stringProvider.getString(R.string.error_server_description)
//            )
//
//            else -> BannerModel(
//                icon = stringProvider.getString(R.string.alert_icon),
//                title = stringProvider.getString(R.string.error_general_title),
//                description = ""
//            )
//        }
    }
}
