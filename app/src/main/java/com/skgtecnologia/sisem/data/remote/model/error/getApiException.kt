package com.skgtecnologia.sisem.data.remote.model.error

import retrofit2.Response

//fun <T> getApiException(response: Response<T>): ErrorResponse? {
//    var apiException: ErrorResponse? = null
//    try {
//        val errorString = response.errorBody()!!.string()
//        apiException = JsonUtil.fromJson(errorString, ErrorResponse::class.java)
//    } catch (ex: Exception) {
//        //Do nothing
//    } finally {
//        if (apiException == null) {
//            apiException = ErrorResponse()
//        }
//        apiException.setStatus(response.code())
//    }
//    return apiException
//}