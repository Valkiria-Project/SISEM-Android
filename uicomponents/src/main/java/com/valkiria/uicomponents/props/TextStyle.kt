package com.valkiria.uicomponents.props

import android.os.Parcelable
import androidx.compose.ui.text.TextStyle
import kotlinx.parcelize.Parcelize

@Parcelize
enum class TextStyle : Parcelable {
    BODY_1,
    BUTTON_1,
    BUTTON_2,
    HEADLINE_1,
    HEADLINE_2,
    HEADLINE_3,
    HEADLINE_4,
    HEADLINE_5,
    HEADLINE_6,
    HEADLINE_7,
    HEADLINE_8;
}

fun aja() {
    TextStyle.Default.copy(

    )
}
