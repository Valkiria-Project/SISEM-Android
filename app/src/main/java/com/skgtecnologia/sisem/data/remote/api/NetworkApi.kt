package com.skgtecnologia.sisem.data.remote.api

import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.skgtecnologia.sisem.R
import com.skgtecnologia.sisem.commons.extensions.recoverResult
import com.skgtecnologia.sisem.commons.extensions.resultOf
import com.skgtecnologia.sisem.commons.resources.ANDROID_NETWORKING_FILE_NAME
import com.skgtecnologia.sisem.commons.resources.StorageProvider
import com.skgtecnologia.sisem.commons.resources.StringProvider
import com.skgtecnologia.sisem.data.remote.extensions.HTTP_FORBIDDEN_STATUS_CODE
import com.skgtecnologia.sisem.data.remote.extensions.HTTP_UNAUTHORIZED_STATUS_CODE
import com.skgtecnologia.sisem.data.remote.model.error.ErrorResponse
import com.skgtecnologia.sisem.data.remote.model.error.mapToDomain
import com.skgtecnologia.sisem.domain.login.model.LoginIdentifier
import com.skgtecnologia.sisem.domain.model.banner.BannerModel
import com.squareup.moshi.Moshi
import com.valkiria.uicomponents.components.button.ButtonSize
import com.valkiria.uicomponents.components.button.ButtonStyle
import com.valkiria.uicomponents.components.button.ButtonUiModel
import com.valkiria.uicomponents.components.button.OnClick
import com.valkiria.uicomponents.components.footer.FooterUiModel
import com.valkiria.uicomponents.components.label.TextStyle
import com.valkiria.uicomponents.utlis.TimeUtils
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.ResponseBody
import retrofit2.Response
import timber.log.Timber
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import java.time.Instant
import javax.inject.Inject

class NetworkApi @Inject constructor(
    private val storageProvider: StorageProvider,
    private val stringProvider: StringProvider
) {
    private val coroutineExceptionHandler = CoroutineExceptionHandler { _, exception ->
        Timber.wtf(exception)
    }

    private val coroutineContext = Dispatchers.IO + coroutineExceptionHandler

    private val errorResponseAdapter = Moshi.Builder()
        .build()
        .adapter(ErrorResponse::class.java)
        .lenient()

    suspend fun <T> apiCall(api: suspend () -> Response<T>) = resultOf {
        val response: Response<T> =
            withContext(coroutineContext) {
                api.invoke()
            }

        val body = response.body()

        if (response.isSuccessful && body != null) {
            storeSuccessResponse(response)
            body
        } else {
            Timber.wtf("The retrieved response is not successful and/or body is empty")

            throw handleHttpException(response.code(), response.errorBody()).mapToDomain()
        }
    }.recoverResult { throwable ->
        throw if (throwable is BannerModel) {
            throwable
        } else {
            parseError(throwable).mapToDomain()
        }
    }

    private fun parseError(throwable: Throwable): ErrorResponse {
        return when (throwable) {
            is ConnectException, is UnknownHostException -> ErrorResponse(
                icon = stringProvider.getString(R.string.alert_icon),
                title = stringProvider.getString(R.string.error_connectivity_title),
                description = stringProvider.getString(R.string.error_connectivity_description)
            )

            is SocketTimeoutException -> ErrorResponse(
                icon = stringProvider.getString(R.string.alert_icon),
                title = stringProvider.getString(R.string.error_server_title),
                description = stringProvider.getString(R.string.error_server_description)
            )

            else -> ErrorResponse(
                icon = stringProvider.getString(R.string.alert_icon),
                title = stringProvider.getString(R.string.error_general_title),
                description = stringProvider.getString(R.string.error_general_description)
            )
        }
    }

    private fun handleHttpException(code: Int, responseBody: ResponseBody?): ErrorResponse {
        val errorResponse = responseBody?.toError(code)
        storeErrorResponse(code, errorResponse, responseBody)

        return when {
            errorResponse != null -> {
                errorResponse
            }

            code == HTTP_FORBIDDEN_STATUS_CODE || code == HTTP_UNAUTHORIZED_STATUS_CODE ->
                ErrorResponse(
                    icon = stringProvider.getString(R.string.alert_icon),
                    title = stringProvider.getString(R.string.error_unauthorized_title),
                    description = stringProvider.getString(R.string.error_unauthorized_description)
                ).apply {
                    footerModel = FooterUiModel(
                        leftButton = ButtonUiModel(
                            identifier = LoginIdentifier.LOGIN_RE_AUTH_BANNER.name,
                            label = stringProvider.getString(R.string.error_unauthorized_cta),
                            style = ButtonStyle.LOUD,
                            textStyle = TextStyle.HEADLINE_5,
                            onClick = OnClick.DISMISS,
                            size = ButtonSize.DEFAULT,
                            arrangement = Arrangement.Start,
                            modifier = Modifier.padding(
                                start = 0.dp,
                                top = 20.dp,
                                end = 0.dp,
                                bottom = 0.dp
                            )
                        )
                    )
                }

            else -> ErrorResponse(
                icon = stringProvider.getString(R.string.alert_icon),
                title = stringProvider.getString(R.string.error_general_title),
                description = stringProvider.getString(R.string.error_general_description)
            )
        }
    }

    @Suppress("SwallowedException", "TooGenericExceptionCaught")
    private fun ResponseBody.toError(code: Int): ErrorResponse? = try {
        errorResponseAdapter.fromJson(source())!!
    } catch (exception: Exception) {
        Timber.d("status: $code")
        null
    }

    private fun <T> storeSuccessResponse(response: Response<T>) {
        val content = TimeUtils.getLocalDateTime(Instant.now()).toString() +
                "\t Body: " + response.body() +
                "\n"

        storageProvider.storeContent(
            ANDROID_NETWORKING_FILE_NAME,
            Context.MODE_APPEND,
            content.toByteArray()
        )
    }

    private fun storeErrorResponse(
        code: Int,
        errorResponse: ErrorResponse?,
        responseBody: ResponseBody?
    ) {
        val content = TimeUtils.getLocalDateTime(Instant.now()).toString() +
                "\t Error Code: " + code +
                "\t Error Body: " + (errorResponse ?: responseBody).toString() +
                "\n"

        storageProvider.storeContent(
            ANDROID_NETWORKING_FILE_NAME,
            Context.MODE_APPEND,
            content.toByteArray()
        )
    }
}
