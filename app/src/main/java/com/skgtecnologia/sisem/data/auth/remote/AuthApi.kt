package com.skgtecnologia.sisem.data.auth.remote

import com.skgtecnologia.sisem.data.auth.remote.model.AccessTokenResponse
import com.skgtecnologia.sisem.data.auth.remote.model.AuthenticateBody
import com.skgtecnologia.sisem.data.auth.remote.model.RefreshBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApi {

    @POST("auth")
    suspend fun authenticate(
        @Body authenticateBody: AuthenticateBody
    ): Response<AccessTokenResponse>

    @POST("auth/refresh-token")
    suspend fun refresh(
        @Body refreshBody: RefreshBody
    ): Response<AccessTokenResponse>
}
