package com.valkiria.uicomponents.components.textfield

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.Modifier
import com.valkiria.uicomponents.components.BodyRowModel
import com.valkiria.uicomponents.components.BodyRowType
import com.valkiria.uicomponents.components.label.TextStyle

data class TextFieldUiModel(
    val identifier: String,
    val icon: String? = null,
    val placeholder: String? = null,
    val label: String? = null,
    val keyboardOptions: KeyboardOptions,
    val textStyle: TextStyle,
    val style: TextFieldStyle = TextFieldStyle.OUTLINED,
    val charLimit: Int? = null,
    val validations: List<ValidationUiModel>,
    val singleLine: Boolean = true,
    val minLines: Int = 1,
    val arrangement: Arrangement.Horizontal,
    val modifier: Modifier = Modifier
) : BodyRowModel {

    override val type: BodyRowType = BodyRowType.TEXT_FIELD
}
