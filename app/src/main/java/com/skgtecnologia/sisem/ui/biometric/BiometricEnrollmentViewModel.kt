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

                    val crewMember = token?.let {
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
                        _uiState.update { state ->
                            when (status) {
                                BiometricRegistrationStatus.Registered -> state.copy(
                                    crewMember = crewMember,
                                    isRegistered = true,
                                    isLoading = false
                                )

                                BiometricRegistrationStatus.NotRegistered -> state.copy(
                                    crewMember = crewMember,
                                    isRegistered = false,
                                    isLoading = false
                                )

                                BiometricRegistrationStatus.NetworkIntermittency -> state.copy(
                                    crewMember = crewMember,
                                    isLoading = false,
                                    errorModel = biometricIntermittencyBanner().mapToUi()
                                )

                                BiometricRegistrationStatus.QueryError -> state.copy(
                                    crewMember = crewMember,
                                    isLoading = false,
                                    errorModel = biometricQueryErrorBanner().mapToUi()
                                )
                            }
                        }
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
