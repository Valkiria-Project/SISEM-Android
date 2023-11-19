package com.valkiria.uicomponents.components.timepicker

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.Modifier
import com.valkiria.uicomponents.components.BodyRowModel
import com.valkiria.uicomponents.components.BodyRowType
import com.valkiria.uicomponents.components.label.TextUiModel

data class TimePickerUiModel(
    override val identifier: String,
    val title: TextUiModel,
    val hour: TextUiModel,
    val minute: TextUiModel,
    val visibility: Boolean = true,
    val required: Boolean = false,
    val arrangement: Arrangement.Horizontal,
    val modifier: Modifier
) : BodyRowModel {

    override val type: BodyRowType = BodyRowType.TIME_PICKER
}
