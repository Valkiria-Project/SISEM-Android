package com.skgtecnologia.sisem.domain.model.header

import android.os.Parcelable
import androidx.compose.ui.Modifier
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue

@Parcelize
data class HeaderModel(
    val title: Title?,
    val subtitle: Subtitle?,
    val leftIcon: String?,
    val modifier: @RawValue Modifier
) : Parcelable
