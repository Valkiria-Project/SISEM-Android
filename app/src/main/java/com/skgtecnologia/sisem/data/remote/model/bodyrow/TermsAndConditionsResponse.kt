package com.skgtecnologia.sisem.data.remote.model.bodyrow

import androidx.compose.ui.Modifier
import com.skgtecnologia.sisem.data.remote.model.props.MarginsResponse
import com.skgtecnologia.sisem.data.remote.model.props.mapToUi
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
        margins = margins?.mapToUi() ?: Modifier
    )
}
