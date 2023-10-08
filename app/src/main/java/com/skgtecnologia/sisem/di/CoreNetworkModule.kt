package com.skgtecnologia.sisem.di

import com.skgtecnologia.sisem.BuildConfig
import com.skgtecnologia.sisem.data.remote.adapters.ArrangementAdapter
import com.skgtecnologia.sisem.data.remote.adapters.KeyboardOptionsAdapter
import com.skgtecnologia.sisem.data.remote.adapters.ModifierAdapter
import com.skgtecnologia.sisem.data.remote.model.components.card.InfoCardResponse
import com.skgtecnologia.sisem.data.remote.model.components.chip.ChipOptionsResponse
import com.skgtecnologia.sisem.data.remote.model.components.chip.ChipResponse
import com.skgtecnologia.sisem.data.remote.model.components.chip.ChipSelectionResponse
import com.skgtecnologia.sisem.data.remote.model.components.chip.FiltersResponse
import com.skgtecnologia.sisem.data.remote.model.components.detailedinfolist.DetailedInfoListResponse
import com.skgtecnologia.sisem.data.remote.model.components.finding.FindingResponse
import com.skgtecnologia.sisem.data.remote.model.components.fingerprint.FingerprintResponse
import com.skgtecnologia.sisem.data.remote.model.components.header.HeaderResponse
import com.skgtecnologia.sisem.data.remote.model.components.inventorycheck.InventoryCheckResponse
import com.skgtecnologia.sisem.data.remote.model.components.label.LabelResponse
import com.skgtecnologia.sisem.data.remote.model.components.richlabel.RichLabelResponse
import com.skgtecnologia.sisem.data.remote.model.components.segmentedswitch.SegmentedSwitchResponse
import com.skgtecnologia.sisem.data.remote.model.components.slider.SliderResponse
import com.skgtecnologia.sisem.data.remote.model.components.termsandconditions.TermsAndConditionsResponse
import com.skgtecnologia.sisem.data.remote.model.components.textfield.PasswordTextFieldResponse
import com.skgtecnologia.sisem.data.remote.model.components.textfield.TextFieldResponse
import com.squareup.moshi.Moshi
import com.squareup.moshi.adapters.EnumJsonAdapter
import com.squareup.moshi.adapters.PolymorphicJsonAdapterFactory
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.valkiria.uicomponents.components.BodyRowType
import com.valkiria.uicomponents.components.button.ButtonSize
import com.valkiria.uicomponents.components.button.ButtonStyle
import com.valkiria.uicomponents.components.button.OnClick
import com.valkiria.uicomponents.components.chip.ChipStyle
import com.valkiria.uicomponents.components.label.TextStyle
import com.valkiria.uicomponents.components.textfield.TextFieldStyle
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.logging.HttpLoggingInterceptor
import javax.inject.Singleton

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
        TextFieldStyle::class.java,
        EnumJsonAdapter.create(TextFieldStyle::class.java)
            .withUnknownFallback(TextFieldStyle.OUTLINED)
    ).add(
        TextStyle::class.java,
        EnumJsonAdapter.create(TextStyle::class.java).withUnknownFallback(TextStyle.BODY_1)
    )

    @Suppress("LongMethod")
    private fun Moshi.Builder.provideBodyPolymorphicAdapter() = this.add(
        BodyRowType::class.java,
        EnumJsonAdapter.create(BodyRowType::class.java).withUnknownFallback(null)
    ).add(
        PolymorphicJsonAdapterFactory.of(
            com.skgtecnologia.sisem.data.remote.model.components.BodyRowResponse::class.java,
            "type"
        ).withSubtype(
            com.skgtecnologia.sisem.data.remote.model.components.button.ButtonResponse::class.java,
            BodyRowType.BUTTON.name
        ).withSubtype(
            ChipResponse::class.java,
            BodyRowType.CHIP.name
        ).withSubtype(
            ChipOptionsResponse::class.java,
            BodyRowType.CHIP_OPTIONS.name
        ).withSubtype(
            ChipSelectionResponse::class.java,
            BodyRowType.CHIP_SELECTION.name
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
            HeaderResponse::class.java,
            BodyRowType.HEADER.name
        ).withSubtype(
            InfoCardResponse::class.java,
            BodyRowType.INFO_CARD.name
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
            SliderResponse::class.java,
            BodyRowType.SLIDER.name
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
}
