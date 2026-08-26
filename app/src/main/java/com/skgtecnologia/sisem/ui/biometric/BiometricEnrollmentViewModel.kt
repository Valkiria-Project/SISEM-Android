package com.skgtecnologia.sisem.ui.biometric

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.skgtecnologia.sisem.di.operation.OperationRole
import com.skgtecnologia.sisem.domain.auth.usecases.GetAllAccessTokens
import com.skgtecnologia.sisem.domain.biometric.model.BiometricRegistrationStatus
import com.skgtecnologia.sisem.domain.biometric.usecases.GetBiometricRegistrationStatus
import com.skgtecnologia.sisem.domain.model.banner.biometricIntermittencyBanner
import com.skgtecnologia.sisem.domain.model.banner.biometricNotRegisteredBanner
import com.skgtecnologia.sisem.domain.model.banner.biometricQueryErrorBanner
import com.skgtecnologia.sisem.domain.model.banner.mapToUi
import com.skgtecnologia.sisem.ui.navigation.MainRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class BiometricEnrollmentViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getAllAccessTokens: GetAllAccessTokens,
    private val getBiometricRegistrationStatus: GetBiometricRegistrationStatus
) : ViewModel() {

    private val document = savedStateHandle.toRoute<MainRoute.BiometricEnrollmentRoute>().document

    private val _uiState = MutableStateFlow(BiometricEnrollmentUiState())
    val uiState: StateFlow<BiometricEnrollmentUiState> = _uiState

    init {
        loadCrewMember()
    }

    fun loadCrewMember() {
        _uiState.update { it.copy(document = document, isLoading = true) }

        viewModelScope.launch {
            getAllAccessTokens.invoke()
                .onSuccess { accessTokenModels ->
                    // Crew need not be logged in; when a matching session exists we enrich
                    // the screen with name/role, otherwise we show only the document.
                    val token = accessTokenModels.firstOrNull { it.document == document }

                    val localCrewMember = token?.let {
                        val humanRole = OperationRole.getRoleByName(it.role)
                            ?.humanizedName
                            .orEmpty()

                        CrewBiometricStatus(
                            username = it.username,
                            name = it.nameUser,
                            role = humanRole,
                            document = "${it.docType} ${it.document}"
                        )
                    }

                    // The CTA (register vs update) reflects the cloud, not local state: a
                    // líder may enroll a crew member who has never logged in on this device.
                    val status = getBiometricRegistrationStatus(document)

                    withContext(Dispatchers.Main) {
                        _uiState.update { state -> state.withStatus(status, localCrewMember) }
                    }
                }
                .onFailure { throwable ->
                    Timber.wtf(throwable, "Failed to load crew member for biometric enrollment")

                    withContext(Dispatchers.Main) {
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                errorModel = throwable.mapToUi()
                            )
                        }
                    }
                }
        }
    }

    fun consumeError() {
        _uiState.update { it.copy(errorModel = null) }
    }
}

private fun BiometricEnrollmentUiState.withStatus(
    status: BiometricRegistrationStatus,
    localCrewMember: CrewBiometricStatus?
): BiometricEnrollmentUiState = when (status) {
    is BiometricRegistrationStatus.Registered -> copy(
        crewMember = status.toCrewBiometricStatus(localCrewMember?.username.orEmpty()),
        isRegistered = true,
        isLoading = false
    )

    is BiometricRegistrationStatus.NotRegistered -> copy(
        crewMember = status.toCrewBiometricStatusOrNull(localCrewMember?.username.orEmpty()) ?: localCrewMember,
        isRegistered = false,
        isLoading = false,
        errorModel = biometricNotRegisteredBanner().mapToUi()
    )

    BiometricRegistrationStatus.NetworkIntermittency -> copy(
        crewMember = localCrewMember,
        isLoading = false,
        errorModel = biometricIntermittencyBanner().mapToUi()
    )

    BiometricRegistrationStatus.QueryError -> copy(
        crewMember = localCrewMember,
        isLoading = false,
        errorModel = biometricQueryErrorBanner().mapToUi()
    )
}

/**
 * Maps the /exists payload into the UI model. [fallbackUsername] is only known locally
 * (from a prior login on this device); GET v1/biometric/exists never returns it.
 *
 * The `role` value observed from the backend is a plain Spanish word (e.g. "medico") rather
 * than an [OperationRole] enum name, so it is humanized by keyword instead of exact match.
 * Unrecognized values are shown as-is rather than dropped.
 */
private fun BiometricRegistrationStatus.Registered.toCrewBiometricStatus(
    fallbackUsername: String
) = CrewBiometricStatus(
    username = fallbackUsername,
    name = "$userName $userLastName".trim(),
    role = humanizeRole(role),
    document = documentNumber
)

/**
 * Maps the /exists payload into the UI model even when `exists: false` — the backend may
 * still return identity data for a document that has no biometric record yet, and that
 * data should be shown whenever it isn't null/blank. Returns null only when the backend
 * gave no identity data at all, letting the caller fall back to a local session match.
 */
private fun BiometricRegistrationStatus.NotRegistered.toCrewBiometricStatusOrNull(
    fallbackUsername: String
): CrewBiometricStatus? {
    val name = listOfNotNull(userName, userLastName)
        .filter { it.isNotBlank() }
        .joinToString(" ")
    val hasIdentity = name.isNotBlank() || !documentNumber.isNullOrBlank() || !role.isNullOrBlank()

    return if (hasIdentity) {
        CrewBiometricStatus(
            username = fallbackUsername,
            name = name,
            role = role?.let(::humanizeRole).orEmpty(),
            document = documentNumber.orEmpty()
        )
    } else {
        null
    }
}

private fun humanizeRole(rawRole: String): String {
    val normalized = rawRole.trim().lowercase(Locale.ROOT)
    return when {
        normalized.contains("medic") -> OperationRole.MEDIC_APH.humanizedName
        normalized.contains("conductor") || normalized.contains("driver") -> OperationRole.DRIVER.humanizedName
        normalized.contains("auxiliar") -> OperationRole.AUXILIARY_AND_OR_TAPH.humanizedName
        normalized.contains("lider") || normalized.contains("líder") -> OperationRole.LEAD_APH.humanizedName
        else -> rawRole
    }
}
