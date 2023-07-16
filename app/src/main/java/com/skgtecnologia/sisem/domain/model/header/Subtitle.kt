package com.skgtecnologia.sisem.domain.model.header

import android.os.Parcelable
import com.valkiria.uicomponents.props.TextStyle
import kotlinx.parcelize.Parcelize

@Parcelize
data class Subtitle(
    val text: String,
    val textStyle: TextStyle
) : Parcelable
