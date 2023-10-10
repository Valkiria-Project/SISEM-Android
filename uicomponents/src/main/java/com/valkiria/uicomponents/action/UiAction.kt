package com.valkiria.uicomponents.action

import com.valkiria.uicomponents.bricks.chip.ChipSectionUiModel
import com.valkiria.uicomponents.bricks.banner.report.ReportsDetailUiModel

const val DISMISS_IDENTIFIER = "dismiss"

sealed interface UiAction

sealed class GenericUiAction(open val identifier: String) : UiAction {
    data class ButtonAction(override val identifier: String) : GenericUiAction(identifier)

    data class ChipOptionAction(
        override val identifier: String,
        val text: String,
        val status: Boolean
    ) : GenericUiAction(identifier)

    data class ChipSelectionAction(
        override val identifier: String,
        val text: String,
        val status: Boolean
    ) : GenericUiAction(identifier)

    data object DismissAction : GenericUiAction(identifier = DISMISS_IDENTIFIER)

    data class FindingAction(
        override val identifier: String,
        val status: Boolean
    ) : GenericUiAction(identifier)

    data class HumanBodyAction(
        override val identifier: String,
        val values: Map<String, List<String>>
    ) : GenericUiAction(identifier)

    data class InfoCardAction(
        override val identifier: String
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

    data class SliderAction(
        override val identifier: String,
        val value: Int
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
    data class AuthCardNews(val reportUiDetail: ReportsDetailUiModel) : AuthCardsUiAction()
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
    data object NewsStepOneOnChipClick : NewsUiAction()
}

sealed class AddReportUiAction : UiAction {
    data class TopicInput(
        val updatedValue: String,
        val fieldValidated: Boolean
    ) : AddReportUiAction()

    data class DescriptionInput(
        val updatedValue: String,
        val fieldValidated: Boolean
    ) : AddReportUiAction()
}
