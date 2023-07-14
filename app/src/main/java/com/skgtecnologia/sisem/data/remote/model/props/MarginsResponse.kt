package com.skgtecnologia.sisem.data.remote.model.props

import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

data class MarginsResponse(
    val top: Int?,
    val bottom: Int?,
    val left: Int?,
    val right: Int?
)

fun MarginsResponse.mapToUi(): Modifier = Modifier.padding(
    top = top?.dp ?: 0.dp,
    bottom = bottom?.dp ?: 0.dp,
    start = left?.dp ?: 0.dp,
    end = right?.dp ?: 0.dp
)
