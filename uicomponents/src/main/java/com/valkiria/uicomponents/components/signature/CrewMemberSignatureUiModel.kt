package com.valkiria.uicomponents.components.signature

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.Modifier
import com.valkiria.uicomponents.components.BodyRowModel
import com.valkiria.uicomponents.components.BodyRowType
import com.valkiria.uicomponents.components.label.TextUiModel

data class CrewMemberSignatureUiModel(
    override val identifier: String,
    val name: TextUiModel,
    val identification: TextUiModel,
    val role: TextUiModel? = null,
    val signature: String,
    val visibility: Boolean = true,
    val required: Boolean = false,
    val arrangement: Arrangement.Horizontal,
    val modifier: Modifier
) : BodyRowModel {

    override val type: BodyRowType = BodyRowType.CREW_MEMBER_SIGNATURE
}
