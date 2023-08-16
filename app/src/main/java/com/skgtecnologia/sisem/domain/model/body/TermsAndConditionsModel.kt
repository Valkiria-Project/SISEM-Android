package com.skgtecnologia.sisem.domain.model.body

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.Modifier
import com.skgtecnologia.sisem.domain.login.model.LoginLink
import com.valkiria.uicomponents.components.termsandconditions.TermsAndConditionsUiModel

data class TermsAndConditionsModel(
    val identifier: String?,
    val arrangement: Arrangement.Horizontal,
    val modifier: Modifier = Modifier
) : BodyRowModel {

    override val type: BodyRowType = BodyRowType.TERMS_AND_CONDITIONS
}

fun TermsAndConditionsModel.mapToUiModel(): TermsAndConditionsUiModel {
    return TermsAndConditionsUiModel(
        termsAndConditionsLink = LoginLink.TERMS_AND_CONDITIONS.name,
        privacyPolicyLink = LoginLink.PRIVACY_POLICY.name,
        arrangement = arrangement,
        modifier = modifier
    )
}
