package com.skgtecnologia.sisem.ui.authcards.create

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.skgtecnologia.sisem.commons.resources.AndroidIdProvider
import com.skgtecnologia.sisem.domain.authcards.usecases.GetAuthCardsScreen
import com.skgtecnologia.sisem.domain.model.banner.mapToUi
import com.skgtecnologia.sisem.domain.operation.usecases.GetOperationConfig
import com.valkiria.uicomponents.bricks.banner.report.ReportsDetailUiModel
import com.valkiria.uicomponents.bricks.chip.ChipSectionUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber

@HiltViewModel
class AuthCardsViewModel @Inject constructor(
    private val androidIdProvider: AndroidIdProvider,
    private val getAuthCardsScreen: GetAuthCardsScreen,
    private val getOperationConfig: GetOperationConfig
) : ViewModel() {

    private var job: Job? = null

    var uiState by mutableStateOf(AuthCardsUiState())
        private set

    init {
        uiState = uiState.copy(isLoading = true)

        job?.cancel()
        job = viewModelScope.launch {
            getOperationConfig.invoke(androidIdProvider.getAndroidId())
                .onSuccess {
                    getAuthCardsScreen()
                }
                .onFailure { throwable ->
                    Timber.wtf(throwable, "This is a failure")

                    uiState = uiState.copy(
                        isLoading = false,
                        errorModel = throwable.mapToUi()
                    )
                }
        }
    }

    private suspend fun getAuthCardsScreen() {
        getAuthCardsScreen.invoke(androidIdProvider.getAndroidId())
            .onSuccess { authCardsScreenModel ->
                withContext(Dispatchers.Main) {
                    uiState = uiState.copy(
                        screenModel = authCardsScreenModel,
                        isLoading = false
                    )
                }
            }
            .onFailure { throwable ->
                Timber.wtf(throwable, "This is a failure")

                uiState = uiState.copy(
                    isLoading = false,
                    errorModel = throwable.mapToUi()
                )
            }
    }

    fun showReportBottomSheet(reportDetail: ReportsDetailUiModel) {
        uiState = uiState.copy(
            reportDetail = reportDetail
        )
    }

    fun consumeReportBottomSheetEvent() {
        uiState = uiState.copy(
            reportDetail = null
        )
    }

    fun showFindingsBottomSheet(chipSection: ChipSectionUiModel) {
        uiState = uiState.copy(
            chipSection = chipSection
        )
    }

    fun consumeFindingsBottomSheetEvent() {
        uiState = uiState.copy(
            chipSection = null
        )
    }

    fun consumeErrorEvent() {
        uiState = uiState.copy(
            errorModel = null
        )
    }
}
