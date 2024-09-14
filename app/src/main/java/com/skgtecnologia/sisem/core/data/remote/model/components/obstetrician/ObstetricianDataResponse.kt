package com.skgtecnologia.sisem.core.data.remote.model.components.obstetrician

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.Modifier
import com.skgtecnologia.sisem.core.data.remote.model.components.label.TextResponse
import com.skgtecnologia.sisem.core.data.remote.model.components.label.mapToUi
import com.skgtecnologia.sisem.core.data.remote.model.screen.BodyRowResponse
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import com.valkiria.uicomponents.components.BodyRowType
import com.valkiria.uicomponents.components.obstetrician.ObstetricianDataUiModel

@JsonClass(generateAdapter = true)
data class ObstetricianDataResponse(
    @Json(name = "identifier") val identifier: String?,
    @Json(name = "data") val data: List<ObsDataResponse>?,
    @Json(name = "fur") val fur: TextResponse?,
    @Json(name = "arrangement") val arrangement: Arrangement.Horizontal?,
    @Json(name = "margins") val modifier: Modifier?
) : BodyRowResponse {

    override val type: BodyRowType = BodyRowType.OBSTETRICIAN_DATA

    override fun mapToUi(): ObstetricianDataUiModel = ObstetricianDataUiModel(
        identifier = identifier ?: error("ObstetricianDataResponse is required"),
        data = data?.map { it.mapToUi() } ?: error("ObstetricianDataResponse is required"),
        fur = fur?.mapToUi() ?: error("ObstetricianDataResponse is required"),
        arrangement = arrangement ?: Arrangement.Center,
        modifier = modifier ?: Modifier
    )
}
