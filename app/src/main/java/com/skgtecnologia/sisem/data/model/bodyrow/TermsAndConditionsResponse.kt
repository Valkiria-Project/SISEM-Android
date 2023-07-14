package com.skgtecnologia.sisem.data.model.bodyrow

import com.skgtecnologia.sisem.data.model.props.MarginsResponse
import com.skgtecnologia.sisem.data.model.props.mapToUi
import com.skgtecnologia.sisem.domain.model.bodyrow.BodyRowModel
import com.skgtecnologia.sisem.domain.model.bodyrow.BodyRowType
import com.skgtecnologia.sisem.domain.model.bodyrow.TermsAndConditionsModel
import com.squareup.moshi.Json

data class TermsAndConditionsResponse(
    @Json(name = "identifier") val identifier: String?,
    @Json(name = "margins") val margins: MarginsResponse?
) : BodyRowResponse {

    override val type: BodyRowType = BodyRowType.TERMS_AND_CONDITIONS

    override fun mapToDomain(): BodyRowModel = TermsAndConditionsModel(
        identifier = identifier,
        margins = margins?.mapToUi()
    )
}
