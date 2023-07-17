package com.valkiria.uicomponents.action

import com.valkiria.uicomponents.action.UiActionType.TERMS_AND_CONDITIONS

class TermsAndConditionsUiAction(
    val link: String
) : UiAction {

    override val type: UiActionType = TERMS_AND_CONDITIONS
}
