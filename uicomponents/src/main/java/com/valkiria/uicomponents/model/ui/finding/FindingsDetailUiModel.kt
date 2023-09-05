package com.valkiria.uicomponents.model.ui.finding

import androidx.compose.ui.Modifier
import com.valkiria.uicomponents.model.props.TextStyle
import com.valkiria.uicomponents.model.ui.finding.FindingDetailUiModel

data class FindingsDetailUiModel(
    val icon: String? = null,
    val title: String,
    val titleTextStyle: TextStyle,
    val subtitle: String? = null,
    val subtitleTextStyle: TextStyle? = null,
    val modifier: Modifier = Modifier,
    val details: List<FindingDetailUiModel>
)
