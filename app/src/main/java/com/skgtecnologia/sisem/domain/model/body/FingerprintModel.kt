package com.skgtecnologia.sisem.domain.model.body

data class FingerprintModel(
    val identifier: String = "FINGERPRINT"
) : BodyRowModel {

    override val type: BodyRowType = BodyRowType.FINGERPRINT
}
