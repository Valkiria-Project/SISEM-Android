package com.valkiria.uicomponents.components.signature

import androidx.compose.ui.Modifier
import com.valkiria.uicomponents.components.BodyRowModel
import com.valkiria.uicomponents.components.BodyRowType
import com.valkiria.uicomponents.components.button.ButtonUiModel
import com.valkiria.uicomponents.components.label.TextUiModel

data class SignatureUiModel(
    override val identifier: String,
    val signatureLabel: TextUiModel,
    val signatureButton: ButtonUiModel,
    val signature: String? = null,
    val visibility: Boolean = true,
    val required: Boolean = false,
    val modifier: Modifier
) : BodyRowModel {

    override val type: BodyRowType = BodyRowType.SIGNATURE
}
