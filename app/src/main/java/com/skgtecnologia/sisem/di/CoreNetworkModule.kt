package com.skgtecnologia.sisem.di

import com.skgtecnologia.sisem.BuildConfig
import com.skgtecnologia.sisem.data.myscreen.remote.model.BodyRowResponse
import com.skgtecnologia.sisem.data.myscreen.remote.model.ButtonResponseNew
import com.skgtecnologia.sisem.data.myscreen.remote.model.ChipResponse
import com.skgtecnologia.sisem.data.myscreen.remote.model.CrossSellingResponse
import com.skgtecnologia.sisem.data.myscreen.remote.model.LabelResponse
import com.skgtecnologia.sisem.data.myscreen.remote.model.MessageResponse
import com.skgtecnologia.sisem.data.myscreen.remote.model.PaymentMethodInfoResponse
import com.skgtecnologia.sisem.data.myscreen.remote.model.SectionResponse
import com.skgtecnologia.sisem.data.myscreen.remote.model.TermsAndConditionsResponse
import com.skgtecnologia.sisem.data.myscreen.remote.model.TextFieldResponse
import com.skgtecnologia.sisem.domain.myscreen.model.BodyRowType
import com.skgtecnologia.sisem.domain.myscreen.model.ButtonStyle
import com.skgtecnologia.sisem.domain.myscreen.model.KeyBoardType
import com.skgtecnologia.sisem.domain.myscreen.model.LabelStyle
import com.skgtecnologia.sisem.domain.myscreen.model.OnClickType
import com.skgtecnologia.sisem.domain.myscreen.model.ScreenType
import com.squareup.moshi.Moshi
import com.squareup.moshi.adapters.EnumJsonAdapter
import com.squareup.moshi.adapters.PolymorphicJsonAdapterFactory
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
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

const val OK_HTTP_CLIENT_TIMEOUT_DEFAULTS = 15_000L

@Module
@InstallIn(SingletonComponent::class)
object CoreNetworkModule {

    @Singleton
    @Provides
    fun providesMoshi(): Moshi = Moshi.Builder()
        .add(
            ScreenType::class.java,
            EnumJsonAdapter.create(ScreenType::class.java).withUnknownFallback(
                ScreenType.PROCESSING
            )
        )
        .add(
            LabelStyle::class.java,
            EnumJsonAdapter.create(LabelStyle::class.java).withUnknownFallback(
                LabelStyle.PRIMARY
            )
        )
        .add(
            KeyBoardType::class.java,
            EnumJsonAdapter.create(KeyBoardType::class.java).withUnknownFallback(
                KeyBoardType.TEXT
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
            PolymorphicJsonAdapterFactory.of(
                BodyRowResponse::class.java,
                "type"
            ).withSubtype(
                CrossSellingResponse::class.java,
                BodyRowType.CROSS_SELLING.name
            ).withSubtype(
                MessageResponse::class.java,
                BodyRowType.MESSAGE.name
            ).withSubtype(
                PaymentMethodInfoResponse::class.java,
                BodyRowType.PAYMENT_METHOD_INFO.name
            ).withSubtype(
                SectionResponse::class.java,
                BodyRowType.SECTION.name
            ).withSubtype(
                LabelResponse::class.java,
                BodyRowType.LABEL.name
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
            .baseUrl("http://34.69.190.119:8080/")
            .client(okHttpClient)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
}
