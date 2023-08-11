package com.valkiria.uicomponents.components.detailedinfolist

import androidx.compose.ui.Modifier
import com.valkiria.uicomponents.props.TextStyle

data class DetailedInfoListUiModel(
    val details: List<DetailedInfoUiModel>,
    val labelTextStyle: TextStyle,
    val textTextStyle: TextStyle,
    val modifier: Modifier = Modifier
)
