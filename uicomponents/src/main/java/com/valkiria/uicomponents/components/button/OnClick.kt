package com.valkiria.uicomponents.components.button

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
enum class OnClick : Parcelable {
    FORGOT_PASSWORD,
    LOGIN
}
