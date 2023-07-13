package com.skgtecnologia.sisem.domain.model.props

import android.os.Parcelable
import com.valkiria.uicomponents.props.textfield.ValidationUiModel
import kotlinx.parcelize.Parcelize

@Parcelize
data class ValidationModel(
    val regex: String?,
    val message: String?
) : Parcelable

// FIXME
fun ValidationModel.mapToUiModel(): ValidationUiModel {
    return ValidationUiModel(
        regex = regex,
        message = message
    )
}
