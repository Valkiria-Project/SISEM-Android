package com.skgtecnologia.sisem.data.remote.model.body

import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.Modifier
import com.skgtecnologia.sisem.data.remote.model.props.ValidationResponse
import com.skgtecnologia.sisem.data.remote.model.props.mapToUi
import com.skgtecnologia.sisem.domain.model.body.BodyRowModel
import com.skgtecnologia.sisem.domain.model.body.BodyRowType
import com.skgtecnologia.sisem.domain.model.body.TextFieldModel
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import com.valkiria.uicomponents.props.TextStyle

@JsonClass(generateAdapter = true)
data class PasswordTextFieldResponse(
    @Json(name = "identifier") val identifier: String?,
    @Json(name = "icon") val icon: String?,
    @Json(name = "placeholder") val placeholder: String?,
    @Json(name = "hint") val hint: String?,
    @Json(name = "keyboard_type") val keyboardOptions: KeyboardOptions,
    @Json(name = "text_style") val textStyle: TextStyle,
    @Json(name = "validations") val validations: List<ValidationResponse>,
    @Json(name = "margins") val margins: Modifier?
) : BodyRowResponse {

    override val type: BodyRowType = BodyRowType.PASSWORD_TEXT_FIELD

    override fun mapToDomain(): BodyRowModel = TextFieldModel(
        identifier = identifier.orEmpty(),
        icon = icon.orEmpty(),
        placeholder = placeholder.orEmpty(),
        label = hint.orEmpty(),
        keyboardOptions = keyboardOptions,
        textStyle = textStyle,
        validations = validations.map { it.mapToUi() },
        modifier = margins ?: Modifier
    )
}
