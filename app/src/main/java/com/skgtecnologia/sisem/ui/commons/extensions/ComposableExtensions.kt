package com.skgtecnologia.sisem.ui.commons.extensions

import com.skgtecnologia.sisem.commons.communication.UnauthorizedEventHandler
import com.skgtecnologia.sisem.domain.login.model.LoginIdentifier
import com.valkiria.uicomponents.action.FooterUiAction
import com.valkiria.uicomponents.action.UiAction

fun UiAction.handleEvent() {
    if ((this is FooterUiAction) &&
        (this.identifier == LoginIdentifier.LOGIN_RE_AUTH_BANNER.name)
    ) {
        UnauthorizedEventHandler.publishUnauthorizedEvent()
    }
}
