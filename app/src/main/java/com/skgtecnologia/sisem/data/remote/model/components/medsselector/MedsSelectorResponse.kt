package com.skgtecnologia.sisem.data.remote.model.components.medsselector

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.Modifier
import com.skgtecnologia.sisem.data.remote.model.components.BodyRowResponse
import com.skgtecnologia.sisem.data.remote.model.components.button.ButtonResponse
import com.skgtecnologia.sisem.data.remote.model.components.card.InfoCardResponse
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import com.valkiria.uicomponents.components.BodyRowModel
import com.valkiria.uicomponents.components.BodyRowType
import com.valkiria.uicomponents.components.medsselector.MedsSelectorUiModel

@JsonClass(generateAdapter = true)
data class MedsSelectorResponse(
    @Json(name = "identifier") val identifier: String?,
    @Json(name = "button") val button: ButtonResponse?,
    @Json(name = "medicines") val medicines: List<InfoCardResponse>?,
    @Json(name = "section") val section: String?,
    @Json(name = "visibility") val visibility: Boolean?,
    @Json(name = "required") val required: Boolean?,
    @Json(name = "arrangement") val arrangement: Arrangement.Horizontal?,
    @Json(name = "margins") val modifier: Modifier?
) : BodyRowResponse {

    override val type: BodyRowType = BodyRowType.INFO_CARD_BUTTON

    override fun mapToUi(): BodyRowModel = MedsSelectorUiModel(
        identifier = identifier ?: error("MedsSelector identifier cannot be null"),
        button = button?.mapToUi() ?: error("MedsSelector button cannot be null"),
        medicines = medicines?.map { it.mapToUi() } ?: emptyList(),
        section = section,
        visibility = visibility ?: true,
        required = required ?: false,
        arrangement = arrangement ?: Arrangement.Center,
        modifier = modifier ?: Modifier
    )
}
