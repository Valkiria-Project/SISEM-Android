package com.skgtecnologia.sisem.core.data.remote.model.components.dropdown

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.Modifier
import com.skgtecnologia.sisem.core.data.remote.model.screen.BodyRowResponse
import com.skgtecnologia.sisem.core.data.remote.model.screen.HeaderResponse
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import com.valkiria.uicomponents.components.BodyRowType
import com.valkiria.uicomponents.components.dropdown.DropDownUiModel

@JsonClass(generateAdapter = true)
data class DropDownResponse(
    @Json(name = "identifier") val identifier: String?,
    @Json(name = "label") val label: String?,
    @Json(name = "options") val items: List<DropDownItemResponse>?,
    @Json(name = "selected") val selected: String?,
    @Json(name = "header") val header: HeaderResponse?,
    @Json(name = "section") val section: String?,
    @Json(name = "visibility") val visibility: Boolean?,
    @Json(name = "required") val required: Boolean?,
    @Json(name = "arrangement") val arrangement: Arrangement.Horizontal?,
    @Json(name = "margins") val modifier: Modifier?
) : BodyRowResponse {

    override val type: BodyRowType = BodyRowType.DROP_DOWN

    override fun mapToUi(): DropDownUiModel = DropDownUiModel(
        identifier = identifier ?: error("DropDown identifier cannot be null"),
        label = label ?: error("DropDown label cannot be null"),
        items = items?.map { it.mapToUi() } ?: error("DropDown items cannot be null"),
        selected = items.find { it.id.equals(selected, true) }?.name ?: selected.orEmpty(),
        header = header?.mapToUi() ?: error("DropDown header cannot be null"),
        section = section,
        visibility = visibility ?: true,
        required = required ?: false,
        arrangement = arrangement ?: Arrangement.Center,
        modifier = modifier ?: Modifier
    )
}
