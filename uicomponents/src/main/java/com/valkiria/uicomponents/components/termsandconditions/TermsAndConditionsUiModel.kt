package com.valkiria.uicomponents.components.termsandconditions

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.Modifier

data class TermsAndConditionsUiModel(
    val termsAndConditionsLink: String,
    val privacyPolicyLink: String,
    val arrangement: Arrangement.Horizontal,
    val modifier: Modifier = Modifier
)
