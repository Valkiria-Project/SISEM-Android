package com.skgtecnologia.sisem.domain.model.body

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.Modifier
import com.valkiria.uicomponents.components.textfield.TextFieldUiModel
import com.valkiria.uicomponents.components.textfield.ValidationUiModel
import com.valkiria.uicomponents.props.TextStyle

data class TextFieldModel(
    val identifier: String,
    val icon: String?,
    val placeholder: String?,
    val label: String?,
    val keyboardOptions: KeyboardOptions,
    val textStyle: TextStyle,
    val validations: List<ValidationUiModel>,
    val arrangement: Arrangement.Horizontal,
    val modifier: Modifier = Modifier
) : BodyRowModel {

    override val type: BodyRowType = BodyRowType.TEXT_FIELD
}

fun TextFieldModel.mapToUiModel(): TextFieldUiModel {
    return TextFieldUiModel(
        icon = icon,
        placeholder = placeholder,
        label = label,
        keyboardOptions = keyboardOptions,
        textStyle = textStyle,
        validations = validations,
        arrangement = arrangement,
        modifier = modifier
    )
}
