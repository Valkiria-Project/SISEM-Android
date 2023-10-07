package com.skgtecnologia.sisem.data.remote.model.body

import com.valkiria.uicomponents.model.ui.body.FingerprintUiModel
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import com.valkiria.uicomponents.model.ui.body.BodyRowType

@JsonClass(generateAdapter = true)
data class FingerprintResponse(
    @Json(name = "identifier") val identifier: String?
) : BodyRowResponse {

    override val type: BodyRowType = BodyRowType.FINGERPRINT

    override fun mapToUi(): FingerprintUiModel = FingerprintUiModel(
        identifier = identifier ?: error("Fingerprint identifier cannot be null")
    )
}
