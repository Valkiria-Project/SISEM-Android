package com.valkiria.uicomponents.action

import com.valkiria.uicomponents.model.ui.chip.ChipSectionUiModel
import com.valkiria.uicomponents.model.ui.report.ReportsDetailUiModel

const val DISMISS_IDENTIFIER = "dismiss"

sealed interface UiAction

sealed class GenericUiAction(open val identifier: String) : UiAction {
    data class ButtonAction(override val identifier: String) : GenericUiAction(identifier)

    data object DismissAction : GenericUiAction(identifier = DISMISS_IDENTIFIER)

    data class ChipOptionAction(
        override val identifier: String,
        val text: String,
        val status: Boolean
    ) : GenericUiAction(identifier)

    data class FindingAction(
        override val identifier: String,
        val status: Boolean
    ) : GenericUiAction(identifier)

    data class InputAction(
        override val identifier: String,
        val updatedValue: String,
        val fieldValidated: Boolean
    ) : GenericUiAction(identifier)

    data class InventoryAction(
        override val identifier: String,
        val updatedValue: String,
        val fieldValidated: Boolean
    ) : GenericUiAction(identifier)

    data class SegmentedSwitchAction(
        override val identifier: String,
        val status: Boolean
    ) : GenericUiAction(identifier)
}

sealed class HeaderUiAction : UiAction {
    data object GoBack : HeaderUiAction()
}

sealed class FooterUiAction(open val identifier: String) : UiAction {
    data class FooterButton(override val identifier: String) : FooterUiAction(identifier)
}

sealed class AuthCardsUiAction : UiAction {
    data object AuthCard : AuthCardsUiAction()
    data class AuthCardNews(val reportDetail: ReportsDetailUiModel) : AuthCardsUiAction()
    data class AuthCardFindings(val chipSectionUiModel: ChipSectionUiModel) : AuthCardsUiAction()
}

sealed class LoginUiAction : UiAction {
    data object ForgotPassword : LoginUiAction()
    data object Login : LoginUiAction()

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
    data object DeviceAuth : DeviceAuthUiAction()
    data class DeviceAuthCodeInput(
        val updatedValue: String,
        val fieldValidated: Boolean
    ) : DeviceAuthUiAction()
}

sealed class ChangePasswordUiAction : UiAction {
    data class ConfirmPasswordInput(
        val updatedValue: String,
        val fieldValidated: Boolean
    ) : ChangePasswordUiAction()

    data class NewPasswordInput(
        val updatedValue: String,
        val fieldValidated: Boolean
    ) : ChangePasswordUiAction()

    data class OldPasswordInput(
        val updatedValue: String
    ) : ChangePasswordUiAction()
}

sealed class NewsUiAction : UiAction {
    data class NewsStepOneOnChipClick(val text: String) : NewsUiAction()
}

sealed class RecordNewsUiAction : UiAction {
    data class TopicInput(
        val updatedValue: String,
        val fieldValidated: Boolean
    ) : RecordNewsUiAction()

    data class DescriptionInput(
        val updatedValue: String,
        val fieldValidated: Boolean
    ) : RecordNewsUiAction()
}
