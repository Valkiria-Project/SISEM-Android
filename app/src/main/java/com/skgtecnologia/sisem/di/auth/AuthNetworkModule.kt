package com.skgtecnologia.sisem.di.auth

import com.skgtecnologia.sisem.data.auth.remote.AuthApi
import com.skgtecnologia.sisem.di.CLIENT_TIMEOUT_DEFAULTS
import com.skgtecnologia.sisem.di.CoreNetworkModule
import com.skgtecnologia.sisem.di.qualifiers.Audit
import com.skgtecnologia.sisem.di.qualifiers.BasicAuthentication
import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import java.util.concurrent.TimeUnit
import javax.inject.Singleton
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

private const val BASE_URL = "http://34.69.190.119:8080/sisem-api/"

@Module(includes = [CoreNetworkModule::class])
@InstallIn(SingletonComponent::class)
object AuthNetworkModule {

    @BasicAuthentication
    @Singleton
    @Provides
    internal fun providesOkHttpClient(
        loggingInterceptor: HttpLoggingInterceptor?,
        @Audit auditInterceptor: Interceptor,
    ): OkHttpClient = OkHttpClient.Builder().apply {
        connectTimeout(CLIENT_TIMEOUT_DEFAULTS, TimeUnit.MILLISECONDS)
        readTimeout(CLIENT_TIMEOUT_DEFAULTS, TimeUnit.MILLISECONDS)
        writeTimeout(CLIENT_TIMEOUT_DEFAULTS, TimeUnit.MILLISECONDS)
        loggingInterceptor?.also { addInterceptor(it) }
        addInterceptor(auditInterceptor)
    }.build()

    @BasicAuthentication
    @Provides
    @Singleton
    internal fun providesRetrofit(
        moshi: Moshi,
        @BasicAuthentication okHttpClient: OkHttpClient
    ): Retrofit =
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()

    @Provides
    internal fun providesAuthApi(@BasicAuthentication retrofit: Retrofit): AuthApi =
        retrofit.create(AuthApi::class.java)
}
