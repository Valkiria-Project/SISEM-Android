package com.skgtecnologia.sisem.domain.authcards.model

data class ConfigModel(
    val id: Int,
    val preoperationalTime: Long,
    val clinicHistObservationsTime: Long,
    val loginTime: Long,
    val numImgPreoperationalDriver: Int,
    val numImgPreoperationalDoctor: Int,
    val numImgPreoperationalAux: Int,
    val numImgNovelty: Int,
    val authMethod: String,
    val attentionsType: String,
    val status: Boolean
)
