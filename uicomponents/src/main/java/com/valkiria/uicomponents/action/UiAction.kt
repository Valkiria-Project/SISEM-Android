package com.valkiria.uicomponents.action

sealed interface UiAction

sealed class LoginUiAction : UiAction {
    object ForgotPassword : LoginUiAction()
    object Login : LoginUiAction()
    data class TermsAndConditions(val link: String) : LoginUiAction()
}
