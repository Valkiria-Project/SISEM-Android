package com.valkiria.uicomponents.components.detailedinfolist

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.Modifier
import com.valkiria.uicomponents.components.BodyRowModel
import com.valkiria.uicomponents.components.BodyRowType
import com.valkiria.uicomponents.components.label.TextStyle

data class DetailedInfoListUiModel(
    override val identifier: String,
    val details: List<DetailedInfoUiModel>,
    val labelTextStyle: TextStyle,
    val textTextStyle: TextStyle,
    val visibility: Boolean = true,
    val required: Boolean = false,
    val arrangement: Arrangement.Horizontal,
    val modifier: Modifier = Modifier
) : BodyRowModel {

    override val type: BodyRowType = BodyRowType.DETAILED_INFO_LIST
}
