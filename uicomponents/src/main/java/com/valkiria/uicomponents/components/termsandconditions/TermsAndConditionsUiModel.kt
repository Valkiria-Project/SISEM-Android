package com.valkiria.uicomponents.components.termsandconditions

import androidx.compose.ui.Modifier

data class TermsAndConditionsUiModel(
    val termsAndConditionsLink: String,
    val privacyPolicyLink: String,
    val modifier: Modifier = Modifier
)
