package com.skgtecnologia.sisem.core.data.remote.model.components.humanbody

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.Modifier
import com.skgtecnologia.sisem.core.data.remote.model.screen.BodyRowResponse
import com.skgtecnologia.sisem.core.data.remote.model.screen.HeaderResponse
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import com.valkiria.uicomponents.components.BodyRowType
import com.valkiria.uicomponents.components.humanbody.HumanBodyUiModel

@JsonClass(generateAdapter = true)
data class HumanBodyResponse(
    @Json(name = "identifier") val identifier: String?,
    @Json(name = "header") val header: HeaderResponse?,
    @Json(name = "values") val values: Map<String, List<WoundResponse>>?,
    @Json(name = "wounds") val wounds: List<String>?,
    @Json(name = "burning_level") val burningLevel: List<String>?,
    @Json(name = "visibility") val visibility: Boolean?,
    @Json(name = "required") val required: Boolean?,
    @Json(name = "section") val section: String?,
    @Json(name = "arrangement") val arrangement: Arrangement.Horizontal?,
    @Json(name = "margins") val modifier: Modifier?
) : BodyRowResponse {

    override val type: BodyRowType = BodyRowType.HUMAN_BODY

    override fun mapToUi(): HumanBodyUiModel = HumanBodyUiModel(
        identifier = identifier ?: error("HumanBody identifier cannot be null"),
        header = header?.mapToUi() ?: error("HumanBody header cannot be null"),
        values = values?.mapValues { (_, value) -> value.map { it.mapToUi() } },
        wounds = wounds ?: error("HumanBody wounds cannot be null"),
        burningLevel = burningLevel ?: error("HumanBody burningLevel cannot be null"),
        visibility = visibility ?: true,
        required = required ?: false,
        section = section,
        arrangement = arrangement ?: Arrangement.Center,
        modifier = modifier ?: Modifier
    )
}
