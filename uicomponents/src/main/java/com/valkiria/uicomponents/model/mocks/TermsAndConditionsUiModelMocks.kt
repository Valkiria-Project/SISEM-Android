package com.valkiria.uicomponents.model.mocks

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.valkiria.uicomponents.model.ui.termsandconditions.TermsAndConditionsUiModel

fun getLoginTermsAndConditionsUiModel(): TermsAndConditionsUiModel {
    return TermsAndConditionsUiModel(
        termsAndConditionsLink = "TERMS_AND_CONDITIONS",
        privacyPolicyLink = "PRIVACY_POLICY",
        arrangement = Arrangement.Center,
        modifier = Modifier.padding(
            start = 0.dp,
            top = 20.dp,
            end = 0.dp,
            bottom = 0.dp
        )
    )
}
