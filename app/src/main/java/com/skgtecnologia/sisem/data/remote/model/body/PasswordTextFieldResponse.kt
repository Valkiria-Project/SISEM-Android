package com.skgtecnologia.sisem.data.remote.model.body

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.Modifier
import com.skgtecnologia.sisem.data.remote.model.bricks.ValidationResponse
import com.skgtecnologia.sisem.data.remote.model.bricks.mapToUi
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import com.valkiria.uicomponents.components.label.TextStyle
import com.valkiria.uicomponents.components.BodyRowType
import com.valkiria.uicomponents.components.textfield.PasswordTextFieldUiModel

@JsonClass(generateAdapter = true)
data class PasswordTextFieldResponse(
    @Json(name = "identifier") val identifier: String?,
    @Json(name = "icon") val icon: String?,
    @Json(name = "placeholder") val placeholder: String?,
    @Json(name = "hint") val label: String?,
    @Json(name = "keyboard_type") val keyboardOptions: KeyboardOptions?,
    @Json(name = "text_style") val textStyle: TextStyle?,
    @Json(name = "validations") val validations: List<ValidationResponse>?,
    @Json(name = "arrangement") val arrangement: Arrangement.Horizontal?,
    @Json(name = "margins") val margins: Modifier?
) : BodyRowResponse {

    override val type: BodyRowType = BodyRowType.PASSWORD_TEXT_FIELD

    override fun mapToUi(): PasswordTextFieldUiModel = PasswordTextFieldUiModel(
        identifier = identifier ?: error("PasswordTextField identifier cannot be null"),
        icon = icon,
        placeholder = placeholder,
        label = label,
        keyboardOptions = keyboardOptions
            ?: error("PasswordTextField keyboardOptions cannot be null"),
        textStyle = textStyle ?: error("PasswordTextField textStyle cannot be null"),
        validations = validations?.map { it.mapToUi() }
            ?: error("PasswordTextField validations cannot be null"),
        arrangement = arrangement ?: Arrangement.Center,
        modifier = margins ?: Modifier
    )
}
