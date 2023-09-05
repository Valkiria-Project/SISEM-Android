package com.valkiria.uicomponents.model.ui.detailedinfolist

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.Modifier
import com.valkiria.uicomponents.model.props.TextStyle

data class DetailedInfoListUiModel(
    val details: List<DetailedInfoUiModel>,
    val labelTextStyle: TextStyle,
    val textTextStyle: TextStyle,
    val arrangement: Arrangement.Horizontal,
    val modifier: Modifier = Modifier
)