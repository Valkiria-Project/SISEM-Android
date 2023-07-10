package com.skgtecnologia.sisem.domain.myscreen.model

import com.valkiria.uicomponents.components.termandconditions.TermsAndConditionsUiModel
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize

@Parcelize
data class TermsAndConditionsModel(
    val margins: MarginsModel?
) : BodyRowModel {

    @IgnoredOnParcel
    override val type: BodyRowType = BodyRowType.TERMS_AND_CONDITIONS
}

fun TermsAndConditionsModel.mapToUiModel(): TermsAndConditionsUiModel {
    return TermsAndConditionsUiModel(
        margins = margins?.mapToUiModel()
    )
}
