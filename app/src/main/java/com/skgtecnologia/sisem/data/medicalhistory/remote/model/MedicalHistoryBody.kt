package com.skgtecnologia.sisem.data.medicalhistory.remote.model

import com.squareup.moshi.Json

data class MedicalHistoryBody(
    @Json(name = "id_turn") val idTurn: String,
    @Json(name = "id_aph") val idAph: String,
    @Json(name = "human_body_values") val humanBodyValues: List<HumanBodyBody>,
    @Json(name = "segmented_values") val segmentedValues: Map<String, Boolean>,
    @Json(name = "signature_values") val signatureValues: Map<String, String>,
    @Json(name = "fields_values") val fieldsValue: Map<String, String>,
    @Json(name = "slider_values") val sliderValues: Map<String, String>,
    @Json(name = "drop_down_values") val dropDownValues: Map<String, String>,
    @Json(name = "chip_selection_values") val chipSelectionValues: Map<String, String>,
    @Json(name = "chip_option_values") val chipOptionsValues: Map<String, List<String>>,
    @Json(name = "image_button_section_values") val imageButtonSectionValues: Map<String, String>,
    @Json(name = "vital_signs") val vitalSigns: Map<String, Map<String, String>>,
    @Json(name = "info_card_button_values") val infoCardButtonValues: List<Map<String, String>>
)
