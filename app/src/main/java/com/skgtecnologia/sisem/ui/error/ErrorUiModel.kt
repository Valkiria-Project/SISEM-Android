package com.skgtecnologia.sisem.ui.error

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import com.valkiria.uicomponents.components.bottomsheet.BottomSheetUiModel
import com.valkiria.uicomponents.props.TextStyle

data class ErrorUiModel(
    val icon: Painter? = null,
    val title: String,
    val titleTextStyle: TextStyle,
    val text: String,
    val textStyle: TextStyle,
    val modifier: Modifier = Modifier
)

@Composable
fun ErrorUiModel.toBottomSheetUiModel() = BottomSheetUiModel(
    icon = icon,
    title = title,
    titleTextStyle = titleTextStyle,
    text = text,
    textStyle = textStyle
)
