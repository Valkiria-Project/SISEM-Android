package com.skgtecnologia.sisem.data.remote.model.components.signature

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
    @Json(name = "is_pending_signature") val isPendingSignature: Boolean?,
    @Json(name = "signature_label") val signatureLabel: TextResponse?,
    @Json(name = "signature_button") val signatureButton: ButtonResponse?,
    @Json(name = "margins_pending_signature") val marginsPendingSignature: Modifier?,
    @Json(name = "name") val name: TextResponse?,
    @Json(name = "identification") val identification: TextResponse?,
    @Json(name = "signature") val signature: String?,
    @Json(name = "margins_signed") val marginsSigned: Modifier?
) : BodyRowResponse {

    override val type: BodyRowType = BodyRowType.SEGMENTED_SWITCH

    override fun mapToUi(): SignatureUiModel = SignatureUiModel(
        identifier = identifier ?: error("Signature identifier cannot be null"),
        isPendingSignature = isPendingSignature
            ?: error("Signature isPendingSignature cannot be null"),
        signatureLabel = signatureLabel?.mapToUi(),
        signatureButton = signatureButton?.mapToUi(),
        marginsPendingSignature = marginsPendingSignature ?: Modifier,
        name = name?.mapToUi(),
        identification = identification?.mapToUi(),
        signature = signature,
        marginsSigned = marginsPendingSignature ?: Modifier
    )
}
