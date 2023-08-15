package com.valkiria.uicomponents.action

import com.valkiria.uicomponents.components.crewmembercard.ChipSectionUiModel
import com.valkiria.uicomponents.components.crewmembercard.ReportsDetailUiModel

sealed interface UiAction

sealed class FooterUiAction(open val identifier: String) : UiAction {
    data class Button(override val identifier: String) : FooterUiAction(identifier)
}

sealed class AuthCardsUiAction : UiAction {
    object AuthCard : AuthCardsUiAction()
    data class AuthCardNews(val reportDetail: ReportsDetailUiModel) : AuthCardsUiAction()
    data class AuthCardFindings(val chipSectionUiModel: ChipSectionUiModel) : AuthCardsUiAction()
}

sealed class LoginUiAction : UiAction {
    object ForgotPassword : LoginUiAction()
    object Login : LoginUiAction()

    data class LoginPasswordInput(
        val updatedValue: String,
        val fieldValidated: Boolean
    ) : LoginUiAction()

    data class LoginUserInput(
        val updatedValue: String,
        val fieldValidated: Boolean
    ) : LoginUiAction()

    data class TermsAndConditions(val link: String) : LoginUiAction()
}

sealed class DeviceAuthUiAction : UiAction {
    object DeviceAuth : DeviceAuthUiAction()
    data class DeviceAuthCodeInput(val updatedValue: String) : DeviceAuthUiAction()
    data class DeviceAuthSwitchState(val state: Boolean) : DeviceAuthUiAction()
}

sealed class PreOperationalUiAction : UiAction {
    data class DriverVehicleKMInput(
        val updatedValue: String,
        val fieldValidated: Boolean
    ) : PreOperationalUiAction()

    data class PreOpSwitchState(val id: String, val status: Boolean) : PreOperationalUiAction()
    object SavePreOperational : PreOperationalUiAction()
}

sealed class ChangePasswordUiAction : UiAction {
    data class OldPasswordInput(
        val updatedValue: String
    ) : ChangePasswordUiAction()

    data class NewPasswordInput(
        val updatedValue: String,
        val fieldValidated: Boolean
    ) : ChangePasswordUiAction()

    data class ConfirmPasswordInput(
        val updatedValue: String,
        val fieldValidated: Boolean
    ) : ChangePasswordUiAction()
}
