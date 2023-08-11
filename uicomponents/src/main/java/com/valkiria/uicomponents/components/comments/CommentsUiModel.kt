package com.valkiria.uicomponents.components.comments

import androidx.compose.ui.Modifier
import com.valkiria.uicomponents.props.TextStyle

data class CommentsUiModel(
    val thread: List<String>,
    val replyIcon: String,
    val textStyle: TextStyle,
    val modifier: Modifier = Modifier
)
