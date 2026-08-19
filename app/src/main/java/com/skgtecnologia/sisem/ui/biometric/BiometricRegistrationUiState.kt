package com.skgtecnologia.sisem.ui.biometric

import com.valkiria.uicomponents.bricks.banner.BannerUiModel

data class CrewBiometricStatus(
    val username: String,
    val name: String,
    val role: String,
    val document: String,
    val isEnrolled: Boolean
)

data class BiometricRegistrationUiState(
    val crewStatuses: List<CrewBiometricStatus> = emptyList(),
    val isLoading: Boolean = false,
    val errorModel: BannerUiModel? = null
)
