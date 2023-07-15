package com.skgtecnologia.sisem.domain.model.body

import androidx.compose.ui.Modifier
import com.skgtecnologia.sisem.domain.model.body.BodyRowType.TERMS_AND_CONDITIONS
import com.valkiria.uicomponents.components.termsandconditions.TermsAndConditionsUiModel

data class TermsAndConditionsModel(
    val identifier: String?,
    val modifier: Modifier = Modifier
) : BodyRowModel {

    override val type: BodyRowType = TERMS_AND_CONDITIONS
}

fun TermsAndConditionsModel.mapToUiModel(): TermsAndConditionsUiModel {
    return TermsAndConditionsUiModel(
        modifier = modifier
    )
}
