package com.skgtecnologia.sisem.domain.model.bodyrow

import androidx.compose.foundation.text.KeyboardOptions
import com.skgtecnologia.sisem.domain.model.bodyrow.BodyRowType.TEXT_FIELD
import com.valkiria.uicomponents.components.textfield.TextFieldUiModel
import com.valkiria.uicomponents.components.textfield.ValidationUiModel
import com.valkiria.uicomponents.props.MarginsUiModel

data class TextFieldModel(
    val identifier: String?,
    val icon: String?,
    val hint: String?,
    val keyboardOptions: KeyboardOptions,
    val validations: List<ValidationUiModel>,
    val margins: MarginsUiModel?
) : BodyRowModel {

    override val type: BodyRowType = TEXT_FIELD
}

fun TextFieldModel.mapToUiModel(): TextFieldUiModel {
    return TextFieldUiModel(
        identifier = identifier,
        icon = icon,
        hint = hint,
        keyboardOptions = keyboardOptions,
        validations = validations,
        margins = margins
    )
}
