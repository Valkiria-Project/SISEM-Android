package com.skgtecnologia.sisem.domain.biometric.model

data class BiometricModel(
    val documentNumber: String,
    val embeddings: List<FloatArray>
)
