package com.skgtecnologia.sisem.data.remote.model.components.body

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.Modifier
import com.skgtecnologia.sisem.data.remote.model.components.BodyRowResponse
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import com.valkiria.uicomponents.components.BodyRowType
import com.valkiria.uicomponents.components.humanbody.HumanBodyViewUiModel

@JsonClass(generateAdapter = true)
data class HumanBodyViewResponse(
    @Json(name = "identifier") val identifier: String?,
    // FIXME: Pending to review back response
    @Json(name = "wounds") val wounds: List<WoundResponse>?,
    @Json(name = "visibility") val visibility: Boolean?,
    @Json(name = "required") val required: Boolean?,
    @Json(name = "section") val section: String?,
    @Json(name = "arrangement") val arrangement: Arrangement.Horizontal?,
    @Json(name = "margins") val modifier: Modifier?
) : BodyRowResponse {

    override val type: BodyRowType = BodyRowType.HUMAN_BODY_VIEW

    override fun mapToUi(): HumanBodyViewUiModel = HumanBodyViewUiModel(
        identifier = identifier ?: error("HumanBody identifier cannot be null"),
        wounds = wounds?.map { it.mapToUi() },
        visibility = visibility ?: true,
        required = required ?: false,
        section = section,
        arrangement = arrangement ?: Arrangement.Center,
        modifier = modifier ?: Modifier
    )
}
