package com.skgtecnologia.sisem.data.remote.model.components.card

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.Modifier
import com.skgtecnologia.sisem.data.remote.model.components.BodyRowResponse
import com.skgtecnologia.sisem.data.remote.model.components.label.TextResponse
import com.skgtecnologia.sisem.data.remote.model.components.label.mapToUi
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import com.valkiria.uicomponents.components.BodyRowType
import com.valkiria.uicomponents.components.card.SimpleCardUiModel

@JsonClass(generateAdapter = true)
data class SimpleCardResponse(
    @Json(name = "identifier") val identifier: String?,
    @Json(name = "icon") val icon: String?,
    @Json(name = "title") val title: TextResponse?,
    @Json(name = "visibility") val visibility: Boolean?,
    @Json(name = "required") val required: Boolean?,
    @Json(name = "arrangement") val arrangement: Arrangement.Horizontal?,
    @Json(name = "margins") val modifier: Modifier?
) : BodyRowResponse {

    override val type: BodyRowType = BodyRowType.SIMPLE_CARD

    override fun mapToUi(): SimpleCardUiModel = SimpleCardUiModel(
        identifier = identifier ?: error("SimpleCard identifier cannot be null"),
        icon = icon ?: error("SimpleCard icon cannot be null"),
        title = title?.mapToUi() ?: error("SimpleCard title cannot be null"),
        visibility = visibility ?: true,
        required = required ?: false,
        arrangement = arrangement ?: Arrangement.Center,
        modifier = modifier ?: Modifier
    )
}
