package com.skgtecnologia.sisem.data.remote.model.bodyrow

import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.Modifier
import com.skgtecnologia.sisem.data.remote.model.props.ValidationResponse
import com.skgtecnologia.sisem.data.remote.model.props.mapToUi
import com.skgtecnologia.sisem.domain.model.bodyrow.BodyRowModel
import com.skgtecnologia.sisem.domain.model.bodyrow.BodyRowType
import com.skgtecnologia.sisem.domain.model.bodyrow.TextFieldModel
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class PasswordTextFieldResponse(
    @Json(name = "identifier") val identifier: String?,
    @Json(name = "icon") val icon: String?,
    @Json(name = "hint") val hint: String?,
    @Json(name = "keyboard_type") val keyboardType: KeyboardOptions,
    @Json(name = "validations") val validations: List<ValidationResponse>,
    @Json(name = "margins") val modifier: Modifier?
) : BodyRowResponse {

    override val type: BodyRowType = BodyRowType.PASSWORD_TEXT_FIELD

    override fun mapToDomain(): BodyRowModel = TextFieldModel(
        identifier = identifier.orEmpty(),
        icon = icon.orEmpty(),
        hint = hint.orEmpty(),
        keyboardOptions = keyboardType,
        validations = validations.map { it.mapToUi() },
        modifier = modifier ?: Modifier
    )
}
