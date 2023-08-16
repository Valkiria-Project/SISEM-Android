package com.skgtecnologia.sisem.di

import com.skgtecnologia.sisem.BuildConfig
import com.skgtecnologia.sisem.data.remote.adapters.ArrangementAdapter
import com.skgtecnologia.sisem.data.remote.adapters.KeyboardOptionsAdapter
import com.skgtecnologia.sisem.data.remote.adapters.ModifierAdapter
import com.skgtecnologia.sisem.data.remote.model.body.BodyRowResponse
import com.skgtecnologia.sisem.data.remote.model.body.ButtonResponse
import com.skgtecnologia.sisem.data.remote.model.body.ChipOptionsResponse
import com.skgtecnologia.sisem.data.remote.model.body.ChipResponse
import com.skgtecnologia.sisem.data.remote.model.body.ContentHeaderResponse
import com.skgtecnologia.sisem.data.remote.model.body.CrewMemberCardResponse
import com.skgtecnologia.sisem.data.remote.model.body.DetailedInfoListResponse
import com.skgtecnologia.sisem.data.remote.model.body.FiltersResponse
import com.skgtecnologia.sisem.data.remote.model.body.FindingResponse
import com.skgtecnologia.sisem.data.remote.model.body.FingerprintResponse
import com.skgtecnologia.sisem.data.remote.model.body.InventoryCheckResponse
import com.skgtecnologia.sisem.data.remote.model.body.LabelResponse
import com.skgtecnologia.sisem.data.remote.model.body.PasswordTextFieldResponse
import com.skgtecnologia.sisem.data.remote.model.body.RichLabelResponse
import com.skgtecnologia.sisem.data.remote.model.body.SegmentedSwitchResponse
import com.skgtecnologia.sisem.data.remote.model.body.TermsAndConditionsResponse
import com.skgtecnologia.sisem.data.remote.model.body.TextFieldResponse
import com.skgtecnologia.sisem.di.qualifiers.Audit
import com.skgtecnologia.sisem.domain.model.body.BodyRowType
import com.squareup.moshi.Moshi
import com.squareup.moshi.adapters.EnumJsonAdapter
import com.squareup.moshi.adapters.PolymorphicJsonAdapterFactory
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.valkiria.uicomponents.components.button.OnClick
import com.valkiria.uicomponents.props.ButtonSize
import com.valkiria.uicomponents.props.ButtonStyle
import com.valkiria.uicomponents.props.ChipStyle
import com.valkiria.uicomponents.props.TextStyle
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import okhttp3.Interceptor
import okhttp3.logging.HttpLoggingInterceptor

const val HTTP_CLIENT_VERSION_HEADER = "User-Agent"
const val CLIENT_VERSION = "sisem/Android/" + BuildConfig.VERSION_NAME
const val HTTP_LOCATION_HEADER = "geolocation"
const val CLIENT_TIMEOUT_DEFAULTS = 15_000L

@Module
@InstallIn(SingletonComponent::class)
object CoreNetworkModule {

    @Singleton
    @Provides
    fun providesMoshi(): Moshi {
        return Moshi.Builder().apply {
            provideEnumAdapters()
            provideBodyPolymorphicAdapter()
            provideAdapters()
        }.build()
    }

    private fun Moshi.Builder.provideEnumAdapters() = this.add(
        ButtonSize::class.java,
        EnumJsonAdapter.create(ButtonSize::class.java).withUnknownFallback(ButtonSize.DEFAULT)
    ).add(
        ButtonStyle::class.java,
        EnumJsonAdapter.create(ButtonStyle::class.java).withUnknownFallback(ButtonStyle.LOUD)
    ).add(
        ChipStyle::class.java,
        EnumJsonAdapter.create(ChipStyle::class.java).withUnknownFallback(ChipStyle.PRIMARY)
    ).add(
        OnClick::class.java,
        EnumJsonAdapter.create(OnClick::class.java)
    ).add(
        TextStyle::class.java,
        EnumJsonAdapter.create(TextStyle::class.java).withUnknownFallback(TextStyle.BODY_1)
    )

    private fun Moshi.Builder.provideBodyPolymorphicAdapter() = this.add(
        BodyRowType::class.java,
        EnumJsonAdapter.create(BodyRowType::class.java).withUnknownFallback(null)
    ).add(
        PolymorphicJsonAdapterFactory.of(
            BodyRowResponse::class.java,
            "type"
        ).withSubtype(
            ButtonResponse::class.java,
            BodyRowType.BUTTON.name
        ).withSubtype(
            ChipResponse::class.java,
            BodyRowType.CHIP.name
        ).withSubtype(
            ChipOptionsResponse::class.java,
            BodyRowType.CHIP_OPTIONS.name
        ).withSubtype(
            ContentHeaderResponse::class.java,
            BodyRowType.CONTENT_HEADER.name
        ).withSubtype(
            CrewMemberCardResponse::class.java,
            BodyRowType.CREW_MEMBER_CARD.name
        ).withSubtype(
            DetailedInfoListResponse::class.java,
            BodyRowType.DETAILED_INFO_LIST.name
        ).withSubtype(
            FiltersResponse::class.java,
            BodyRowType.FILTERS.name
        ).withSubtype(
            FindingResponse::class.java,
            BodyRowType.FINDING.name
        ).withSubtype(
            FingerprintResponse::class.java,
            BodyRowType.FINGERPRINT.name
        ).withSubtype(
            InventoryCheckResponse::class.java,
            BodyRowType.INVENTORY_CHECK.name
        ).withSubtype(
            LabelResponse::class.java,
            BodyRowType.LABEL.name
        ).withSubtype(
            SegmentedSwitchResponse::class.java,
            BodyRowType.SEGMENTED_SWITCH.name
        ).withSubtype(
            PasswordTextFieldResponse::class.java,
            BodyRowType.PASSWORD_TEXT_FIELD.name
        ).withSubtype(
            RichLabelResponse::class.java,
            BodyRowType.RICH_LABEL.name
        ).withSubtype(
            TermsAndConditionsResponse::class.java,
            BodyRowType.TERMS_AND_CONDITIONS.name
        ).withSubtype(
            TextFieldResponse::class.java,
            BodyRowType.TEXT_FIELD.name
        )
    )

    private fun Moshi.Builder.provideAdapters() = this.add(
        KeyboardOptionsAdapter()
    ).add(
        ArrangementAdapter()
    ).add(
        ModifierAdapter()
    ).add(
        KotlinJsonAdapterFactory()
    )

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

    @Audit
    @Singleton
    @Provides
    fun providesAuditInterceptor(): Interceptor =
        Interceptor { chain ->
            val request = chain.request()
                .newBuilder()
                .addHeader(HTTP_CLIENT_VERSION_HEADER, CLIENT_VERSION)
                .addHeader(HTTP_LOCATION_HEADER, "6.155216, -75.327840") // FIXME: Hardcoded data
                .build()
            chain.proceed(request)
        }
}
