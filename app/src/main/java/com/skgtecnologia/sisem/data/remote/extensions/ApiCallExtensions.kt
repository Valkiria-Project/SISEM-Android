package com.skgtecnologia.sisem.data.remote.extensions

import com.skgtecnologia.sisem.commons.extensions.recoverResult
import com.skgtecnologia.sisem.commons.extensions.resultOf
import com.skgtecnologia.sisem.data.remote.model.bricks.banner.BannerResponse
import com.skgtecnologia.sisem.data.remote.model.error.ErrorModelFactory
import com.skgtecnologia.sisem.domain.model.banner.BannerModel
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

suspend fun <T> apiCall(api: suspend () -> Response<T>) =
    resultOf {
        val response: Response<T> = withContext(coroutineContext) {
            api()
        }

        val body = response.body()

        if (response.isSuccessful && body != null) {
            body
        } else {
            Timber.wtf("The retrieved response is not successful and/or body is empty")

            var failure = ErrorModelFactory.genericError()
            response.errorBody()?.also {
                failure = ErrorModelFactory.getErrorModel(response)
            }
            failure
        }
    }.recoverResult {
        if (it is BannerModel) {
            throw it
        } else {
//            throw ErrorModelFactory.getErrorModel(response)
            throw it
        }
    }

private val errorAdapter = Moshi.Builder()
    .build()
    .adapter(BannerResponse::class.java)

@Suppress("SwallowedException", "TooGenericExceptionCaught")
private fun ResponseBody?.toError(): BannerResponse? = if (this != null) {
    try {
        errorAdapter.fromJson(string())
    } catch (exception: Exception) {
        null
    }
} else {
    null
}
