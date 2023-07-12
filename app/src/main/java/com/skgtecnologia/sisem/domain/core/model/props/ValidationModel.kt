package com.skgtecnologia.sisem.domain.core.model.props

import android.os.Parcelable
import com.valkiria.uicomponents.components.textfield.ValidationUiModel
import kotlinx.parcelize.Parcelize

@Parcelize
data class ValidationModel(
    val regex: String?,
    val message: String?
) : Parcelable

fun ValidationModel.mapToUiModel(): ValidationUiModel {
    return ValidationUiModel(
        regex = regex,
        message = message
    )
}
