package com.skgtecnologia.sisem.domain.model.body

import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.Modifier
import com.skgtecnologia.sisem.domain.model.body.BodyRowType.TEXT_FIELD
import com.valkiria.uicomponents.components.passwordtextfield.PasswordTextFieldUiModel
import com.valkiria.uicomponents.components.textfield.ValidationUiModel
import com.valkiria.uicomponents.props.TextStyle

data class PasswordTextFieldModel(
    val identifier: String,
    val icon: String?,
    val placeholder: String?,
    val label: String?,
    val keyboardOptions: KeyboardOptions,
    val textStyle: TextStyle,
    val validations: List<ValidationUiModel>,
    val modifier: Modifier = Modifier
) : BodyRowModel {

    override val type: BodyRowType = TEXT_FIELD
}

fun PasswordTextFieldModel.mapToUiModel(): PasswordTextFieldUiModel {
    return PasswordTextFieldUiModel(
        icon = icon,
        placeholder = placeholder,
        label = label,
        keyboardOptions = keyboardOptions,
        textStyle = textStyle,
        validations = validations,
        modifier = modifier
    )
}