package com.skgtecnologia.sisem.ui.sections

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.skgtecnologia.sisem.domain.login.model.LoginIdentifier
import com.skgtecnologia.sisem.domain.model.body.BodyRowModel
import com.skgtecnologia.sisem.domain.model.body.ButtonModel
import com.skgtecnologia.sisem.domain.model.body.ChipModel
import com.skgtecnologia.sisem.domain.model.body.LabelModel
import com.skgtecnologia.sisem.domain.model.body.LabeledSwitchModel
import com.skgtecnologia.sisem.domain.model.body.PasswordTextFieldModel
import com.skgtecnologia.sisem.domain.model.body.RichLabelModel
import com.skgtecnologia.sisem.domain.model.body.TermsAndConditionsModel
import com.skgtecnologia.sisem.domain.model.body.TextFieldModel
import com.skgtecnologia.sisem.domain.model.body.mapToUiModel
import com.valkiria.uicomponents.action.LoginUiAction.ForgotPassword
import com.valkiria.uicomponents.action.LoginUiAction.Login
import com.valkiria.uicomponents.action.LoginUiAction.LoginPasswordInput
import com.valkiria.uicomponents.action.LoginUiAction.LoginUserInput
import com.valkiria.uicomponents.action.LoginUiAction.TermsAndConditions
import com.valkiria.uicomponents.action.UiAction
import com.valkiria.uicomponents.components.button.ButtonComponent
import com.valkiria.uicomponents.components.chip.ChipComponent
import com.valkiria.uicomponents.components.label.LabelComponent
import com.valkiria.uicomponents.components.passwordtextfield.PasswordTextFieldComponent
import com.valkiria.uicomponents.components.richlabel.RichLabelComponent
import com.valkiria.uicomponents.components.termsandconditions.TermsAndConditionsComponent
import com.valkiria.uicomponents.components.textfield.TextFieldComponent

@Suppress("LongMethod")
@Composable
fun BodySection(
    body: List<BodyRowModel>?,
    isTablet: Boolean,
    modifier: Modifier = Modifier,
    onAction: (actionInput: UiAction) -> Unit
) {
    if (body?.isNotEmpty() == true) {
        LazyColumn(
            modifier = modifier,
            contentPadding = PaddingValues(horizontal = 20.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(0.dp, Alignment.Top),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            handleBodyRows(body, isTablet, onAction)
        }
    }
}

private fun LazyListScope.handleBodyRows(
    body: List<BodyRowModel>,
    isTablet: Boolean,
    onAction: (actionInput: UiAction) -> Unit
) {
    body.map { model ->
        when (model) {
            is ButtonModel -> item(key = model.identifier) {
                HandleButtonRows(model, isTablet, onAction)
            }

            is ChipModel -> item(key = model.identifier) {
                ChipComponent(
                    uiModel = model.mapToUiModel()
                )
            }

            is LabelModel -> item(key = model.identifier) {
                LabelComponent(uiModel = model.mapToUiModel())
            }

            is LabeledSwitchModel -> item(key = model.identifier) {

            }

            is PasswordTextFieldModel -> item(key = model.identifier) {
                HandlePasswordTextFieldRows(model, isTablet, onAction)
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
                HandleTextFieldRows(model, isTablet, onAction)
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
        LoginIdentifier.FORGOT_PASSWORD_BUTTON.name -> ButtonComponent(
            uiModel = model.mapToUiModel(),
            isTablet = isTablet,
            arrangement = Arrangement.Start
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

/*
        LOGIN_PASSWORD.name -> TextFieldComponent(
            uiModel = model.mapToUiModel(),
            isTablet = isTablet
        ) { updatedValue ->
            onAction(LoginPasswordInput(updatedValue = updatedValue))
        }
 */

@Composable
private fun HandlePasswordTextFieldRows(
    model: PasswordTextFieldModel,
    isTablet: Boolean,
    onAction: (actionInput: UiAction) -> Unit
) {
    when (model.identifier) {
        LoginIdentifier.LOGIN_PASSWORD.name -> PasswordTextFieldComponent(
            uiModel = model.mapToUiModel(),
            isTablet = isTablet
        ) { updatedValue ->
            onAction(LoginPasswordInput(updatedValue = updatedValue))
        }
    }
}

@Composable
private fun HandleTextFieldRows(
    model: TextFieldModel,
    isTablet: Boolean,
    onAction: (actionInput: UiAction) -> Unit
) {
    when (model.identifier) {
        LoginIdentifier.LOGIN_EMAIL.name -> TextFieldComponent(
            uiModel = model.mapToUiModel(),
            isTablet = isTablet
        ) { updatedValue ->
            onAction(LoginUserInput(updatedValue = updatedValue))
        }
    }
}
