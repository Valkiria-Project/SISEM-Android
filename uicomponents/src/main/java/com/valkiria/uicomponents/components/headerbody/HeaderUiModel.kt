package com.valkiria.uicomponents.components.headerbody

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.Modifier
import com.valkiria.uicomponents.components.BodyRowModel
import com.valkiria.uicomponents.components.BodyRowType
import com.valkiria.uicomponents.model.props.TextModel

data class HeaderUiModel(
    val identifier: String,
    val title: TextModel,
    val subtitle: TextModel? = null,
    val leftIcon: String? = null,
    val rightIcon: String? = null,
    val badgeCount: String? = null,
    val arrangement: Arrangement.Horizontal = Arrangement.Center,
    val modifier: Modifier
) : BodyRowModel {

    override val type: BodyRowType = BodyRowType.HEADER
}
