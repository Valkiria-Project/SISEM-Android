package com.skgtecnologia.sisem.domain.model.bodyrow

import com.skgtecnologia.sisem.domain.model.bodyrow.BodyRowType.TERMS_AND_CONDITIONS
import com.valkiria.uicomponents.components.termsandconditions.TermsAndConditionsUiModel
import com.valkiria.uicomponents.props.MarginsUiModel

data class TermsAndConditionsModel(
    val identifier: String?,
    val margins: MarginsUiModel?
) : BodyRowModel {

    override val type: BodyRowType = TERMS_AND_CONDITIONS
}

fun TermsAndConditionsModel.mapToUiModel(): TermsAndConditionsUiModel {
    return TermsAndConditionsUiModel(
        margins = margins
    )
}
