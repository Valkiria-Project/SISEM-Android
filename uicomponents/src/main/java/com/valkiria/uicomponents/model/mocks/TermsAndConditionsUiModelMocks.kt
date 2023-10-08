package com.valkiria.uicomponents.model.mocks

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.valkiria.uicomponents.components.termsandconditions.TermsAndConditionsUiModel
import com.valkiria.uicomponents.model.ui.link.Link

fun getLoginTermsAndConditionsUiModel(): TermsAndConditionsUiModel {
    return TermsAndConditionsUiModel(
        identifier = "LOGIN_TERMS_CONDITIONS",
        links = listOf(
            LoginLinkMock.PRIVACY_POLICY,
            LoginLinkMock.TERMS_AND_CONDITIONS
        ),
        arrangement = Arrangement.Center,
        modifier = Modifier.padding(
            start = 0.dp,
            top = 20.dp,
            end = 0.dp,
            bottom = 0.dp
        )
    )
}

private enum class LoginLinkMock : Link {
    PRIVACY_POLICY,
    TERMS_AND_CONDITIONS
}
