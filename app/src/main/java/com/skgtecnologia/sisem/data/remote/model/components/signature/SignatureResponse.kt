package com.skgtecnologia.sisem.data.remote.model.components.signature

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.Modifier
import com.skgtecnologia.sisem.data.remote.model.components.BodyRowResponse
import com.skgtecnologia.sisem.data.remote.model.components.button.ButtonResponse
import com.skgtecnologia.sisem.data.remote.model.components.label.TextResponse
import com.skgtecnologia.sisem.data.remote.model.components.label.mapToUi
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import com.valkiria.uicomponents.components.BodyRowType
import com.valkiria.uicomponents.components.signature.SignatureUiModel

@JsonClass(generateAdapter = true)
data class SignatureResponse(
    @Json(name = "identifier") val identifier: String?,
    @Json(name = "label") val signatureLabel: TextResponse?,
    @Json(name = "button") val signatureButton: ButtonResponse?,
    @Json(name = "visibility") val visibility: Boolean?,
    @Json(name = "required") val required: Boolean?,
    @Json(name = "arrangement") val arrangement: Arrangement.Horizontal?,
    @Json(name = "margins") val modifier: Modifier?
) : BodyRowResponse {

    override val type: BodyRowType = BodyRowType.SIGNATURE

    override fun mapToUi(): SignatureUiModel = SignatureUiModel(
        identifier = identifier ?: error("Signature identifier cannot be null"),
        signatureLabel = signatureLabel?.mapToUi()
            ?: error("Signature signatureLabel cannot be null"),
        signatureButton = signatureButton?.mapToUi()
            ?: error("Signature signatureButton cannot be null"),
        visibility = visibility ?: true,
        required = required ?: false,
        modifier = modifier ?: Modifier
    )
}
