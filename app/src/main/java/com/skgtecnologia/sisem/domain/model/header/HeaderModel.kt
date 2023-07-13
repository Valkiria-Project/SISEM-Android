package com.skgtecnologia.sisem.domain.model.header

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class HeaderModel(
    val title: String,
    val iconUrl: String?,
//    val closeAction: Action
) : Parcelable
