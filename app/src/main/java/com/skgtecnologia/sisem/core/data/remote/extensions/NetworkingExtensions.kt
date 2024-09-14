package com.skgtecnologia.sisem.core.data.remote.extensions

import okhttp3.Request
import okhttp3.Response

// Bearer Authentication
private const val HTTP_AUTHORIZATION_HEADER = "Authorization"
const val HTTP_FORBIDDEN_STATUS_CODE = 403
const val HTTP_UNAUTHORIZED_STATUS_CODE = 401
private const val BEARER_TOKEN_PREFIX = "Bearer "

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

fun Response.isUnauthorized() = this.code == HTTP_UNAUTHORIZED_STATUS_CODE
