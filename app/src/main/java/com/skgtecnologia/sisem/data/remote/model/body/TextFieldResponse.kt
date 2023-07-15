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

@JsonClass(generateAdapter = true)
data class TextFieldResponse(
    val identifier: String?,
    val icon: String?,
    val hint: String?,
    @Json(name = "keyboard_type") val keyboardOptions: KeyboardOptions,
    val validations: List<ValidationResponse>,
    val margins: Modifier?
) : BodyRowResponse {

    override val type: BodyRowType = BodyRowType.TEXT_FIELD

    override fun mapToDomain(): BodyRowModel = TextFieldModel(
        identifier = identifier.orEmpty(),
        icon = icon.orEmpty(),
        hint = hint.orEmpty(),
        keyboardOptions = keyboardOptions,
        validations = validations.map { it.mapToUi() },
        modifier = margins ?: Modifier
    )
}
