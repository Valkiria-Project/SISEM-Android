package com.skgtecnologia.sisem.data.remote.model.components.time

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.Modifier
import com.skgtecnologia.sisem.data.remote.model.components.BodyRowResponse
import com.skgtecnologia.sisem.data.remote.model.components.label.TextResponse
import com.skgtecnologia.sisem.data.remote.model.components.label.mapToUI
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import com.valkiria.uicomponents.components.BodyRowType
import com.valkiria.uicomponents.components.timepicker.TimePickerUiModel

@JsonClass(generateAdapter = true)
data class TimePickerResponse(
    @Json(name = "identifier") val identifier: String?,
    @Json(name = "title") val title: TextResponse?,
    @Json(name = "hour") val hour: TextResponse?,
    @Json(name = "minute") val minute: TextResponse?,
    @Json(name = "arrangement") val arrangement: Arrangement.Horizontal?,
    @Json(name = "margins") val modifier: Modifier?
) : BodyRowResponse {

    override val type: BodyRowType = BodyRowType.TIME_PICKER

    override fun mapToUi(): TimePickerUiModel = TimePickerUiModel(
        identifier = identifier ?: error("TimePicker identifier cannot be null"),
        title = title?.mapToUI() ?: error("TimePicker title cannot be null"),
        hour = hour?.mapToUI() ?: error("TimePicker hour cannot be null"),
        minute = minute?.mapToUI() ?: error("TimePicker minute cannot be null"),
        arrangement = arrangement ?: Arrangement.Center,
        modifier = modifier ?: Modifier
    )
}
