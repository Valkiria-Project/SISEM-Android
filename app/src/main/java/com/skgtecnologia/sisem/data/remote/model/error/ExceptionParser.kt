package com.skgtecnologia.sisem.data.remote.model.error

import retrofit2.HttpException
import retrofit2.Response
import java.net.SocketTimeoutException
import java.net.UnknownHostException

internal object ExceptionParser {

//    fun <D : Any> parse(errorResponse: Response<D>): ApiException {
//        return runCatching { ApiUtil.getApiException(errorResponse) }.getOrElse(::parse)
//    }
//
//    fun parse(throwable: Throwable): ApiException {
//        return runCatching {
//            when (throwable) {
//                is HttpException -> ExceptionFactory.httpError(throwable)
//                is UnknownHostException -> ExceptionFactory.connectionError()
//                is SocketTimeoutException -> ExceptionFactory.socketTimeoutError(throwable.message)
//                else -> ExceptionFactory.genericError(throwable.localizedMessage.orEmpty())
//            }
//        }.getOrDefault(ExceptionFactory.genericError())
//    }
}