package com.valkiria.uicomponents.action

import com.valkiria.uicomponents.action.UiActionType.TERMS_AND_CONDITIONS

class TermsAndConditionsUiAction(
    val onClick: (link: String) -> Unit
) : UiAction {

    override val type: UiActionType = TERMS_AND_CONDITIONS
}
