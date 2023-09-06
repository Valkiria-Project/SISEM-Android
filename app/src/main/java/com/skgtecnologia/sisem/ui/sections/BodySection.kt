package com.skgtecnologia.sisem.ui.sections

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.skgtecnologia.sisem.R
import com.skgtecnologia.sisem.domain.authcards.model.AuthCardsIdentifier
import com.skgtecnologia.sisem.domain.changepassword.model.ChangePasswordIdentifier
import com.skgtecnologia.sisem.domain.deviceauth.model.DeviceAuthIdentifier
import com.skgtecnologia.sisem.domain.login.model.LoginIdentifier
import com.skgtecnologia.sisem.domain.model.body.BodyRowModel
import com.skgtecnologia.sisem.domain.model.body.ButtonModel
import com.skgtecnologia.sisem.domain.model.body.ChipModel
import com.skgtecnologia.sisem.domain.model.body.ChipOptionsModel
import com.skgtecnologia.sisem.domain.model.body.ContentHeaderModel
import com.skgtecnologia.sisem.domain.model.body.CrewMemberCardModel
import com.skgtecnologia.sisem.domain.model.body.DetailedInfoListModel
import com.skgtecnologia.sisem.domain.model.body.FiltersModel
import com.skgtecnologia.sisem.domain.model.body.FindingModel
import com.skgtecnologia.sisem.domain.model.body.FingerprintModel
import com.skgtecnologia.sisem.domain.model.body.InventoryCheckModel
import com.skgtecnologia.sisem.domain.model.body.LabelModel
import com.skgtecnologia.sisem.domain.model.body.PasswordTextFieldModel
import com.skgtecnologia.sisem.domain.model.body.RichLabelModel
import com.skgtecnologia.sisem.domain.model.body.SegmentedSwitchModel
import com.skgtecnologia.sisem.domain.model.body.TermsAndConditionsModel
import com.skgtecnologia.sisem.domain.model.body.TextFieldModel
import com.skgtecnologia.sisem.domain.model.body.mapToHeaderModel
import com.skgtecnologia.sisem.domain.model.body.mapToUiModel
import com.skgtecnologia.sisem.domain.news.model.NewsIdentifier
import com.skgtecnologia.sisem.domain.recordnews.model.RecordNewsIdentifier
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
import com.valkiria.uicomponents.components.card.CrewMemberCardComponent
import com.valkiria.uicomponents.components.chip.ChipComponent
import com.valkiria.uicomponents.components.chip.ChipOptionsComponent
import com.valkiria.uicomponents.components.chip.FiltersComponent
import com.valkiria.uicomponents.components.detailedinfolist.DetailedInfoListComponent
import com.valkiria.uicomponents.components.finding.FindingComponent
import com.valkiria.uicomponents.components.inventorycheck.InventoryCheckComponent
import com.valkiria.uicomponents.components.label.LabelComponent
import com.valkiria.uicomponents.components.richlabel.RichLabelComponent
import com.valkiria.uicomponents.components.segmentedswitch.SegmentedSwitchComponent
import com.valkiria.uicomponents.components.termsandconditions.TermsAndConditionsComponent
import com.valkiria.uicomponents.components.textfield.PasswordTextFieldComponent
import com.valkiria.uicomponents.components.textfield.TextFieldComponent
import timber.log.Timber

@Suppress("ComplexMethod", "LongMethod")
@Composable
fun BodySection(
    body: List<BodyRowModel>?,
    isTablet: Boolean,
    modifier: Modifier = Modifier,
    validateFields: Boolean = false,
    onAction: (actionInput: UiAction) -> Unit
) {
    if (body?.isNotEmpty() == true) {
        LazyColumn(
            modifier = modifier,
            contentPadding = PaddingValues(horizontal = 20.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(0.dp, Alignment.Top),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            handleBodyRows(body, isTablet, validateFields, onAction)
        }
    }
}

@Suppress("LongMethod", "ComplexMethod")
private fun LazyListScope.handleBodyRows(
    body: List<BodyRowModel>,
    isTablet: Boolean,
    validateFields: Boolean,
    onAction: (actionInput: UiAction) -> Unit
) {
    body.forEach { model ->
        when (model) {
            is ButtonModel -> item(key = model.identifier) {
                HandleButtonRows(model, isTablet, onAction)
            }

            is ChipModel -> item(key = model.identifier) {
                HandleChipRows(model, isTablet, onAction)
            }

            is ChipOptionsModel -> item(key = model.identifier) {
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

            is ContentHeaderModel -> item(key = model.identifier) {
                HeaderSection(
                    headerModel = model.mapToHeaderModel()
                )
            }

            is CrewMemberCardModel -> item(key = model.identifier) {
                HandleCrewMemberCardRows(model, isTablet, onAction)
            }

            is DetailedInfoListModel -> item(key = model.identifier) {
                DetailedInfoListComponent(
                    uiModel = model.mapToUiModel(),
                    isTablet = isTablet
                )
            }

            is FiltersModel -> stickyHeader(key = model.identifier) {
                FiltersComponent(
                    uiModel = model.mapToUiModel()
                ) { selected, isSelection ->
                    // FIXME: Finish this stuff
                    Timber.d("Selected $selected and is $isSelection")
                }
            }

            is FindingModel -> item(key = model.identifier) {
                FindingComponent(
                    uiModel = model.mapToUiModel(),
                    isTablet = isTablet
                ) { id, status ->
                    onAction(GenericUiAction.FindingAction(identifier = id, status = status))
                }
            }

            is FingerprintModel -> item(key = model.identifier) {
                Image(
                    modifier = Modifier.padding(vertical = 20.dp),
                    painter = painterResource(id = R.drawable.ic_login_fingerprint),
                    contentDescription = null
                )
            }

            is InventoryCheckModel -> item(key = model.identifier) {
                InventoryCheckComponent(
                    uiModel = model.mapToUiModel(), isTablet
                ) { id, updatedValue ->
                    onAction(
                        GenericUiAction.InventoryAction(
                            identifier = id,
                            updatedValue = updatedValue
                        )
                    )
                }
            }

            is LabelModel -> item(key = model.identifier) {
                LabelComponent(
                    uiModel = model.mapToUiModel(),
                    isTablet = isTablet
                )
            }

            is SegmentedSwitchModel -> item(key = model.identifier) {
                SegmentedSwitchComponent(
                    uiModel = model.mapToUiModel(),
                    isTablet = isTablet
                ) { id, status ->
                    onAction(
                        GenericUiAction.SegmentedSwitchAction(
                            identifier = id,
                            status = status
                        )
                    )
                }
            }

            is PasswordTextFieldModel -> item(key = model.identifier) {
                HandlePasswordTextFieldRows(model, isTablet, validateFields, onAction)
            }

            is RichLabelModel -> item(key = model.identifier) {
                RichLabelComponent(uiModel = model.mapToUiModel())
            }

            is TermsAndConditionsModel -> item(key = model.identifier) {
                TermsAndConditionsComponent(
                    uiModel = model.mapToUiModel(),
                    isTablet = isTablet
                ) { link ->
                    onAction(TermsAndConditions(link = link))
                }
            }

            is TextFieldModel -> item(key = model.identifier) {
                HandleTextFieldRows(model, isTablet, validateFields, onAction)
            }
        }
    }
}

@Composable
private fun HandleButtonRows(
    model: ButtonModel,
    isTablet: Boolean,
    onAction: (actionInput: UiAction) -> Unit
) {
    when (model.identifier) {
        AuthCardsIdentifier.CREW_MEMBER_CARD_ADMIN_BUTTON.name -> ButtonComponent(
            uiModel = model.mapToUiModel(),
            isTablet = isTablet
        ) {
            onAction(AuthCardsUiAction.AuthCard)
        }

        LoginIdentifier.LOGIN_FORGOT_PASSWORD_BUTTON.name -> ButtonComponent(
            uiModel = model.mapToUiModel(),
            isTablet = isTablet
        ) {
            onAction(ForgotPassword)
        }

        LoginIdentifier.LOGIN_BUTTON.name -> ButtonComponent(
            uiModel = model.mapToUiModel(),
            isTablet = isTablet
        ) {
            onAction(Login)
        }
    }
}

@Suppress("UnusedPrivateMember")
@Composable
private fun HandleChipRows(
    model: ChipModel,
    isTablet: Boolean,
    onAction: (actionInput: UiAction) -> Unit
) {
    when (model.identifier) {
        NewsIdentifier.ADD_REPORT_ROLE_CHIP_ASSISTANT.name,
        NewsIdentifier.ADD_REPORT_ROLE_CHIP_DOCTOR.name,
        NewsIdentifier.ADD_REPORT_ROLE_CHIP_DRIVER.name -> ChipComponent(
            uiModel = model.mapToUiModel(),
            isTablet = isTablet,
            onClick = { onAction(NewsUiAction.NewsStepOneOnChipClick(it)) }
        )

        else -> ChipComponent(
            uiModel = model.mapToUiModel(),
            isTablet = isTablet
        )
    }
}

@Composable
private fun HandleCrewMemberCardRows(
    model: CrewMemberCardModel,
    isTablet: Boolean,
    onAction: (actionInput: UiAction) -> Unit
) {
    when (model.identifier) {
        AuthCardsIdentifier.CREW_MEMBER_CARD_ASSISTANT.name,
        AuthCardsIdentifier.CREW_MEMBER_CARD_DRIVER.name,
        AuthCardsIdentifier.CREW_MEMBER_CARD_DOCTOR.name -> {
            CrewMemberCardComponent(
                uiModel = model.mapToUiModel(),
                isTablet = isTablet,
                onAction = { onAction(AuthCardsUiAction.AuthCard) },
                onNewsAction = { onAction(AuthCardsUiAction.AuthCardNews(it)) },
                onFindingsAction = { onAction(AuthCardsUiAction.AuthCardFindings(it)) }
            )
        }
    }
}

@Composable
private fun HandlePasswordTextFieldRows(
    model: PasswordTextFieldModel,
    isTablet: Boolean,
    validateFields: Boolean,
    onAction: (actionInput: UiAction) -> Unit
) {
    when (model.identifier) {
        ChangePasswordIdentifier.CHANGE_PASSWORD_CONFIRM.name -> PasswordTextFieldComponent(
            uiModel = model.mapToUiModel(),
            isTablet = isTablet,
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
            isTablet = isTablet,
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
            isTablet = isTablet,
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
    isTablet: Boolean,
    validateFields: Boolean,
    onAction: (actionInput: UiAction) -> Unit
) {
    when (model.identifier) {
        DeviceAuthIdentifier.DEVICE_AUTH_CODE.name -> TextFieldComponent(
            uiModel = model.mapToUiModel(),
            isTablet = isTablet
        ) { _, updatedValue, _ ->
            onAction(DeviceAuthCodeInput(updatedValue = updatedValue))
        }

        LoginIdentifier.LOGIN_EMAIL.name -> TextFieldComponent(
            uiModel = model.mapToUiModel(),
            isTablet = isTablet,
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
            isTablet = isTablet,
            validateFields = validateFields
        ) { _, updatedValue, _ ->
            onAction(
                OldPasswordInput(
                    updatedValue = updatedValue
                )
            )
        }

        RecordNewsIdentifier.ADD_REPORT_ENTRY_TOPIC.name -> TextFieldComponent(
            uiModel = model.mapToUiModel(),
            isTablet = isTablet,
            validateFields = validateFields
        ) { _, updatedValue, _ ->
            onAction(
                RecordNewsUiAction.TopicInput(
                    updatedValue = updatedValue,
                    fieldValidated = validateFields
                )
            )
        }

        RecordNewsIdentifier.ADD_REPORT_ENTRY_DESCRIPTION.name -> TextFieldComponent(
            uiModel = model.mapToUiModel(),
            isTablet = isTablet,
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
            isTablet = isTablet,
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
