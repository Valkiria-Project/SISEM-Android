package com.skgtecnologia.sisem.ui.sections

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.skgtecnologia.sisem.R
import com.skgtecnologia.sisem.domain.authcards.model.AuthCardsIdentifier
import com.skgtecnologia.sisem.domain.changepassword.model.ChangePasswordIdentifier
import com.skgtecnologia.sisem.domain.deviceauth.model.DeviceAuthIdentifier
import com.skgtecnologia.sisem.domain.login.model.LoginIdentifier
import com.valkiria.uicomponents.model.ui.body.ButtonModel
import com.valkiria.uicomponents.model.ui.body.ChipUiModel
import com.valkiria.uicomponents.model.ui.body.ChipOptionsUiModel
import com.valkiria.uicomponents.model.ui.body.ChipSelectionUiModel
import com.valkiria.uicomponents.model.ui.body.DetailedInfoListUiModel
import com.valkiria.uicomponents.model.ui.body.DropDownUiModel
import com.valkiria.uicomponents.model.ui.body.FiltersUiModel
import com.skgtecnologia.sisem.domain.model.body.FindingModel
import com.valkiria.uicomponents.model.ui.body.FingerprintUiModel
import com.skgtecnologia.sisem.domain.model.body.FooterBodyModel
import com.valkiria.uicomponents.model.ui.body.HeaderUiModel
import com.valkiria.uicomponents.model.ui.body.InfoCardUiModel
import com.valkiria.uicomponents.model.ui.body.InventoryCheckUiModel
import com.skgtecnologia.sisem.domain.model.body.LabelModel
import com.skgtecnologia.sisem.domain.model.body.PasswordTextFieldModel
import com.skgtecnologia.sisem.domain.model.body.RichLabelModel
import com.valkiria.uicomponents.model.ui.body.SegmentedSwitchUiModel
import com.skgtecnologia.sisem.domain.model.body.SliderModel
import com.skgtecnologia.sisem.domain.model.body.TermsAndConditionsModel
import com.skgtecnologia.sisem.domain.model.body.TextFieldModel
import com.skgtecnologia.sisem.domain.model.body.mapToSection
import com.valkiria.uicomponents.model.ui.body.mapToUiModel
import com.skgtecnologia.sisem.domain.report.model.AddReportIdentifier
import com.skgtecnologia.sisem.domain.report.model.AddReportRoleIdentifier
import com.valkiria.uicomponents.action.AuthCardsUiAction
import com.valkiria.uicomponents.action.ChangePasswordUiAction.ConfirmPasswordInput
import com.valkiria.uicomponents.action.ChangePasswordUiAction.NewPasswordInput
import com.valkiria.uicomponents.action.ChangePasswordUiAction.OldPasswordInput
import com.valkiria.uicomponents.action.DeviceAuthUiAction.DeviceAuthCodeInput
import com.valkiria.uicomponents.action.GenericUiAction
import com.valkiria.uicomponents.action.LoginUiAction.ForgotPassword
import com.valkiria.uicomponents.action.LoginUiAction.Login
import com.valkiria.uicomponents.action.LoginUiAction.LoginPasswordInput
import com.valkiria.uicomponents.action.LoginUiAction.LoginUserInput
import com.valkiria.uicomponents.action.LoginUiAction.TermsAndConditions
import com.valkiria.uicomponents.action.NewsUiAction
import com.valkiria.uicomponents.action.RecordNewsUiAction
import com.valkiria.uicomponents.action.UiAction
import com.valkiria.uicomponents.components.button.ButtonComponent
import com.valkiria.uicomponents.components.card.InfoCardComponent
import com.valkiria.uicomponents.components.chip.ChipComponent
import com.valkiria.uicomponents.components.chip.ChipOptionsComponent
import com.valkiria.uicomponents.components.chip.ChipSelectionComponent
import com.valkiria.uicomponents.components.chip.FiltersComponent
import com.valkiria.uicomponents.components.detailedinfolist.DetailedInfoListComponent
import com.valkiria.uicomponents.components.finding.FindingComponent
import com.valkiria.uicomponents.components.inventorycheck.InventoryCheckComponent
import com.valkiria.uicomponents.components.label.LabelComponent
import com.valkiria.uicomponents.components.richlabel.RichLabelComponent
import com.valkiria.uicomponents.components.segmentedswitch.SegmentedSwitchComponent
import com.valkiria.uicomponents.components.slider.SliderComponent
import com.valkiria.uicomponents.components.termsandconditions.TermsAndConditionsComponent
import com.valkiria.uicomponents.components.textfield.PasswordTextFieldComponent
import com.valkiria.uicomponents.components.textfield.TextFieldComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Suppress("ComplexMethod", "LongMethod")
@Composable
fun BodySection(
    body: List<BodyRowModel>?,
    modifier: Modifier = Modifier,
    validateFields: Boolean = false,
    onAction: (actionInput: UiAction) -> Unit
) {
    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()

    if (body?.isNotEmpty() == true) {
        LazyColumn(
            modifier = modifier,
            state = listState,
            contentPadding = PaddingValues(horizontal = 20.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(0.dp, Alignment.Top),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            handleBodyRows(
                body = body,
                listState = listState,
                coroutineScope = coroutineScope,
                validateFields = validateFields,
                onAction = onAction
            )
        }
    }
}

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
            is ButtonModel -> item(key = model.identifier) {
                HandleButtonRows(model, onAction)
            }

            is ChipUiModel -> item(key = model.identifier) {
                HandleChipRows(model, onAction)
            }

            is ChipOptionsUiModel -> item(key = model.identifier) {
                ChipOptionsComponent(uiModel = model.mapToUiModel()) { id, text, isSelection ->
                    onAction(
                        GenericUiAction.ChipOptionAction(
                            identifier = id,
                            text = text,
                            status = isSelection
                        )
                    )
                }
            }

            is ChipSelectionUiModel -> item(key = model.identifier) {
                ChipSelectionComponent(uiModel = model.mapToUiModel()) { id, text, isSelection ->
                    onAction(
                        GenericUiAction.ChipSelectionAction(
                            identifier = id,
                            text = text,
                            status = isSelection
                        )
                    )
                }
            }

            is DropDownUiModel -> item(key = model.identifier) {
                TODO("Create DropDownComponent")
            }

            is InfoCardUiModel -> item(key = model.identifier) {
                HandleInfoCardRows(model, onAction)
            }

            is DetailedInfoListUiModel -> item(key = model.identifier) {
                DetailedInfoListComponent(uiModel = model.mapToUiModel())
            }

            is FiltersUiModel -> stickyHeader(key = model.identifier) {
                FiltersComponent(
                    uiModel = model.mapToUiModel()
                ) { selected, _ ->
                    coroutineScope.launch {
                        val contentHeader = body.indexOfFirst {
                            it is HeaderUiModel && it.title.text == selected
                        }

                        if (contentHeader >= 0) {
                            listState.animateScrollToItem(index = contentHeader)
                        }
                    }
                }
            }

            is FindingModel -> item(key = model.identifier) {
                FindingComponent(
                    uiModel = model.mapToUiModel(),
                ) { id, status ->
                    onAction(GenericUiAction.FindingAction(identifier = id, status = status))
                }
            }

            is FingerprintUiModel -> item(key = model.identifier) {
                Image(
                    modifier = Modifier.padding(vertical = 20.dp),
                    painter = painterResource(id = R.drawable.ic_login_fingerprint),
                    contentDescription = null
                )
            }

            is FooterBodyModel -> item(key = model.identifier) {
                FooterSection(
                    footerModel = model.mapToSection()
                ) { uiAction ->
                    onAction(uiAction)
                }
            }

            is HeaderUiModel -> item(key = model.identifier) {
                HeaderSection(
                    headerUiModel = model
                )
            }

            is InventoryCheckUiModel -> item(key = model.identifier) {
                InventoryCheckComponent(
                    uiModel = model.mapToUiModel(),
                    validateFields
                ) { id, updatedValue, fieldValidated ->
                    onAction(
                        GenericUiAction.InventoryAction(
                            identifier = id,
                            updatedValue = updatedValue,
                            fieldValidated = fieldValidated
                        )
                    )
                }
            }

            is LabelModel -> item(key = model.identifier) {
                LabelComponent(uiModel = model.mapToUiModel())
            }

            is SegmentedSwitchUiModel -> item(key = model.identifier) {
                SegmentedSwitchComponent(uiModel = model.mapToUiModel()) { id, status ->
                    onAction(
                        GenericUiAction.SegmentedSwitchAction(
                            identifier = id,
                            status = status
                        )
                    )
                }
            }

            is SliderModel -> item(key = model.identifier) {
                SliderComponent(uiModel = model.mapToUiModel()) { id, value ->
                    onAction(GenericUiAction.SliderAction(identifier = id, value = value))
                }
            }

            is PasswordTextFieldModel -> item(key = model.identifier) {
                HandlePasswordTextFieldRows(model, validateFields, onAction)
            }

            is RichLabelModel -> item(key = model.identifier) {
                RichLabelComponent(uiModel = model.mapToUiModel())
            }

            is TermsAndConditionsModel -> item(key = model.identifier) {
                TermsAndConditionsComponent(uiModel = model.mapToUiModel()) { link ->
                    onAction(TermsAndConditions(link = link))
                }
            }

            is TextFieldModel -> item(key = model.identifier) {
                HandleTextFieldRows(model, validateFields, onAction)
            }
        }
    }
}

@Composable
private fun HandleButtonRows(
    model: ButtonModel,
    onAction: (actionInput: UiAction) -> Unit
) {
    when (model.identifier) {
        AuthCardsIdentifier.CREW_MEMBER_CARD_ADMIN_BUTTON.name -> ButtonComponent(
            uiModel = model.mapToUiModel()
        ) {
            onAction(AuthCardsUiAction.AuthCard)
        }

        LoginIdentifier.LOGIN_FORGOT_PASSWORD_BUTTON.name -> ButtonComponent(
            uiModel = model.mapToUiModel()
        ) {
            onAction(ForgotPassword)
        }

        LoginIdentifier.LOGIN_BUTTON.name -> ButtonComponent(
            uiModel = model.mapToUiModel()
        ) {
            onAction(Login)
        }

        else -> ButtonComponent(
            uiModel = model.mapToUiModel()
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
            uiModel = model.mapToUiModel(),
            onAction = { onAction(NewsUiAction.NewsStepOneOnChipClick) }
        )

        else -> ChipComponent(
            uiModel = model.mapToUiModel(),
        )
    }
}

@Composable
private fun HandleInfoCardRows(
    model: InfoCardUiModel,
    onAction: (actionInput: UiAction) -> Unit
) {
    when (model.identifier) {
        AuthCardsIdentifier.CREW_MEMBER_CARD_ASSISTANT.name,
        AuthCardsIdentifier.CREW_MEMBER_CARD_DRIVER.name,
        AuthCardsIdentifier.CREW_MEMBER_CARD_DOCTOR.name -> {
            InfoCardComponent(
                uiModel = model.mapToUiModel(),
                onAction = { onAction(AuthCardsUiAction.AuthCard) },
                onNewsAction = { onAction(AuthCardsUiAction.AuthCardNews(it)) },
                onFindingsAction = { onAction(AuthCardsUiAction.AuthCardFindings(it)) }
            )
        }
        else -> InfoCardComponent(
            uiModel = model.mapToUiModel(),
            onAction = { onAction(AuthCardsUiAction.AuthCard) }
        )
    }
}

@Composable
private fun HandlePasswordTextFieldRows(
    model: PasswordTextFieldModel,
    validateFields: Boolean,
    onAction: (actionInput: UiAction) -> Unit
) {
    when (model.identifier) {
        ChangePasswordIdentifier.CHANGE_PASSWORD_CONFIRM.name -> PasswordTextFieldComponent(
            uiModel = model.mapToUiModel(),
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
            uiModel = model.mapToUiModel(),
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
            uiModel = model.mapToUiModel(),
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
    model: TextFieldModel,
    validateFields: Boolean,
    onAction: (actionInput: UiAction) -> Unit
) {
    when (model.identifier) {
        DeviceAuthIdentifier.DEVICE_AUTH_CODE.name -> TextFieldComponent(
            uiModel = model.mapToUiModel(),
            validateFields = validateFields
        ) { _, updatedValue, fieldValidated ->
            onAction(
                DeviceAuthCodeInput(
                    updatedValue = updatedValue,
                    fieldValidated = fieldValidated
                )
            )
        }

        LoginIdentifier.LOGIN_EMAIL.name -> TextFieldComponent(
            uiModel = model.mapToUiModel(),
            validateFields = validateFields
        ) { _, updatedValue, fieldValidated ->
            onAction(
                LoginUserInput(
                    updatedValue = updatedValue,
                    fieldValidated = fieldValidated
                )
            )
        }

        ChangePasswordIdentifier.CHANGE_PASSWORD_CURRENT.name -> TextFieldComponent(
            uiModel = model.mapToUiModel(),
            validateFields = validateFields
        ) { _, updatedValue, _ ->
            onAction(
                OldPasswordInput(
                    updatedValue = updatedValue
                )
            )
        }

        AddReportIdentifier.ADD_REPORT_ENTRY_TOPIC.name -> TextFieldComponent(
            uiModel = model.mapToUiModel(),
            validateFields = validateFields
        ) { _, updatedValue, _ ->
            onAction(
                RecordNewsUiAction.TopicInput(
                    updatedValue = updatedValue,
                    fieldValidated = validateFields
                )
            )
        }

        AddReportIdentifier.ADD_REPORT_ENTRY_DESCRIPTION.name -> TextFieldComponent(
            uiModel = model.mapToUiModel(),
            validateFields = validateFields
        ) { _, updatedValue, _ ->
            onAction(
                RecordNewsUiAction.DescriptionInput(
                    updatedValue = updatedValue,
                    fieldValidated = validateFields
                )
            )
        }

        else -> TextFieldComponent(
            uiModel = model.mapToUiModel(),
            validateFields = validateFields
        ) { id, updatedValue, fieldValidated ->
            onAction(
                GenericUiAction.InputAction(
                    identifier = id, updatedValue = updatedValue, fieldValidated = fieldValidated
                )
            )
        }
    }
}
