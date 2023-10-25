package com.valkiria.uicomponents.components.medsselector

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.Modifier
import com.valkiria.uicomponents.components.BodyRowModel
import com.valkiria.uicomponents.components.BodyRowType
import com.valkiria.uicomponents.components.button.ButtonUiModel
import com.valkiria.uicomponents.components.card.InfoCardUiModel

data class MedsSelectorUiModel(
    val identifier: String,
    val button: ButtonUiModel,
    val medicines: List<InfoCardUiModel>,
    val section: String?,
    val arrangement: Arrangement.Horizontal,
    val modifier: Modifier
) : BodyRowModel {
    override val type: BodyRowType = BodyRowType.INFO_CARD_BUTTON
}
