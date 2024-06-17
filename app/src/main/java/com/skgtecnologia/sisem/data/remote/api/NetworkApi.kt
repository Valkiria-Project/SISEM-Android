package com.skgtecnologia.sisem.data.remote.api

import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.skgtecnologia.sisem.commons.extensions.recoverResult
import com.skgtecnologia.sisem.commons.extensions.resultOf
import com.skgtecnologia.sisem.commons.resources.StorageProvider
import com.skgtecnologia.sisem.data.remote.extensions.HTTP_FORBIDDEN_STATUS_CODE
import com.skgtecnologia.sisem.data.remote.extensions.HTTP_UNAUTHORIZED_STATUS_CODE
import com.skgtecnologia.sisem.data.remote.model.error.ErrorResponse
import com.skgtecnologia.sisem.data.remote.model.error.mapToDomain
import com.skgtecnologia.sisem.domain.login.model.LoginIdentifier
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
import retrofit2.HttpException
import retrofit2.Response
import timber.log.Timber
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.inject.Inject

private const val FILE_NAME = "android_networking"

class NetworkApi @Inject constructor(
    private val storageProvider: StorageProvider
) {
    private val coroutineExceptionHandler = CoroutineExceptionHandler { _, exception ->
        Timber.wtf(exception)
    }

    private val coroutineContext = Dispatchers.IO + coroutineExceptionHandler

    private val errorResponseAdapter = Moshi.Builder()
        .build()
        .adapter(ErrorResponse::class.java)

    suspend fun <T> apiCall(api: suspend () -> Response<T>) = resultOf {
        val response: Response<T> =
            withContext(coroutineContext) {
                api()
            }

        val body = response.body()
        val content = TimeUtils.getLocalTimeAsString() +
                "\t Overview" + response.toString() +
                "\t Body:" + body +
                "\t Error Body" + response.errorBody() +
                "\n"
        storageProvider.storeContent(
            FILE_NAME,
            Context.MODE_APPEND,
            content.toByteArray()
        )

        if (response.isSuccessful && body != null) {
            body
        } else {
            Timber.wtf("The retrieved response is not successful and/or body is empty")

            throw response.errorBody()!!.toError(response.code()).mapToDomain()
        }
    }.recoverResult {
        throw when (it) {
            is HttpException -> handleHttpException(it.code()).mapToDomain()
            is ConnectException, is UnknownHostException -> ErrorResponse(
                icon = "ic_alert",
                title = "Conectividad",
                description = "Revise su conexión a Internet"
            ).mapToDomain()

            is SocketTimeoutException -> ErrorResponse(
                icon = "ic_alert",
                title = "Servicio no disponible",
                description = "Sucedió algo inesperado, por favor intenta de nuevo"
            ).mapToDomain()

            else -> it
        }
    }

    @Suppress("SwallowedException", "TooGenericExceptionCaught")
    private fun ResponseBody.toError(code: Int): ErrorResponse = try {
        errorResponseAdapter.fromJson(string())!!
    } catch (exception: Exception) {
        Timber.d("status: $code")
        handleHttpException(code)
    }

    private fun handleHttpException(code: Int): ErrorResponse {
        return if (code == HTTP_FORBIDDEN_STATUS_CODE || code == HTTP_UNAUTHORIZED_STATUS_CODE) {
            ErrorResponse(
                icon = "ic_alert",
                title = "Autenticación",
                description = "Se requiere re-autenticación"
            ).apply {
                footerModel = FooterUiModel(
                    leftButton = ButtonUiModel(
                        identifier = LoginIdentifier.LOGIN_RE_AUTH_BANNER.name,
                        label = "Re Autenticar",
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
        } else {
            ErrorResponse(
                icon = "ic_alert",
                title = "Ha ocurrido un error",
                description = ""
            )
        }
    }
}