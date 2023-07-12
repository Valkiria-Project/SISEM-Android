package com.skgtecnologia.sisem.domain.core.model.bodyrow

import com.skgtecnologia.sisem.domain.core.model.props.MarginsModel
import com.skgtecnologia.sisem.domain.core.model.props.mapToUiModel
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
