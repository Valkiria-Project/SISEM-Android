package com.valkiria.uicomponents.components.fingerprint

import com.valkiria.uicomponents.components.BodyRowModel
import com.valkiria.uicomponents.components.BodyRowType

data class FingerprintUiModel(
    val identifier: String = "FINGERPRINT"
) : BodyRowModel {

    override val type: BodyRowType = BodyRowType.FINGERPRINT
}
