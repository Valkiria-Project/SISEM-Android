package com.skgtecnologia.sisem.data.remote.model.body

import com.skgtecnologia.sisem.domain.model.body.BodyRowModel
import com.skgtecnologia.sisem.domain.model.body.BodyRowType
import com.skgtecnologia.sisem.domain.model.body.FingerprintModel
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class FingerprintResponse(
    @Json(name = "identifier") val identifier: String?
) : BodyRowResponse {

    override val type: BodyRowType = BodyRowType.FINGERPRINT

    override fun mapToDomain(): FingerprintModel = FingerprintModel(
        identifier = identifier ?: error("Fingerprint identifier cannot be null")
    )
}
