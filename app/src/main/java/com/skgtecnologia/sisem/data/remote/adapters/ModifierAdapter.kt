package com.skgtecnologia.sisem.data.remote.adapters

import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.skgtecnologia.sisem.data.remote.model.props.MarginsResponse
import com.squareup.moshi.FromJson
import com.squareup.moshi.ToJson

class ModifierAdapter {

    @FromJson
    fun fromJson(margins: MarginsResponse): Modifier = Modifier.padding(
        start = margins.left?.dp ?: 0.dp,
        top = margins.top?.dp ?: 0.dp,
        end = margins.right?.dp ?: 0.dp,
        bottom = margins.bottom?.dp ?: 0.dp
    )

    @Suppress("UNUSED_PARAMETER")
    @ToJson
    fun toJson(modifier: Modifier): MarginsResponse = MarginsResponse(
        top = 0,
        bottom = 0,
        left = 0,
        right = 0
    )
}
