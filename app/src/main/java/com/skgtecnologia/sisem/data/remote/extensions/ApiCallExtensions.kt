package com.skgtecnologia.sisem.data.remote.extensions

import com.skgtecnologia.sisem.commons.extensions.recoverResult
import com.skgtecnologia.sisem.commons.extensions.resultOf
import com.skgtecnologia.sisem.data.remote.model.error.ErrorResponse
import com.skgtecnologia.sisem.data.remote.model.error.mapToDomain
import com.skgtecnologia.sisem.domain.model.error.ErrorModel
import com.squareup.moshi.Moshi
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.ResponseBody
import retrofit2.HttpException
import retrofit2.Response
import timber.log.Timber

private val coroutineExceptionHandler = CoroutineExceptionHandler { _, exception ->
    Timber.wtf(exception)
}

private val coroutineContext = Dispatchers.IO + coroutineExceptionHandler

suspend fun <T> apiCall(api: suspend () -> Response<T>) = resultOf {
    val response: Response<T> = withContext(coroutineContext) {
        api()
    }

    val body = response.body()

    if (response.isSuccessful && body != null) {
        body
    } else {
        Timber.wtf("The retrieved response is not successful and/or body is empty")

        // FIXME: Generic error message "Algo salió mal"
        response.errorBody().toError()?.mapToDomain()?.let {
            throw it
        } ?: error("The retrieved response is not successful and/or body is empty")
    }
}.recoverResult {
    if (it is ErrorModel) {
        throw it
    } else {
        throw it.handleThrowable()
    }
}

// FIXME: Finish this
private fun Throwable.handleThrowable(): ErrorModel {
    Timber.e(this)
    return if (this is UnknownHostException)
        ErrorModel(icon = "", title = "Connection error", description = "")
    else if ((this is HttpException) && (this.code() == 403))
        ErrorModel(icon = "", title = "Not authorized", description = "")
    else if (this is SocketTimeoutException)
        ErrorModel(
            icon = "",
            title = "Conectividad",
            description = ""
        )
    else if (this is ConnectException)
        ErrorModel(
            icon = "",
            title = "Conectividad",
            description = ""
        )
    else if (this.message != null)
        ErrorModel(icon = "", title = this.message!!, description = "")
    else
        ErrorModel(icon = "", title = "Unknown error", description = "")
}

private val errorAdapter = Moshi.Builder()
    .build()
    .adapter(ErrorResponse::class.java)

private fun ResponseBody?.toError(): ErrorResponse? = if (this != null) {
    try {
        errorAdapter.fromJson(string())
    } catch (e: Exception) {
        null
    }
} else {
    null
}
