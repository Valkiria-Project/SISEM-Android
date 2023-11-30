package com.valkiria.uicomponents.action

import com.valkiria.uicomponents.bricks.banner.finding.FindingsDetailUiModel
import com.valkiria.uicomponents.bricks.banner.report.ReportsDetailUiModel
import com.valkiria.uicomponents.bricks.chip.ChipSectionUiModel
import com.valkiria.uicomponents.components.chip.ChipOptionUiModel
import com.valkiria.uicomponents.components.chip.ChipSelectionItemUiModel
import com.valkiria.uicomponents.components.humanbody.HumanBodyUi
import com.valkiria.uicomponents.components.media.MediaAction

const val DISMISS_IDENTIFIER = "dismiss"

sealed interface UiAction

sealed class GenericUiAction(open val identifier: String) : UiAction {
    data class ButtonAction(override val identifier: String) : GenericUiAction(identifier)

    data class ChipOptionAction(
        override val identifier: String,
        val chipOptionUiModel: ChipOptionUiModel
    ) : GenericUiAction(identifier)

    data class ChipSelectionAction(
        override val identifier: String,
        val chipSelectionItemUiModel: ChipSelectionItemUiModel,
        val status: Boolean,
        val viewsVisibility: Map<String, Boolean>
    ) : GenericUiAction(identifier)

    data object DismissAction : GenericUiAction(identifier = DISMISS_IDENTIFIER)

    data class DropDownAction(
        override val identifier: String,
        val id: String,
        val name: String,
        val quantity: Int,
        val fieldValidated: Boolean
    ) : GenericUiAction(identifier)

    data class FindingAction(
        override val identifier: String,
        val status: Boolean,
        val findingDetail: FindingsDetailUiModel?
    ) : GenericUiAction(identifier)

    data class HumanBodyAction(
        override val identifier: String,
        val values: HumanBodyUi
    ) : GenericUiAction(identifier)

    data class ImageButtonAction(
        override val identifier: String,
        val itemIdentifier: String
    ) : GenericUiAction(identifier)

    data class InfoCardAction(
        override val identifier: String,
        val isPill: Boolean,
        val patient: String?,
        val isClickCard: Boolean,
        val reportDetail: ReportsDetailUiModel?,
        val chipSection: ChipSectionUiModel?
    ) : GenericUiAction(identifier)

    data class InputAction(
        override val identifier: String,
        val updatedValue: String,
        val fieldValidated: Boolean,
        val required: Boolean
    ) : GenericUiAction(identifier)

    data class InventoryAction(
        override val identifier: String,
        val itemIdentifier: String,
        val updatedValue: String,
        val fieldValidated: Boolean
    ) : GenericUiAction(identifier)

    data class MediaItemAction(
        override val identifier: String,
        val mediaAction: MediaAction
    ) : GenericUiAction(identifier)

    data class MedsSelectorAction(
        override val identifier: String
    ) : GenericUiAction(identifier)

    data class NotificationAction(
        override val identifier: String,
        val isDismiss: Boolean
    ) : GenericUiAction(identifier)

    data class SegmentedSwitchAction(
        override val identifier: String,
        val status: Boolean,
        val viewsVisibility: Map<String, Boolean>
    ) : GenericUiAction(identifier)

    data class SignatureAction(
        override val identifier: String
    ) : GenericUiAction(identifier)

    data class SimpleCardAction(
        override val identifier: String
    ) : GenericUiAction(identifier)

    data class SliderAction(
        override val identifier: String,
        val value: Int
    ) : GenericUiAction(identifier)

    data class StepperAction(
        override val identifier: String
    ) : GenericUiAction(identifier)

    data class TimePickerAction(
        override val identifier: String,
        val value: String
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
