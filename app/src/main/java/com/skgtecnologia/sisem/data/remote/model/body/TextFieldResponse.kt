package com.skgtecnologia.sisem.data.remote.model.body

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.Modifier
import com.skgtecnologia.sisem.data.remote.model.bricks.ValidationResponse
import com.skgtecnologia.sisem.data.remote.model.bricks.mapToUi
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import com.valkiria.uicomponents.model.props.TextFieldStyle
import com.valkiria.uicomponents.model.props.TextStyle
import com.valkiria.uicomponents.model.ui.body.BodyRowType
import com.valkiria.uicomponents.model.ui.body.TextFieldUiModel

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
    @Json(name = "validations") val validations: List<ValidationResponse>?,
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
        charLimit = charLimit,
        validations = validations?.map { it.mapToUi() }
            ?: error("TextField validations cannot be null"),
        arrangement = arrangement ?: Arrangement.Center,
        modifier = margins ?: Modifier
    )
}
