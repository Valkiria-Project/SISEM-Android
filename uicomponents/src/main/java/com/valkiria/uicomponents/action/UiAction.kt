package com.valkiria.uicomponents.action

sealed interface UiAction

sealed class LoginUiAction : UiAction {
    data class LoginTermsAndConditions(val link: String) : LoginUiAction()
}
