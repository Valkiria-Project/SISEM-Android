package com.skgtecnologia.sisem.domain.model.bodyrow

import com.skgtecnologia.sisem.domain.model.props.MarginsModel
import com.skgtecnologia.sisem.domain.model.props.mapToUiModel
import com.skgtecnologia.sisem.domain.model.bodyrow.BodyRowType.TERMS_AND_CONDITIONS
import com.valkiria.uicomponents.components.termsandconditions.TermsAndConditionsUiModel
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize

@Parcelize
data class TermsAndConditionsModel(
    val identifier: String?,
    val margins: MarginsModel?
) : BodyRowModel {

    @IgnoredOnParcel
    override val type: BodyRowType = TERMS_AND_CONDITIONS
}

fun TermsAndConditionsModel.mapToUiModel(): TermsAndConditionsUiModel {
    return TermsAndConditionsUiModel(
        margins = margins?.mapToUiModel()
    )
}
