package com.skgtecnologia.sisem.ui.biometric

import com.valkiria.uicomponents.bricks.banner.BannerUiModel

data class CrewBiometricStatus(
    val username: String,
    val name: String,
    val role: String,
    val document: String
)

data class BiometricEnrollmentUiState(
    val document: String = "",
    val crewMember: CrewBiometricStatus? = null,
    val isRegistered: Boolean = false,
    val isLoading: Boolean = false,
    val errorModel: BannerUiModel? = null
)
