package com.valkiria.uicomponents.model.ui.body

data class FingerprintUiModel(
    val identifier: String = "FINGERPRINT"
) : BodyRowModel {

    override val type: BodyRowType = BodyRowType.FINGERPRINT
}
