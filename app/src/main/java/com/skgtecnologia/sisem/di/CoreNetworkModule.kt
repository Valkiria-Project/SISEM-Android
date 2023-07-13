package com.skgtecnologia.sisem.di

import com.skgtecnologia.sisem.BuildConfig
import com.skgtecnologia.sisem.data.model.bodyrow.BodyRowResponse
import com.skgtecnologia.sisem.data.model.bodyrow.ButtonResponseNew
import com.skgtecnologia.sisem.data.model.bodyrow.ChipResponse
import com.skgtecnologia.sisem.data.model.bodyrow.RichLabelResponse
import com.skgtecnologia.sisem.data.model.bodyrow.TermsAndConditionsResponse
import com.skgtecnologia.sisem.data.model.bodyrow.TextFieldResponse
import com.skgtecnologia.sisem.domain.model.bodyrow.BodyRowType
import com.skgtecnologia.sisem.domain.model.props.ButtonStyle
import com.skgtecnologia.sisem.domain.model.props.KeyboardType
import com.skgtecnologia.sisem.domain.model.props.LabelStyle
import com.skgtecnologia.sisem.domain.model.props.OnClickType
import com.squareup.moshi.Moshi
import com.squareup.moshi.adapters.EnumJsonAdapter
import com.squareup.moshi.adapters.PolymorphicJsonAdapterFactory
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.valkiria.uicomponents.components.button.ButtonSize
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import java.util.concurrent.TimeUnit
import javax.inject.Singleton
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

private const val OK_HTTP_CLIENT_TIMEOUT_DEFAULTS = 15_000L
private const val BASE_URL = "http://34.69.190.119:8080/"

@Module
@InstallIn(SingletonComponent::class)
object CoreNetworkModule {

    @Singleton
    @Provides
    fun providesMoshi(): Moshi = Moshi.Builder()
        .add(
            LabelStyle::class.java,
            EnumJsonAdapter.create(LabelStyle::class.java).withUnknownFallback(
                LabelStyle.PRIMARY
            )
        )
        .add(
            KeyboardType::class.java,
            EnumJsonAdapter.create(KeyboardType::class.java).withUnknownFallback(
                KeyboardType.TEXT
            )
        )
        .add(
            ButtonStyle::class.java,
            EnumJsonAdapter.create(ButtonStyle::class.java).withUnknownFallback(
                ButtonStyle.LOUD
            )
        )
        .add(
            OnClickType::class.java,
            EnumJsonAdapter.create(OnClickType::class.java).withUnknownFallback(
                OnClickType.FORGOT_PASSWORD
            )
        )
        .add(
            BodyRowType::class.java,
            EnumJsonAdapter.create(BodyRowType::class.java).withUnknownFallback(null)
        )
        .add(
            ButtonSize::class.java,
            EnumJsonAdapter.create(ButtonSize::class.java).withUnknownFallback(
                ButtonSize.DEFAULT
            )
        )
        .add(
            PolymorphicJsonAdapterFactory.of(
                BodyRowResponse::class.java,
                "type"
            ).withSubtype(
                RichLabelResponse::class.java,
                BodyRowType.RICH_LABEL.name
            ).withSubtype(
                TextFieldResponse::class.java,
                BodyRowType.TEXT_FIELD.name
            ).withSubtype(
                ButtonResponseNew::class.java,
                BodyRowType.BUTTON.name
            ).withSubtype(
                ChipResponse::class.java,
                BodyRowType.CHIP.name
            ).withSubtype(
                TermsAndConditionsResponse::class.java,
                BodyRowType.PASSWORD_TEXT_FIELD.name
            ).withSubtype(
                TermsAndConditionsResponse::class.java,
                BodyRowType.TERMS_AND_CONDITIONS.name
            )
        )
        .add(KotlinJsonAdapterFactory())
        .build()

    @Singleton
    @Provides
    internal fun providesLoggingInterceptor(): HttpLoggingInterceptor? = when {
        BuildConfig.DEBUG -> {
            HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            }
        }

        else -> null
    }

    @Singleton
    @Provides
    internal fun providesOkHttpClientBuilder(
        loggingInterceptor: HttpLoggingInterceptor?
    ): OkHttpClient.Builder = OkHttpClient.Builder().apply {
        connectTimeout(OK_HTTP_CLIENT_TIMEOUT_DEFAULTS, TimeUnit.MILLISECONDS)
        readTimeout(OK_HTTP_CLIENT_TIMEOUT_DEFAULTS, TimeUnit.MILLISECONDS)
        writeTimeout(OK_HTTP_CLIENT_TIMEOUT_DEFAULTS, TimeUnit.MILLISECONDS)
        loggingInterceptor?.also { addInterceptor(it) }
    }

    @Singleton
    @Provides
    internal fun providesOkHttpClient(
        builder: OkHttpClient.Builder
    ): OkHttpClient = with(builder) {
        build()
    }

    @Provides
    @Singleton
    internal fun providesRetrofit(
        moshi: Moshi,
        okHttpClient: OkHttpClient
    ): Retrofit =
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
}
