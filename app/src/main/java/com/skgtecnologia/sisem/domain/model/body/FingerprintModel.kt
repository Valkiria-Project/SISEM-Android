package com.skgtecnologia.sisem.domain.model.body

import com.valkiria.uicomponents.model.ui.body.BodyRowType

data class FingerprintModel(
    val identifier: String = "FINGERPRINT"
) : BodyRowModel {

    override val type: BodyRowType = BodyRowType.FINGERPRINT
}
