package com.skgtecnologia.sisem.data.remote.model.components.signature

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.Modifier
import com.skgtecnologia.sisem.data.remote.model.components.BodyRowResponse
import com.skgtecnologia.sisem.data.remote.model.components.label.TextResponse
import com.skgtecnologia.sisem.data.remote.model.components.label.mapToUi
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import com.valkiria.uicomponents.components.BodyRowType
import com.valkiria.uicomponents.components.signature.CrewMemberSignatureUiModel

@JsonClass(generateAdapter = true)
data class CrewMemberSignatureResponse(
    @Json(name = "identifier") val identifier: String?,
    @Json(name = "name") val name: TextResponse?,
    @Json(name = "identification") val identification: TextResponse?,
    @Json(name = "signature") val signature: String?,
    @Json(name = "arrangement") val arrangement: Arrangement.Horizontal?,
    @Json(name = "margins") val modifier: Modifier?
) : BodyRowResponse {

    override val type: BodyRowType = BodyRowType.CREW_MEMBER_SIGNATURE

    override fun mapToUi(): CrewMemberSignatureUiModel = CrewMemberSignatureUiModel(
        identifier = identifier ?: error("Signature identifier cannot be null"),
        name = name?.mapToUi() ?: error("Signature name cannot be null"),
        identification = identification?.mapToUi()
            ?: error("Signature identification cannot be null"),
        signature = signature ?: error("Signature signature cannot be null"),
        modifier = modifier ?: Modifier
    )
}
