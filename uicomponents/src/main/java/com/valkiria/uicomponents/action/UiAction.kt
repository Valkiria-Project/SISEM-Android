package com.valkiria.uicomponents.action

import com.valkiria.uicomponents.components.crewmembercard.ReportsDetailUiModel

sealed interface UiAction

sealed class FooterUiAction(open val identifier: String) : UiAction {
    data class Button(override val identifier: String) : FooterUiAction(identifier)
}

sealed class LoginUiAction : UiAction {
    object ForgotPassword : LoginUiAction()
    object Login : LoginUiAction()
    data class TermsAndConditions(val link: String) : LoginUiAction()

    data class LoginPasswordInput(
        val updatedValue: String,
        val fieldValidated: Boolean
    ) : LoginUiAction()

    data class LoginUserInput(
        val updatedValue: String,
        val fieldValidated: Boolean
    ) : LoginUiAction()
}

sealed class DeviceAuthUiAction : UiAction {
    object DeviceAuth : DeviceAuthUiAction()
    data class DeviceAuthCodeInput(val updatedValue: String) : DeviceAuthUiAction()
}

sealed class AuthCardsUiAction : UiAction {
    object AuthCard : AuthCardsUiAction()
    data class AuthCardNews(val reportDetail: ReportsDetailUiModel) : AuthCardsUiAction()
}
