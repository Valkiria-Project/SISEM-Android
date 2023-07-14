package com.valkiria.uicomponents.components.textfield

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ValidationUiModel(
    val regex: String?,
    val message: String?
) : Parcelable
