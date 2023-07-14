package com.skgtecnologia.sisem.domain.model.bodyrow

import androidx.compose.ui.Modifier
import com.skgtecnologia.sisem.domain.model.bodyrow.BodyRowType.TERMS_AND_CONDITIONS
import com.valkiria.uicomponents.components.termsandconditions.TermsAndConditionsUiModel

data class TermsAndConditionsModel(
    val identifier: String?,
    val margins: Modifier = Modifier
) : BodyRowModel {

    override val type: BodyRowType = TERMS_AND_CONDITIONS
}

fun TermsAndConditionsModel.mapToUiModel(): TermsAndConditionsUiModel {
    return TermsAndConditionsUiModel(
        margins = margins
    )
}
