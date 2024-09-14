package com.skgtecnologia.sisem.core.data.remote.adapters

import androidx.compose.foundation.layout.Arrangement
import com.squareup.moshi.FromJson
import com.squareup.moshi.ToJson

private const val START = "START"
private const val END = "END"
private const val CENTER = "CENTER"

class ArrangementAdapter {

    @FromJson
    fun fromJson(arrangement: String): Arrangement.Horizontal = when (arrangement) {
        START -> Arrangement.Start
        END -> Arrangement.End
        CENTER -> Arrangement.Center
        else -> Arrangement.Center
    }

    @ToJson
    fun toJson(arrangement: Arrangement.Horizontal): String = when (arrangement) {
        Arrangement.Start -> START
        Arrangement.End -> END
        Arrangement.Center -> CENTER
        else -> CENTER
    }
}
