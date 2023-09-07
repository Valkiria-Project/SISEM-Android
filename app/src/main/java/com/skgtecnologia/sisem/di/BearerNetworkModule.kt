package com.skgtecnologia.sisem.di

import com.skgtecnologia.sisem.BuildConfig
import com.skgtecnologia.sisem.data.remote.interceptors.AccessTokenInterceptor
import com.skgtecnologia.sisem.di.qualifiers.Audit
import com.skgtecnologia.sisem.di.qualifiers.BearerAuthentication
import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module(includes = [CoreNetworkModule::class])
@InstallIn(SingletonComponent::class)
object BearerNetworkModule {

    @BearerAuthentication
    @Singleton
    @Provides
    internal fun providesOkHttpClient(
        loggingInterceptor: HttpLoggingInterceptor?,
        @Audit auditInterceptor: Interceptor,
        accessTokenInterceptor: AccessTokenInterceptor
    ): OkHttpClient = OkHttpClient.Builder().apply {
        connectTimeout(CLIENT_TIMEOUT_DEFAULTS, TimeUnit.MILLISECONDS)
        readTimeout(CLIENT_TIMEOUT_DEFAULTS, TimeUnit.MILLISECONDS)
        writeTimeout(CLIENT_TIMEOUT_DEFAULTS, TimeUnit.MILLISECONDS)
        loggingInterceptor?.also { addInterceptor(it) }
        addInterceptor(auditInterceptor)
        addInterceptor(accessTokenInterceptor)
    }.build()

    @BearerAuthentication
    @Provides
    @Singleton
    internal fun providesRetrofit(
        moshi: Moshi,
        @BearerAuthentication okHttpClient: OkHttpClient
    ): Retrofit =
        Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
}
