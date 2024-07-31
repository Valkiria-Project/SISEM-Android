package com.skgtecnologia.sisem.data.auth.remote

import com.skgtecnologia.sisem.BuildConfig
import com.skgtecnologia.sisem.data.auth.remote.model.AccessTokenResponse
import com.skgtecnologia.sisem.data.auth.remote.model.AuthenticateBody
import com.skgtecnologia.sisem.data.auth.remote.model.LogoutResponse
import com.skgtecnologia.sisem.data.auth.remote.model.RefreshTokenResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Url

interface AuthApi {

    @POST("auth")
    suspend fun authenticate(
        @Body authenticateBody: AuthenticateBody
    ): Response<AccessTokenResponse>

    @FormUrlEncoded
    @POST
    suspend fun refresh(
        @Url url: String = BuildConfig.REFRESH_URL,
        @Field("grant_type") grantType: String = "refresh_token",
        @Field("client_id") clientId: String = "sisem_real_app_mobile",
        @Field("client_secret") clientSecret: String = "",
        @Field("refresh_token") refreshToken: String,
        @Field("scope") scope: String = "offline_access"
    ): Response<RefreshTokenResponse>

    @GET("auth/logout")
    suspend fun logout(
        @Header("username") username: String
    ): Response<LogoutResponse>
}
