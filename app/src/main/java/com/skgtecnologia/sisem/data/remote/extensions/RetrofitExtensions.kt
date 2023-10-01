package com.skgtecnologia.sisem.data.remote.extensions

import okhttp3.Request
import okhttp3.Response


// Bearer Authentication
private const val HTTP_AUTHORIZATION_HEADER = "Authorization"
private const val BEARER_TOKEN_PREFIX = "Bearer "
private const val HTTP_UNAUTHORIZED_ERROR = "401"

/**
 * Returns the Bearer Authentication Header for the given token of type [String]
 */
fun String.getBearerAuthHeader(): String = BEARER_TOKEN_PREFIX.plus(this)

/**
 * Signs the [Request] with the given token as Header
 *
 * @param [token] to sign the request
 * @return [Request] with Authorization token as Header
 */
fun Request.signWithToken(token: String) = newBuilder()
    .header(HTTP_AUTHORIZATION_HEADER, token.getBearerAuthHeader())
    .build()

fun isRequestWithAccessToken(response: Response): Boolean {
    val header: String? = response.request.header(HTTP_AUTHORIZATION_HEADER)
    return header?.startsWith(BEARER_TOKEN_PREFIX) == true
}

fun Response.isUnauthorized() = this.code.toString() == HTTP_UNAUTHORIZED_ERROR
