package com.skgtecnologia.sisem.data.myscreen.remote.model

import com.skgtecnologia.sisem.domain.core.model.bodyrow.BodyRowModel
import com.skgtecnologia.sisem.domain.core.model.bodyrow.BodyRowType
import com.skgtecnologia.sisem.domain.myscreen.model.KeyboardType
import com.skgtecnologia.sisem.domain.core.model.bodyrow.TextFieldModel
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class TextFieldResponse(
    val identifier: String?,
    val icon: String?,
    val hint: String?,
    @Json(name = "keyboard_type") val keyboardType: KeyboardType,
    val validations: List<ValidationResponse>,
    val margins: MarginsResponse?
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
