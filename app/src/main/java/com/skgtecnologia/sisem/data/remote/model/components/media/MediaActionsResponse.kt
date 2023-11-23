package com.skgtecnologia.sisem.data.remote.model.components.media

import androidx.compose.ui.Modifier
import com.skgtecnologia.sisem.data.remote.model.components.BodyRowResponse
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import com.valkiria.uicomponents.components.BodyRowType
import com.valkiria.uicomponents.components.media.MediaActionsUiModel

@JsonClass(generateAdapter = true)
data class MediaActionsResponse(
    @Json(name = "identifier") val identifier: String?,
    @Json(name = "within_form") val withinForm: Boolean?,
    @Json(name = "visibility") val visibility: Boolean?,
    @Json(name = "required") val required: Boolean?,
    @Json(name = "margins") val modifier: Modifier?
) : BodyRowResponse {

    override val type: BodyRowType = BodyRowType.LABEL

    override fun mapToUi(): MediaActionsUiModel = MediaActionsUiModel(
        identifier = identifier ?: error("MediaActions identifier cannot be null"),
        withinForm = withinForm ?: false,
        visibility = visibility ?: true,
        required = required ?: false,
        modifier = modifier ?: Modifier
    )
}
