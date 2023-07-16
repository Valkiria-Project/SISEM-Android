package com.valkiria.uicomponents.mocks

import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.valkiria.uicomponents.components.termsandconditions.TermsAndConditionsUiModel

fun getLoginTermsAndConditionsUiModel(): TermsAndConditionsUiModel {
    /*
    {
        "type": "TERMS_AND_CONDITIONS",
        "margins": {
            "top": 35,
            "left": 20,
            "right": 20,
            "bottom": 0
        }
    }
    */
    return TermsAndConditionsUiModel(
        termsAndConditionsLink = "TERMS_AND_CONDITIONS",
        privacyPolicyLink = "PRIVACY_POLICY",
        modifier = Modifier.padding(
            start = 0.dp,
            top = 20.dp,
            end = 0.dp,
            bottom = 0.dp
        )
    )
}
