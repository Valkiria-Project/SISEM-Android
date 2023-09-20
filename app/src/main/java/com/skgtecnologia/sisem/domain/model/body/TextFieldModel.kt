package com.skgtecnologia.sisem.domain.model.body

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.Modifier
import com.valkiria.uicomponents.model.ui.textfield.TextFieldUiModel
import com.valkiria.uicomponents.model.ui.textfield.ValidationUiModel
import com.valkiria.uicomponents.model.props.TextFieldStyle
import com.valkiria.uicomponents.model.props.TextStyle

data class TextFieldModel(
    val identifier: String,
    val icon: String?,
    val placeholder: String?,
    val label: String?,
    val keyboardOptions: KeyboardOptions,
    val textStyle: TextStyle,
    val style: TextFieldStyle,
    val charLimit: Int?,
    val validations: List<ValidationUiModel>,
    val arrangement: Arrangement.Horizontal,
    val modifier: Modifier = Modifier
) : BodyRowModel {

    override val type: BodyRowType = BodyRowType.TEXT_FIELD
}

fun TextFieldModel.mapToUiModel(): TextFieldUiModel {
    return TextFieldUiModel(
        identifier = identifier,
        icon = icon,
        placeholder = placeholder,
        label = label,
        keyboardOptions = keyboardOptions,
        textStyle = textStyle,
        style = style,
        charLimit = charLimit,
        validations = validations,
        arrangement = arrangement,
        modifier = modifier
    )
}
