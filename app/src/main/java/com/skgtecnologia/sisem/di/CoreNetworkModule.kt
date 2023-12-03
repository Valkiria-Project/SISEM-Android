package com.skgtecnologia.sisem.di

import com.skgtecnologia.sisem.BuildConfig
import com.skgtecnologia.sisem.data.remote.adapters.ArrangementAdapter
import com.skgtecnologia.sisem.data.remote.adapters.KeyboardOptionsAdapter
import com.skgtecnologia.sisem.data.remote.adapters.ModifierAdapter
import com.skgtecnologia.sisem.data.remote.model.components.BodyRowResponse
import com.skgtecnologia.sisem.data.remote.model.components.body.HumanBodyResponse
import com.skgtecnologia.sisem.data.remote.model.components.button.ButtonResponse
import com.skgtecnologia.sisem.data.remote.model.components.button.ImageButtonResponse
import com.skgtecnologia.sisem.data.remote.model.components.button.ImageButtonSectionResponse
import com.skgtecnologia.sisem.data.remote.model.components.card.InfoCardResponse
import com.skgtecnologia.sisem.data.remote.model.components.card.SimpleCardResponse
import com.skgtecnologia.sisem.data.remote.model.components.card.StaggeredCardsResponse
import com.skgtecnologia.sisem.data.remote.model.components.chip.ChipOptionsResponse
import com.skgtecnologia.sisem.data.remote.model.components.chip.ChipResponse
import com.skgtecnologia.sisem.data.remote.model.components.chip.ChipSelectionResponse
import com.skgtecnologia.sisem.data.remote.model.components.chip.FiltersResponse
import com.skgtecnologia.sisem.data.remote.model.components.detailedinfolist.DetailedInfoListResponse
import com.skgtecnologia.sisem.data.remote.model.components.dropdown.DropDownResponse
import com.skgtecnologia.sisem.data.remote.model.components.finding.FindingResponse
import com.skgtecnologia.sisem.data.remote.model.components.fingerprint.FingerprintResponse
import com.skgtecnologia.sisem.data.remote.model.components.header.HeaderResponse
import com.skgtecnologia.sisem.data.remote.model.components.inventorycheck.InventoryCheckResponse
import com.skgtecnologia.sisem.data.remote.model.components.inventorysearch.InventorySearchResponse
import com.skgtecnologia.sisem.data.remote.model.components.label.LabelResponse
import com.skgtecnologia.sisem.data.remote.model.components.media.MediaActionsResponse
import com.skgtecnologia.sisem.data.remote.model.components.medsselector.MedsSelectorResponse
import com.skgtecnologia.sisem.data.remote.model.components.obstetrician.ObstetricianDataResponse
import com.skgtecnologia.sisem.data.remote.model.components.richlabel.RichLabelResponse
import com.skgtecnologia.sisem.data.remote.model.components.segmentedswitch.SegmentedSwitchResponse
import com.skgtecnologia.sisem.data.remote.model.components.signature.CrewMemberSignatureResponse
import com.skgtecnologia.sisem.data.remote.model.components.signature.SignatureResponse
import com.skgtecnologia.sisem.data.remote.model.components.slider.SliderResponse
import com.skgtecnologia.sisem.data.remote.model.components.stepper.StepperResponse
import com.skgtecnologia.sisem.data.remote.model.components.termsandconditions.TermsAndConditionsResponse
import com.skgtecnologia.sisem.data.remote.model.components.textfield.PasswordTextFieldResponse
import com.skgtecnologia.sisem.data.remote.model.components.textfield.TextFieldResponse
import com.skgtecnologia.sisem.data.remote.model.components.time.TimePickerResponse
import com.skgtecnologia.sisem.data.remote.model.components.timeline.TimelineResponse
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
            ChipSelectionResponse::class.java,
            BodyRowType.CHIP_SELECTION.name
        ).withSubtype(
            CrewMemberSignatureResponse::class.java,
            BodyRowType.CREW_MEMBER_SIGNATURE.name
        ).withSubtype(
            DetailedInfoListResponse::class.java,
            BodyRowType.DETAILED_INFO_LIST.name
        ).withSubtype(
            DropDownResponse::class.java,
            BodyRowType.DROP_DOWN.name
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
            HumanBodyResponse::class.java,
            BodyRowType.HUMAN_BODY.name
        ).withSubtype(
            ImageButtonResponse::class.java,
            BodyRowType.IMAGE_BUTTON.name
        ).withSubtype(
            ImageButtonSectionResponse::class.java,
            BodyRowType.IMAGE_BUTTON_SECTION.name
        ).withSubtype(
            InfoCardResponse::class.java,
            BodyRowType.INFO_CARD.name
        ).withSubtype(
            InventorySearchResponse::class.java,
            BodyRowType.INVENTORY_SEARCH.name
        ).withSubtype(
            MedsSelectorResponse::class.java,
            BodyRowType.INFO_CARD_BUTTON.name
        ).withSubtype(
            InventoryCheckResponse::class.java,
            BodyRowType.INVENTORY_CHECK.name
        ).withSubtype(
            LabelResponse::class.java,
            BodyRowType.LABEL.name
        ).withSubtype(
            MediaActionsResponse::class.java,
            BodyRowType.MEDIA_ACTIONS.name
        ).withSubtype(
            ObstetricianDataResponse::class.java,
            BodyRowType.OBSTETRICIAN_DATA.name
        ).withSubtype(
            PasswordTextFieldResponse::class.java,
            BodyRowType.PASSWORD_TEXT_FIELD.name
        ).withSubtype(
            RichLabelResponse::class.java,
            BodyRowType.RICH_LABEL.name
        ).withSubtype(
            SegmentedSwitchResponse::class.java,
            BodyRowType.SEGMENTED_SWITCH.name
        ).withSubtype(
            SignatureResponse::class.java,
            BodyRowType.SIGNATURE.name
        ).withSubtype(
            SimpleCardResponse::class.java,
            BodyRowType.SIMPLE_CARD.name
        ).withSubtype(
            SliderResponse::class.java,
            BodyRowType.SLIDER.name
        ).withSubtype(
            StaggeredCardsResponse::class.java,
            BodyRowType.STAGGERED_CARDS.name
        ).withSubtype(
            StepperResponse::class.java,
            BodyRowType.STEPPER.name
        ).withSubtype(
            TermsAndConditionsResponse::class.java,
            BodyRowType.TERMS_AND_CONDITIONS.name
        ).withSubtype(
            TextFieldResponse::class.java,
            BodyRowType.TEXT_FIELD.name
        ).withSubtype(
            TimelineResponse::class.java,
            BodyRowType.TIMELINE.name
        ).withSubtype(
            TimePickerResponse::class.java,
            BodyRowType.TIME_PICKER.name
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
