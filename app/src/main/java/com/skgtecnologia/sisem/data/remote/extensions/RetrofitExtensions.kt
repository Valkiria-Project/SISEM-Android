package com.skgtecnologia.sisem.data.remote.extensions

import okhttp3.Request

// Bearer Authentication
const val HTTP_AUTHORIZATION_HEADER = "Authorization"
const val BEARER_TOKEN_PREFIX = "Bearer "

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
