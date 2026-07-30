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

    @Suppress("LongParameterList")
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

    /**
     * Closes whatever session the user has open elsewhere. This goes straight to Keycloak
     * rather than through the SISEM API, on the same token endpoint the refresh call uses.
     * It authenticates as a side effect and hands back tokens, but we deliberately drop
     * them: the user stays on the login screen and signs in again through `auth`, which is
     * the only call that returns the role, turn and preoperational data the app needs.
     */
    @Suppress("LongParameterList")
    @FormUrlEncoded
    @POST
    suspend fun closeActiveSession(
        @Url url: String = BuildConfig.REFRESH_URL,
        @Field("grant_type") grantType: String = "password",
        @Field("client_id") clientId: String = "sisem_real_app_mobile",
        @Field("client_secret") clientSecret: String = "",
        @Field("username") username: String,
        @Field("password") password: String,
        @Field("scope") scope: String = "offline_access",
        @Field("force_close_session") forceCloseSession: Boolean = true
    ): Response<RefreshTokenResponse>

    @GET("auth/logout")
    suspend fun logout(
        @Header("username") username: String,
        @Header("refresh_token") refreshToken: String
    ): Response<LogoutResponse>
}
