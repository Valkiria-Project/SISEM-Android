package com.skgtecnologia.sisem.data.myscreen.remote.model

import com.skgtecnologia.sisem.domain.core.model.bodyrow.BodyRowModel
import com.skgtecnologia.sisem.domain.core.model.bodyrow.BodyRowType
import com.skgtecnologia.sisem.domain.core.model.bodyrow.TermsAndConditionsModel

data class TermsAndConditionsResponse(
    val margins: MarginsResponse?
) : BodyRowResponse {

    override val type: BodyRowType = BodyRowType.TERMS_AND_CONDITIONS

    override fun mapToDomain(): BodyRowModel = TermsAndConditionsModel(
        margins = margins?.mapToDomain()
    )
}
