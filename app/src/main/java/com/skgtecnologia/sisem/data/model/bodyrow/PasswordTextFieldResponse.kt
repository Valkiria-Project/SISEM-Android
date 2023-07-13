package com.skgtecnologia.sisem.data.model.bodyrow

import com.skgtecnologia.sisem.data.model.props.MarginsResponse
import com.skgtecnologia.sisem.data.model.props.ValidationResponse
import com.skgtecnologia.sisem.data.model.props.mapToDomain
import com.skgtecnologia.sisem.domain.model.bodyrow.BodyRowModel
import com.skgtecnologia.sisem.domain.model.bodyrow.BodyRowType
import com.skgtecnologia.sisem.domain.model.bodyrow.TextFieldModel
import com.skgtecnologia.sisem.domain.model.props.KeyboardType
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class PasswordTextFieldResponse(
    @Json(name = "identifier") val identifier: String?,
    @Json(name = "icon") val icon: String?,
    @Json(name = "hint") val hint: String?,
    @Json(name = "keyboard_type") val keyboardType: KeyboardType,
    @Json(name = "validations") val validations: List<ValidationResponse>,
    @Json(name = "margins") val margins: MarginsResponse?
) : BodyRowResponse {

    override val type: BodyRowType = BodyRowType.TEXT_FIELD

    override fun mapToDomain(): BodyRowModel = TextFieldModel(
        identifier = identifier.orEmpty(),
        icon = icon.orEmpty(),
        hint = hint.orEmpty(),
        keyboardType = keyboardType,
        validations = validations.map { it.mapToDomain() },
        margins = margins?.mapToDomain()
    )
}
