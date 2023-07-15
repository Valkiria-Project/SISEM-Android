package com.skgtecnologia.sisem.domain.model.body

import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.Modifier
import com.skgtecnologia.sisem.domain.model.body.BodyRowType.TEXT_FIELD
import com.valkiria.uicomponents.components.textfield.TextFieldUiModel
import com.valkiria.uicomponents.components.textfield.ValidationUiModel

data class TextFieldModel(
    val identifier: String?,
    val icon: String?,
    val hint: String?,
    val keyboardOptions: KeyboardOptions,
    val validations: List<ValidationUiModel>,
    val modifier: Modifier = Modifier
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
        modifier = modifier
    )
}
