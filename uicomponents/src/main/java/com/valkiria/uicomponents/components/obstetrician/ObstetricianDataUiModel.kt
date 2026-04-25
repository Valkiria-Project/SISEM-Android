package com.valkiria.uicomponents.components.obstetrician

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.Modifier
import com.valkiria.uicomponents.components.BodyRowModel
import com.valkiria.uicomponents.components.BodyRowType
import com.valkiria.uicomponents.components.label.TextUiModel

data class ObstetricianDataUiModel(
    override val identifier: String,
    val data: List<ObsDataUiModel>,
    val fur: TextUiModel,
    val arrangement: Arrangement.Horizontal,
    val modifier: Modifier
) : BodyRowModel {

    override val type: BodyRowType = BodyRowType.OBSTETRICIAN_DATA
}
