package com.skgtecnologia.sisem.domain.biometric.model

data class BiometricModel(
    val username: String,
    val role: String,
    val embeddings: List<FloatArray>
)
