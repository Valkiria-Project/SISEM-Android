package com.skgtecnologia.sisem.core.data.remote.model.components.obstetrician

import com.skgtecnologia.sisem.data.remote.model.components.label.TextResponse
import com.skgtecnologia.sisem.data.remote.model.components.label.mapToUi
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import com.valkiria.uicomponents.components.obstetrician.ObsDataUiModel

@JsonClass(generateAdapter = true)
data class ObsDataResponse(
    @Json(name = "letter") val letter: TextResponse?,
    @Json(name = "result") val result: TextResponse?
)

fun com.skgtecnologia.sisem.core.data.remote.model.components.obstetrician.ObsDataResponse.mapToUi() = ObsDataUiModel(
    letter = this.letter?.mapToUi() ?: error("ObsDataResponse letter is required"),
    result = this.result?.mapToUi() ?: error("ObsDataResponse result is required")
)
