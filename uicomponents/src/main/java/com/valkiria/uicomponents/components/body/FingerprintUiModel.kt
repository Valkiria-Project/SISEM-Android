package com.valkiria.uicomponents.components.body

import com.valkiria.uicomponents.components.BodyRowType

data class FingerprintUiModel(
    val identifier: String = "FINGERPRINT"
) : com.valkiria.uicomponents.components.body.BodyRowModel {

    override val type: BodyRowType = BodyRowType.FINGERPRINT
}
