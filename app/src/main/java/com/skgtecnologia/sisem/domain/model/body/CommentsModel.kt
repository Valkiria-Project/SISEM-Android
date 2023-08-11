package com.skgtecnologia.sisem.domain.model.body

import androidx.compose.ui.Modifier
import com.valkiria.uicomponents.components.comments.CommentsUiModel
import com.valkiria.uicomponents.props.TextStyle

data class CommentsModel(
    val identifier: String,
    val thread: List<String>,
    val replyIcon: String,
    val textStyle: TextStyle,
    val modifier: Modifier = Modifier
) : BodyRowModel {

    override val type: BodyRowType = BodyRowType.COMMENTS
}

fun CommentsModel.mapToUiModel() = CommentsUiModel(
    thread = thread,
    replyIcon = replyIcon,
    textStyle = textStyle,
    modifier = modifier
)
