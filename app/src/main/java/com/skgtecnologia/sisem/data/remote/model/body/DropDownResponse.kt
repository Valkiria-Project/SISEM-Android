package com.skgtecnologia.sisem.data.remote.model.body

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.Modifier
import com.skgtecnologia.sisem.data.remote.model.bricks.DropDownItemResponse
import com.skgtecnologia.sisem.data.remote.model.bricks.mapToUi
import com.valkiria.uicomponents.components.body.DropDownUiModel
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import com.valkiria.uicomponents.components.BodyRowType

@JsonClass(generateAdapter = true)
data class DropDownResponse(
    @Json(name = "identifier") val identifier: String?,
    @Json(name = "label") val label: String?,
    @Json(name = "options") val options: List<DropDownItemResponse>?,
    @Json(name = "selected") val selected: String?,
    @Json(name = "arrangement") val arrangement: Arrangement.Horizontal?,
    @Json(name = "margins") val modifier: Modifier?
) : BodyRowResponse {

    override val type: BodyRowType = BodyRowType.DROP_DOWN

    override fun mapToUi(): com.valkiria.uicomponents.components.body.DropDownUiModel =
        com.valkiria.uicomponents.components.body.DropDownUiModel(
            identifier = identifier ?: error("DropDown identifier cannot be null"),
            label = label ?: error("DropDown min cannot be null"),
            options = options?.map { it.mapToUi() } ?: error("DropDown options cannot be null"),
            selected = selected,
            arrangement = arrangement ?: Arrangement.Center,
            modifier = modifier ?: Modifier
        )
}
