package com.skgtecnologia.sisem.ui.preoperational.view

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.skgtecnologia.sisem.commons.resources.AndroidIdProvider
import com.skgtecnologia.sisem.di.operation.OperationRole
import com.skgtecnologia.sisem.domain.model.banner.mapToUi
import com.skgtecnologia.sisem.domain.preoperational.usecases.GetPreOperationalScreenView
import com.skgtecnologia.sisem.ui.navigation.NavigationArgument.ROLE
import com.valkiria.uicomponents.bricks.banner.finding.FindingsDetailUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class PreOperationalViewViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val androidIdProvider: AndroidIdProvider,
    private val getPreOperationalScreenView: GetPreOperationalScreenView
) : ViewModel() {

    private var job: Job? = null

    var uiState by mutableStateOf(PreOperationalViewUiState())
        private set

    private val roleName: String? = savedStateHandle[ROLE]

    init {
        val role = OperationRole.getRoleByName(roleName.orEmpty())

        if (role != null) {
            uiState = uiState.copy(isLoading = true)

            job?.cancel()
            job = viewModelScope.launch(Dispatchers.IO) {
                getPreOperationalScreenView.invoke(androidIdProvider.getAndroidId(), role)
                    .onSuccess { preOperationalScreenModel ->
                        withContext(Dispatchers.Main) {
                            uiState = uiState.copy(
                                screenModel = preOperationalScreenModel,
                                isLoading = false
                            )
                        }
                    }
                    .onFailure { throwable ->
                        Timber.wtf(throwable, "This is a failure")
                        withContext(Dispatchers.Main) {
                            uiState = uiState.copy(
                                isLoading = false,
                                errorModel = throwable.mapToUi()
                            )
                        }
                    }
            }
        } else {
            goBack()
        }
    }

    fun goBack() {
        uiState = uiState.copy(
            navigationModel = PreOpViewNavigationModel(back = true)
        )
    }

    fun onNavigationHandled() {
        uiState = uiState.copy(
            navigationModel = null,
            isLoading = false
        )
    }

    fun handleShownError() {
        uiState = uiState.copy(
            errorModel = null
        )
    }

    fun handleShownFindingBottomSheet() {
        uiState = uiState.copy(
            findingDetail = null
        )
    }

    fun showFindings(findingDetail: FindingsDetailUiModel?) {
        uiState = uiState.copy(
            findingDetail = findingDetail
        )
    }
}
