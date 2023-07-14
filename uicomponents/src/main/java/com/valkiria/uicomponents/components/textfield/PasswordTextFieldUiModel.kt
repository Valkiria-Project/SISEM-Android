package com.valkiria.uicomponents.components.textfield

import android.os.Parcelable
import com.valkiria.uicomponents.props.KeyBoardType
import com.valkiria.uicomponents.props.MarginsUiModel
import kotlinx.parcelize.Parcelize

@Parcelize
data class PasswordTextFieldUiModel(
    val identifier: String?,
    val icon: String?,
    val hint: String?,
    val keyboardType: KeyBoardType,
    val validations: List<ValidationUiModel>,
    val margins: MarginsUiModel?
) : Parcelable
