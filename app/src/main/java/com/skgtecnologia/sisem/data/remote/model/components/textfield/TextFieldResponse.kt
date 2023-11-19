package com.skgtecnologia.sisem.data.remote.model.components.textfield

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.Modifier
import com.skgtecnologia.sisem.data.remote.model.components.BodyRowResponse
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import com.valkiria.uicomponents.components.BodyRowType
import com.valkiria.uicomponents.components.label.TextStyle
import com.valkiria.uicomponents.components.textfield.TextFieldStyle
import com.valkiria.uicomponents.components.textfield.TextFieldUiModel

@Suppress("MagicNumber", "ComplexMethod")
@JsonClass(generateAdapter = true)
data class TextFieldResponse(
    @Json(name = "identifier") val identifier: String?,
    @Json(name = "icon") val icon: String?,
    @Json(name = "placeholder") val placeholder: String?,
    @Json(name = "hint") val label: String?,
    @Json(name = "keyboard_type") val keyboardOptions: KeyboardOptions?,
    @Json(name = "text_style") val textStyle: TextStyle?,
    @Json(name = "style") val style: TextFieldStyle?,
    @Json(name = "char_limit") val charLimit: Int?,
    @Json(name = "enabled") val enabled: Boolean?,
    @Json(name = "validations") val validations: List<ValidationResponse>?,
    @Json(name = "real_time_validation") val realTimeValidation: Boolean?,
    @Json(name = "max_date") val maxDate: String?,
    @Json(name = "min_date") val minDate: String?,
    @Json(name = "visibility") val visibility: Boolean?,
    @Json(name = "required") val required: Boolean?,
    @Json(name = "text") val text: String?,
    @Json(name = "min_lines") val minLines: Int?,
    @Json(name = "single_line") val singleLine: Boolean?,
    @Json(name = "arrangement") val arrangement: Arrangement.Horizontal?,
    @Json(name = "margins") val margins: Modifier?
) : BodyRowResponse {

    override val type: BodyRowType = BodyRowType.TEXT_FIELD

    override fun mapToUi(): TextFieldUiModel = TextFieldUiModel(
        identifier = identifier ?: error("TextField identifier cannot be null"),
        icon = icon,
        placeholder = placeholder,
        label = label,
        keyboardOptions = keyboardOptions ?: error("TextField keyboardOptions cannot be null"),
        textStyle = textStyle ?: error("TextField textStyle cannot be null"),
        style = style ?: error("TextField style cannot be null"),
        charLimit = charLimit ?: 600,
        enabled = enabled ?: true,
        validations = validations?.map { it.mapToUi() }
            ?: error("TextField validations cannot be null"),
        realTimeValidation = realTimeValidation ?: false,
        maxDate = maxDate,
        visibility = visibility ?: true,
        required = required ?: true,
        text = text ?: "",
        minLines = minLines ?: 1,
        singleLine = singleLine ?: true,
        arrangement = arrangement ?: Arrangement.Center,
        modifier = margins ?: Modifier
    )
}
