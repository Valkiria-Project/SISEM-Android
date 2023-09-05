package com.skgtecnologia.sisem.data.remote.extensions

import com.skgtecnologia.sisem.commons.extensions.recoverResult
import com.skgtecnologia.sisem.commons.extensions.resultOf
import com.skgtecnologia.sisem.data.remote.model.error.ErrorResponse
import com.skgtecnologia.sisem.data.remote.model.error.mapToDomain
import com.skgtecnologia.sisem.domain.model.error.ErrorModel
import com.skgtecnologia.sisem.domain.model.error.ErrorModelFactory
import com.squareup.moshi.Moshi
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.ResponseBody
import retrofit2.Response
import timber.log.Timber

private val coroutineExceptionHandler = CoroutineExceptionHandler { _, exception ->
    Timber.wtf(exception)
}

private val coroutineContext = Dispatchers.IO + coroutineExceptionHandler

suspend fun <T> apiCall(errorModelFactory: ErrorModelFactory, api: suspend () -> Response<T>) =
    resultOf {
        val response: Response<T> = withContext(coroutineContext) {
            api()
        }

        val body = response.body()

        if (response.isSuccessful && body != null) {
            body
        } else {
            Timber.wtf("The retrieved response is not successful and/or body is empty")

            response.errorBody().toError()?.mapToDomain()?.let {
                throw it
            } ?: error("Response is not successful and/or body is empty")
        }
    }.recoverResult {
        if (it is ErrorModel) {
            throw it
        } else {
            throw errorModelFactory.getErrorModel(it)
        }
    }

private val errorAdapter = Moshi.Builder()
    .build()
    .adapter(ErrorResponse::class.java)

@Suppress("SwallowedException", "TooGenericExceptionCaught")
private fun ResponseBody?.toError(): ErrorResponse? = if (this != null) {
    try {
        errorAdapter.fromJson(string())
    } catch (exception: Exception) {
        null
    }
} else {
    null
}
