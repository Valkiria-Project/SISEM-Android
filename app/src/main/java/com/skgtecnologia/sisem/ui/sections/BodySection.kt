package com.skgtecnologia.sisem.ui.sections

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import com.skgtecnologia.sisem.R
import com.skgtecnologia.sisem.domain.authcards.model.AuthCardsIdentifier
import com.skgtecnologia.sisem.domain.changepassword.model.ChangePasswordIdentifier
import com.skgtecnologia.sisem.domain.deviceauth.model.DeviceAuthIdentifier
import com.skgtecnologia.sisem.domain.login.model.LoginIdentifier
import com.skgtecnologia.sisem.domain.login.model.mapToLoginModel
import com.skgtecnologia.sisem.domain.report.model.AddReportIdentifier
import com.skgtecnologia.sisem.domain.report.model.AddReportRoleIdentifier
import com.skgtecnologia.sisem.ui.dropdown.DropDownComponent
import com.skgtecnologia.sisem.ui.humanbody.HumanBodyComponent
import com.skgtecnologia.sisem.ui.medicalhistory.medicine.MedsSelectorComponent
import com.valkiria.uicomponents.action.AddReportUiAction
import com.valkiria.uicomponents.action.AuthCardsUiAction
import com.valkiria.uicomponents.action.ChangePasswordUiAction.ConfirmPasswordInput
import com.valkiria.uicomponents.action.ChangePasswordUiAction.NewPasswordInput
import com.valkiria.uicomponents.action.ChangePasswordUiAction.OldPasswordInput
import com.valkiria.uicomponents.action.DeviceAuthUiAction.DeviceAuthCodeInput
import com.valkiria.uicomponents.action.GenericUiAction
import com.valkiria.uicomponents.action.HeaderUiAction
import com.valkiria.uicomponents.action.LoginUiAction.ForgotPassword
import com.valkiria.uicomponents.action.LoginUiAction.Login
import com.valkiria.uicomponents.action.LoginUiAction.LoginPasswordInput
import com.valkiria.uicomponents.action.LoginUiAction.LoginUserInput
import com.valkiria.uicomponents.action.LoginUiAction.TermsAndConditions
import com.valkiria.uicomponents.action.NewsUiAction
import com.valkiria.uicomponents.action.UiAction
import com.valkiria.uicomponents.components.BodyRowModel
import com.valkiria.uicomponents.components.button.ButtonComponent
import com.valkiria.uicomponents.components.button.ButtonUiModel
import com.valkiria.uicomponents.components.button.ImageButtonSectionComponent
import com.valkiria.uicomponents.components.button.ImageButtonSectionUiModel
import com.valkiria.uicomponents.components.card.InfoCardComponent
import com.valkiria.uicomponents.components.card.InfoCardUiModel
import com.valkiria.uicomponents.components.card.SimpleCardComponent
import com.valkiria.uicomponents.components.card.SimpleCardUiModel
import com.valkiria.uicomponents.components.card.StaggeredCardsComponent
import com.valkiria.uicomponents.components.card.StaggeredCardsUiModel
import com.valkiria.uicomponents.components.chip.ChipComponent
import com.valkiria.uicomponents.components.chip.ChipOptionsComponent
import com.valkiria.uicomponents.components.chip.ChipOptionsUiModel
import com.valkiria.uicomponents.components.chip.ChipSelectionComponent
import com.valkiria.uicomponents.components.chip.ChipSelectionUiModel
import com.valkiria.uicomponents.components.chip.ChipUiModel
import com.valkiria.uicomponents.components.chip.FiltersComponent
import com.valkiria.uicomponents.components.chip.FiltersUiModel
import com.valkiria.uicomponents.components.detailedinfolist.DetailedInfoListComponent
import com.valkiria.uicomponents.components.detailedinfolist.DetailedInfoListUiModel
import com.valkiria.uicomponents.components.dropdown.DropDownUiModel
import com.valkiria.uicomponents.components.finding.FindingComponent
import com.valkiria.uicomponents.components.finding.FindingUiModel
import com.valkiria.uicomponents.components.fingerprint.FingerprintUiModel
import com.valkiria.uicomponents.components.footer.FooterBodyUiModel
import com.valkiria.uicomponents.components.footer.mapToSection
import com.valkiria.uicomponents.components.header.HeaderUiModel
import com.valkiria.uicomponents.components.humanbody.HumanBodyUiModel
import com.valkiria.uicomponents.components.inventorycheck.InventoryCheckComponent
import com.valkiria.uicomponents.components.inventorycheck.InventoryCheckUiModel
import com.valkiria.uicomponents.components.inventorysearch.InventorySearchComponent
import com.valkiria.uicomponents.components.inventorysearch.InventorySearchUiModel
import com.valkiria.uicomponents.components.label.LabelComponent
import com.valkiria.uicomponents.components.label.LabelUiModel
import com.valkiria.uicomponents.components.media.MediaActionsComponent
import com.valkiria.uicomponents.components.media.MediaActionsUiModel
import com.valkiria.uicomponents.components.medsselector.MedsSelectorUiModel
import com.valkiria.uicomponents.components.obstetrician.ObstetricianDataComponent
import com.valkiria.uicomponents.components.obstetrician.ObstetricianDataUiModel
import com.valkiria.uicomponents.components.richlabel.RichLabelComponent
import com.valkiria.uicomponents.components.richlabel.RichLabelUiModel
import com.valkiria.uicomponents.components.segmentedswitch.SegmentedSwitchComponent
import com.valkiria.uicomponents.components.segmentedswitch.SegmentedSwitchUiModel
import com.valkiria.uicomponents.components.signature.CrewMemberSignatureComponent
import com.valkiria.uicomponents.components.signature.CrewMemberSignatureUiModel
import com.valkiria.uicomponents.components.signature.SignatureComponent
import com.valkiria.uicomponents.components.signature.SignatureUiModel
import com.valkiria.uicomponents.components.slider.SliderComponent
import com.valkiria.uicomponents.components.slider.SliderUiModel
import com.valkiria.uicomponents.components.stepper.StepperComponent
import com.valkiria.uicomponents.components.stepper.StepperUiModel
import com.valkiria.uicomponents.components.termsandconditions.TermsAndConditionsComponent
import com.valkiria.uicomponents.components.termsandconditions.TermsAndConditionsUiModel
import com.valkiria.uicomponents.components.textfield.PasswordTextFieldComponent
import com.valkiria.uicomponents.components.textfield.PasswordTextFieldUiModel
import com.valkiria.uicomponents.components.textfield.TextFieldComponent
import com.valkiria.uicomponents.components.textfield.TextFieldUiModel
import com.valkiria.uicomponents.components.timeline.TimelineComponent
import com.valkiria.uicomponents.components.timeline.TimelineUiModel
import com.valkiria.uicomponents.components.timepicker.TimePickerComponent
import com.valkiria.uicomponents.components.timepicker.TimePickerUiModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import timber.log.Timber

private const val FILTERS_SCROLL_OFFSET = -120

@Suppress("ComplexMethod", "LongMethod")
@Composable
fun BodySection(
    body: List<BodyRowModel>?,
    modifier: Modifier = Modifier,
    validateFields: Boolean = false,
    onAction: (actionInput: UiAction) -> Unit
) {
    val listState = rememberLazyListState()
    val scope = rememberCoroutineScope()

    if (body?.isNotEmpty() == true) {
        Box(modifier = modifier.fillMaxSize()) {
            val stickyFooter = getStickyFooter(body)
            val updatedModifier = if (stickyFooter != null) {
                modifier.then(Modifier.padding(bottom = 60.dp))
            } else {
                modifier
            }

            val nestedScrollConnection = remember {
                object : NestedScrollConnection {
                    override fun onPreScroll(
                        available: Offset,
                        source: NestedScrollSource
                    ): Offset {
                        val delta = available.y
                        Timber.d("onPreScroll $delta")
                        // called when you scroll the content
                        return Offset.Zero
                    }
                }
            }

            LazyColumn(
                modifier = updatedModifier.nestedScroll(nestedScrollConnection),
                state = listState,
                contentPadding = PaddingValues(horizontal = 20.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(0.dp, Alignment.Top),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                handleBodyRows(
                    body = body,
                    listState = listState,
                    coroutineScope = scope,
                    validateFields = validateFields,
                    onAction = onAction
                )
            }

            stickyFooter?.let { model ->
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.BottomCenter)
                ) {
                    StepperComponent(uiModel = model) { selectedIndex ->
                        scope.launch {
                            val selected = model.options[selectedIndex.toString()]

                            val contentHeader = body.indexOfFirst {
                                it is HeaderUiModel && it.title.text == selected
                            }

                            if (contentHeader >= 0) {
                                listState.animateScrollToItem(
                                    index = contentHeader,
                                    scrollOffset = -100
                                )
                            }
                        }

                        if (selectedIndex == null) {
                            onAction(GenericUiAction.StepperAction(identifier = model.identifier))
                        }
                    }
                }
            }
        }
    }
}

private fun getStickyFooter(body: List<BodyRowModel>): StepperUiModel? = body
    .filterIsInstance<StepperUiModel>()
    .firstOrNull()

@Suppress("ComplexMethod", "LongMethod", "LongParameterList")
private fun LazyListScope.handleBodyRows(
    body: List<BodyRowModel>,
    listState: LazyListState,
    coroutineScope: CoroutineScope,
    validateFields: Boolean,
    onAction: (actionInput: UiAction) -> Unit
) {
    body.forEach { model ->
        when (model) {
            is ButtonUiModel -> if (model.visibility) {
                item(key = model.identifier) {
                    HandleButtonRows(model, onAction)
                }
            }

            is ChipUiModel -> if (model.visibility) {
                item(key = model.identifier) {
                    HandleChipRows(model, onAction)
                }
            }

            is ChipOptionsUiModel -> if (model.visibility) {
                item(key = model.identifier) {
                    ChipOptionsComponent(
                        uiModel = model,
                        validateFields = validateFields
                    ) { id, chipOptionItem ->
                        onAction(
                            GenericUiAction.ChipOptionAction(
                                identifier = id,
                                chipOptionUiModel = chipOptionItem
                            )
                        )
                    }
                }
            }

            is ChipSelectionUiModel -> if (model.visibility) {
                item(key = model.identifier) {
                    ChipSelectionComponent(
                        uiModel = model,
                        validateFields = validateFields
                    ) { id, chipSelectionItem, isSelection, viewsVisibility ->
                        onAction(
                            GenericUiAction.ChipSelectionAction(
                                identifier = id,
                                chipSelectionItemUiModel = chipSelectionItem,
                                status = isSelection,
                                viewsVisibility = viewsVisibility
                            )
                        )
                    }
                }
            }

            is CrewMemberSignatureUiModel -> if (model.visibility) {
                item(key = model.identifier) {
                    CrewMemberSignatureComponent(uiModel = model)
                }
            }

            is DetailedInfoListUiModel -> if (model.visibility) {
                item(key = model.identifier) {
                    DetailedInfoListComponent(uiModel = model)
                }
            }

            is DropDownUiModel -> if (model.visibility) {
                item(key = model.identifier) {
                    DropDownComponent(model, validateFields) { dropDownInputUiModel ->
                        onAction(
                            GenericUiAction.DropDownAction(
                                identifier = dropDownInputUiModel.identifier,
                                id = dropDownInputUiModel.id,
                                name = dropDownInputUiModel.name,
                                quantity = dropDownInputUiModel.quantity,
                                fieldValidated = dropDownInputUiModel.fieldValidated
                            )
                        )
                    }
                }
            }

            is FiltersUiModel -> if (model.visibility) {
                stickyHeader(key = model.identifier) {
                    FiltersComponent(uiModel = model) { selected, _ ->
                        coroutineScope.launch {
                            val contentHeader = body.indexOfFirst {
                                it is HeaderUiModel && it.title.text == selected
                            }

                            if (contentHeader >= 0) {
                                listState.animateScrollToItem(
                                    index = contentHeader,
                                    scrollOffset = FILTERS_SCROLL_OFFSET
                                )
                            }
                        }
                    }

                    // FIXME: Send data to Stepper or create shared fun
                }
            }

            is FindingUiModel -> if (model.visibility) {
                item(key = model.identifier) {
                    FindingComponent(uiModel = model) { id, status, findingDetail ->
                        onAction(
                            GenericUiAction.FindingAction(
                                identifier = id,
                                status = status,
                                findingDetail = findingDetail
                            )
                        )
                    }
                }
            }

            is FingerprintUiModel -> item(key = model.identifier) {
                Image(
                    modifier = Modifier.padding(vertical = 20.dp),
                    imageVector = ImageVector.vectorResource(id = R.drawable.ic_login_fingerprint),
                    contentDescription = null
                )
            }

            is FooterBodyUiModel -> item(key = model.identifier) {
                FooterSection(footerModel = model.mapToSection()) { uiAction ->
                    onAction(uiAction)
                }
            }

            is HeaderUiModel -> if (model.visibility) {
                item(key = model.identifier) {
                    HeaderSection(
                        headerUiModel = model
                    ) {
                        onAction(HeaderUiAction.GoBack)
                    }
                }
            }

            is HumanBodyUiModel -> if (model.visibility) {
                item(key = model.identifier) {
                    HumanBodyComponent(model) { values ->
                        onAction(GenericUiAction.HumanBodyAction(model.identifier, values))
                    }
                }
            }

            is ImageButtonSectionUiModel -> if (model.visibility) {
                item(key = model.identifier) {
                    ImageButtonSectionComponent(
                        uiModel = model,
                        validateFields = validateFields
                    ) { id, itemId ->
                        onAction(
                            GenericUiAction.ImageButtonAction(
                                identifier = id,
                                itemIdentifier = itemId
                            )
                        )
                    }
                }
            }

            is InfoCardUiModel -> if (model.visibility) {
                item(key = model.identifier) {
                    InfoCardComponent(
                        uiModel = model,
                        onAction = { cardUiModel ->
                            onAction(
                                GenericUiAction.InfoCardAction(
                                    identifier = cardUiModel.identifier,
                                    isPill = cardUiModel.isPill,
                                    patient = cardUiModel.patientAph,
                                    isClickCard = cardUiModel.isClickCard,
                                    reportDetail = cardUiModel.reportDetail,
                                    chipSection = cardUiModel.chipSection
                                )
                            )
                        }
                    )
                }
            }

            is InventoryCheckUiModel -> if (model.visibility) {
                item(key = model.identifier) {
                    InventoryCheckComponent(
                        uiModel = model,
                        validateFields = validateFields
                    ) { inputUiModel ->
                        onAction(
                            GenericUiAction.InventoryAction(
                                identifier = model.identifier,
                                itemIdentifier = inputUiModel.identifier,
                                updatedValue = inputUiModel.updatedValue,
                                fieldValidated = inputUiModel.fieldValidated
                            )
                        )
                    }
                }
            }

            is InventorySearchUiModel -> if (model.visibility) {
                item(key = model.identifier) {
                    InventorySearchComponent(uiModel = model)
                }
            }

            is LabelUiModel -> if (model.visibility) {
                item(key = model.identifier) {
                    LabelComponent(uiModel = model)
                }
            }

            is MediaActionsUiModel -> if (model.visibility) {
                item(key = model.identifier) {
                    val mediaActionsIndex by remember {
                        mutableIntStateOf(body.indexOfFirst { it is MediaActionsUiModel })
                    }

                    MediaActionsComponent(
                        uiModel = model,
                        listState = listState,
                        mediaActionsIndex = mediaActionsIndex
                    ) { id, mediaAction ->
                        onAction(
                            GenericUiAction.MediaItemAction(
                                identifier = id,
                                mediaAction = mediaAction
                            )
                        )
                    }
                }
            }

            is MedsSelectorUiModel -> if (model.visibility) {
                item(key = model.identifier) {
                    MedsSelectorComponent(uiModel = model) { id ->
                        onAction(GenericUiAction.MedsSelectorAction(identifier = id))
                    }
                }
            }

            is ObstetricianDataUiModel -> item(key = model.identifier) {
                ObstetricianDataComponent(uiModel = model)
            }

            is PasswordTextFieldUiModel -> if (model.visibility) {
                item(key = model.identifier) {
                    HandlePasswordTextFieldRows(model, validateFields, onAction)
                }
            }

            is RichLabelUiModel -> if (model.visibility) {
                item(key = model.identifier) {
                    RichLabelComponent(uiModel = model)
                }
            }

            is SegmentedSwitchUiModel -> if (model.visibility) {
                item(key = model.identifier) {
                    SegmentedSwitchComponent(uiModel = model) { id, status, viewsVisibility ->
                        onAction(
                            GenericUiAction.SegmentedSwitchAction(
                                identifier = id,
                                status = status,
                                viewsVisibility = viewsVisibility
                            )
                        )
                    }
                }
            }

            is SignatureUiModel -> if (model.visibility) {
                item(key = model.identifier) {
                    SignatureComponent(
                        uiModel = model,
                        validateFields = validateFields
                    ) { id ->
                        onAction(GenericUiAction.SignatureAction(identifier = id))
                    }
                }
            }

            is SimpleCardUiModel -> if (model.visibility) {
                item(key = model.identifier) {
                    SimpleCardComponent(uiModel = model) { identifier ->
                        onAction(GenericUiAction.SimpleCardAction(identifier = identifier))
                    }
                }
            }

            is SliderUiModel -> if (model.visibility) {
                item(key = model.identifier) {
                    SliderComponent(uiModel = model) { id, value ->
                        onAction(GenericUiAction.SliderAction(identifier = id, value = value))
                    }
                }
            }

            is StaggeredCardsUiModel -> item(key = model.identifier) {
                StaggeredCardsComponent(uiModel = model)
            }

            is TermsAndConditionsUiModel -> if (model.visibility) {
                item(key = model.identifier) {
                    TermsAndConditionsComponent(uiModel = model.mapToLoginModel()) { link ->
                        onAction(TermsAndConditions(link = link))
                    }
                }
            }

            is TextFieldUiModel -> if (model.visibility) {
                item(key = model.identifier) {
                    HandleTextFieldRows(model, validateFields, onAction)
                }
            }

            is TimelineUiModel -> item(key = model.identifier) {
                TimelineComponent(uiModel = model)
            }

            is TimePickerUiModel -> if (model.visibility) {
                item(key = model.identifier) {
                    TimePickerComponent(uiModel = model) { id, time ->
                        onAction(GenericUiAction.TimePickerAction(identifier = id, value = time))
                    }
                }
            }
        }
    }
}

@Composable
private fun HandleButtonRows(
    model: ButtonUiModel,
    onAction: (actionInput: UiAction) -> Unit
) {
    when (model.identifier) {
        AuthCardsIdentifier.CREW_MEMBER_CARD_ADMIN_BUTTON.name -> ButtonComponent(
            uiModel = model
        ) {
            onAction(AuthCardsUiAction.AuthCard)
        }

        LoginIdentifier.LOGIN_FORGOT_PASSWORD_BUTTON.name -> ButtonComponent(
            uiModel = model
        ) {
            onAction(ForgotPassword)
        }

        LoginIdentifier.LOGIN_BUTTON.name -> ButtonComponent(
            uiModel = model
        ) {
            onAction(Login)
        }

        else -> ButtonComponent(
            uiModel = model
        ) { id ->
            onAction(GenericUiAction.ButtonAction(id))
        }
    }
}

@Suppress("UnusedPrivateMember")
@Composable
private fun HandleChipRows(
    model: ChipUiModel,
    onAction: (actionInput: UiAction) -> Unit
) {
    when (model.identifier) {
        AddReportRoleIdentifier.ADD_REPORT_ROLE_CHIP_ASSISTANT.name,
        AddReportRoleIdentifier.ADD_REPORT_ROLE_CHIP_DOCTOR.name,
        AddReportRoleIdentifier.ADD_REPORT_ROLE_CHIP_DRIVER.name -> ChipComponent(
            uiModel = model,
            onAction = { onAction(NewsUiAction.NewsStepOneOnChipClick) }
        )

        else -> ChipComponent(
            uiModel = model,
        )
    }
}

@Composable
private fun HandlePasswordTextFieldRows(
    model: PasswordTextFieldUiModel,
    validateFields: Boolean,
    onAction: (actionInput: UiAction) -> Unit
) {
    when (model.identifier) {
        ChangePasswordIdentifier.CHANGE_PASSWORD_CONFIRM.name -> PasswordTextFieldComponent(
            uiModel = model,
            validateFields = validateFields
        ) { updatedValue, fieldValidated ->
            onAction(
                ConfirmPasswordInput(
                    updatedValue = updatedValue,
                    fieldValidated = fieldValidated
                )
            )
        }

        ChangePasswordIdentifier.CHANGE_PASSWORD_NEW.name -> PasswordTextFieldComponent(
            uiModel = model,
            validateFields = validateFields
        ) { updatedValue, fieldValidated ->
            onAction(
                NewPasswordInput(
                    updatedValue = updatedValue,
                    fieldValidated = fieldValidated
                )
            )
        }

        LoginIdentifier.LOGIN_PASSWORD.name -> PasswordTextFieldComponent(
            uiModel = model,
            validateFields = validateFields
        ) { updatedValue, fieldValidated ->
            onAction(
                LoginPasswordInput(
                    updatedValue = updatedValue,
                    fieldValidated = fieldValidated
                )
            )
        }
    }
}

@Suppress("LongMethod")
@Composable
private fun HandleTextFieldRows(
    model: TextFieldUiModel,
    validateFields: Boolean,
    onAction: (actionInput: UiAction) -> Unit
) {
    when (model.identifier) {
        DeviceAuthIdentifier.DEVICE_AUTH_CODE.name -> TextFieldComponent(
            uiModel = model,
            validateFields = validateFields
        ) { inputUiModel ->
            onAction(
                DeviceAuthCodeInput(
                    updatedValue = inputUiModel.updatedValue,
                    fieldValidated = inputUiModel.fieldValidated
                )
            )
        }

        LoginIdentifier.LOGIN_EMAIL.name -> TextFieldComponent(
            uiModel = model,
            validateFields = validateFields
        ) { inputUiModel ->
            onAction(
                LoginUserInput(
                    updatedValue = inputUiModel.updatedValue,
                    fieldValidated = inputUiModel.fieldValidated
                )
            )
        }

        ChangePasswordIdentifier.CHANGE_PASSWORD_CURRENT.name -> TextFieldComponent(
            uiModel = model,
            validateFields = validateFields
        ) { inputUiModel ->
            onAction(
                OldPasswordInput(
                    updatedValue = inputUiModel.updatedValue
                )
            )
        }

        AddReportIdentifier.ADD_REPORT_ENTRY_TOPIC.name -> TextFieldComponent(
            uiModel = model,
            validateFields = validateFields
        ) { inputUiModel ->
            onAction(
                AddReportUiAction.TopicInput(
                    updatedValue = inputUiModel.updatedValue,
                    fieldValidated = inputUiModel.fieldValidated
                )
            )
        }

        AddReportIdentifier.ADD_REPORT_ENTRY_DESCRIPTION.name -> TextFieldComponent(
            uiModel = model,
            validateFields = validateFields
        ) { inputUiModel ->
            onAction(
                AddReportUiAction.DescriptionInput(
                    updatedValue = inputUiModel.updatedValue,
                    fieldValidated = inputUiModel.fieldValidated
                )
            )
        }

        else -> TextFieldComponent(
            uiModel = model,
            validateFields = validateFields
        ) { inputUiModel ->
            onAction(
                GenericUiAction.InputAction(
                    identifier = inputUiModel.identifier,
                    updatedValue = inputUiModel.updatedValue,
                    fieldValidated = inputUiModel.fieldValidated,
                    required = inputUiModel.required
                )
            )
        }
    }
}
