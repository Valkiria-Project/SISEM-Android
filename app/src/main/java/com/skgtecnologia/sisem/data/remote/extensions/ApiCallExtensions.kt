package com.skgtecnologia.sisem.data.remote.extensions

import com.skgtecnologia.sisem.commons.extensions.resultOf
import com.skgtecnologia.sisem.data.remote.model.error.ErrorResponse
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

//    throw exception.handleThrowable().mapToDomain()
}

private val coroutineContext = Dispatchers.IO + coroutineExceptionHandler

suspend fun <T> apiCall(apiToBeCalled: suspend () -> Response<T>) = resultOf {
    val response: Response<T> = withContext(coroutineContext) {
        apiToBeCalled()
    }

    val body = response.body()

    if (response.isSuccessful && body != null) {
        body
    } else {
        error("")
    }

//    withContext(Dispatchers.IO) {
//        try {
//            val response: Response<T> = apiToBeCalled()
//
//            if (response.isSuccessful) {
//                // In case of success response we
//                // are returning Resource.Success object
//                // by passing our data in it.
//                Resource.Success(data = response.body()!!)
//            } else {
//                // parsing api's own custom json error
//                // response in ExampleErrorResponse pojo
//                val errorResponse: ExampleErrorResponse? = convertErrorBody(response.errorBody())
//                // Simply returning api's own failure message
//                Resource.Error(
//                    errorMessage = errorResponse?.failureMessage ?: "Something went wrong"
//                )
//            }
//
//        } catch (e: HttpException) {
//            // Returning HttpException's message
//            // wrapped in Resource.Error
//            Resource.Error(errorMessage = e.message ?: "Something went wrong")
//        } catch (e: IOException) {
//            // Returning no internet message
//            // wrapped in Resource.Error
//            Resource.Error("Please check your network connection")
//        } catch (e: Exception) {
//            // Returning 'Something went wrong' in case
//            // of unknown error wrapped in Resource.Error
//            Resource.Error(errorMessage = "Something went wrong")
//        }
//    }
}


// FIXME: Finish this
fun Throwable.handleThrowable(): ErrorModel {
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

fun ResponseBody?.toError(): ErrorResponse? = if (this != null) {
    try {
        errorAdapter.fromJson(string())
    } catch (e: Exception) {
        null
    }
} else {
    null
}
