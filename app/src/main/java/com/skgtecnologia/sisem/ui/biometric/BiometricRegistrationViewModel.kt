package com.skgtecnologia.sisem.ui.biometric

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.skgtecnologia.sisem.commons.biometric.FaceCredentialStore
import com.skgtecnologia.sisem.di.operation.OperationRole
import com.skgtecnologia.sisem.domain.auth.usecases.GetAllAccessTokens
import com.skgtecnologia.sisem.domain.model.banner.mapToUi
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
class BiometricRegistrationViewModel @Inject constructor(
    private val getAllAccessTokens: GetAllAccessTokens,
    private val faceCredentialStore: FaceCredentialStore
) : ViewModel() {

    private val _uiState = MutableStateFlow(BiometricRegistrationUiState())
    val uiState: StateFlow<BiometricRegistrationUiState> = _uiState

    init {
        loadCrewBiometricStatus()
    }

    fun loadCrewBiometricStatus() {
        _uiState.update { it.copy(isLoading = true) }

        viewModelScope.launch {
            getAllAccessTokens.invoke()
                .onSuccess { accessTokenModels ->
                    val statuses = accessTokenModels.map { token ->
                        val isEnrolled = faceCredentialStore.hasEmbedding(token.username)
                        val humanRole = OperationRole.getRoleByName(token.role)
                            ?.humanizedName
                            .orEmpty()

                        CrewBiometricStatus(
                            username = token.username,
                            name = token.nameUser,
                            role = humanRole,
                            document = "${token.docType} ${token.document}",
                            isEnrolled = isEnrolled
                        )
                    }

                    withContext(Dispatchers.Main) {
                        _uiState.update {
                            it.copy(
                                crewStatuses = statuses,
                                isLoading = false
                            )
                        }
                    }
                }
                .onFailure { throwable ->
                    Timber.wtf(throwable, "Failed to load crew biometric status")

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
