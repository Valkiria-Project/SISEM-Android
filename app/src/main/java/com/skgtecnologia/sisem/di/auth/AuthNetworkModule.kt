package com.skgtecnologia.sisem.di.auth

import com.skgtecnologia.sisem.BuildConfig
import com.skgtecnologia.sisem.core.data.remote.interceptors.AuditInterceptor
import com.skgtecnologia.sisem.core.data.remote.interceptors.NetworkInterceptor
import com.skgtecnologia.sisem.data.auth.remote.AuthApi
import com.skgtecnologia.sisem.di.CLIENT_TIMEOUT_DEFAULTS
import com.skgtecnologia.sisem.di.CoreNetworkModule
import com.skgtecnologia.sisem.di.qualifiers.BasicAuthentication
import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module(includes = [CoreNetworkModule::class])
@InstallIn(SingletonComponent::class)
object AuthNetworkModule {

    @BasicAuthentication
    @Singleton
    @Provides
    internal fun providesOkHttpClient(
        loggingInterceptor: HttpLoggingInterceptor?,
        auditInterceptor: AuditInterceptor,
        networkInterceptor: NetworkInterceptor
    ): OkHttpClient = OkHttpClient.Builder().apply {
        connectTimeout(CLIENT_TIMEOUT_DEFAULTS, TimeUnit.MILLISECONDS)
        readTimeout(CLIENT_TIMEOUT_DEFAULTS, TimeUnit.MILLISECONDS)
        writeTimeout(CLIENT_TIMEOUT_DEFAULTS, TimeUnit.MILLISECONDS)
        addInterceptor(auditInterceptor)
        addInterceptor(networkInterceptor)
        loggingInterceptor?.also { addInterceptor(it) }
    }.build()

    @BasicAuthentication
    @Provides
    @Singleton
    internal fun providesRetrofit(
        moshi: Moshi,
        @BasicAuthentication okHttpClient: OkHttpClient
    ): Retrofit =
        Retrofit.Builder()
            .baseUrl(BuildConfig.AUTH_BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()

    @Provides
    internal fun providesAuthApi(@BasicAuthentication retrofit: Retrofit): AuthApi =
        retrofit.create(AuthApi::class.java)
}
