package com.skgtecnologia.sisem.data.remote.model.body

import androidx.compose.ui.Modifier
import com.skgtecnologia.sisem.domain.model.body.BodyRowModel
import com.skgtecnologia.sisem.domain.model.body.BodyRowType
import com.skgtecnologia.sisem.domain.model.body.TermsAndConditionsModel
import com.squareup.moshi.Json

data class TermsAndConditionsResponse(
    @Json(name = "identifier") val identifier: String?,
    @Json(name = "margins") val modifier: Modifier?
) : BodyRowResponse {

    override val type: BodyRowType = BodyRowType.TERMS_AND_CONDITIONS

    override fun mapToDomain(): BodyRowModel = TermsAndConditionsModel(
        identifier = identifier,
        modifier = modifier ?: Modifier
    )
}
