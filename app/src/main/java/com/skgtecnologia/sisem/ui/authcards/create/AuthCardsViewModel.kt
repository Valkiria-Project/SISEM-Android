package com.skgtecnologia.sisem.ui.authcards.create

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.skgtecnologia.sisem.commons.resources.AndroidIdProvider
import com.skgtecnologia.sisem.domain.authcards.usecases.GetAuthCardsScreen
import com.skgtecnologia.sisem.domain.model.banner.mapToUi
import com.skgtecnologia.sisem.domain.operation.usecases.GetOperationConfig
import com.skgtecnologia.sisem.ui.navigation.AuthRoute
import com.valkiria.uicomponents.bricks.banner.report.ReportsDetailUiModel
import com.valkiria.uicomponents.bricks.chip.ChipSectionUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class AuthCardsViewModel @Inject constructor(
    private val androidIdProvider: AndroidIdProvider,
    private val getAuthCardsScreen: GetAuthCardsScreen,
    private val getOperationConfig: GetOperationConfig,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private var job: Job? = null

    var uiState by mutableStateOf(AuthCardsUiState())
        private set

    init {
        val loggedOutRole = savedStateHandle.toRoute<AuthRoute.AuthCardsRoute>().loggedOutRole
        uiState = uiState.copy(isLoading = true, loggedOutRole = loggedOutRole)

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

    fun showRoleRestrictionBanner(banner: com.valkiria.uicomponents.bricks.banner.BannerUiModel) {
        uiState = uiState.copy(roleRestrictionBanner = banner)
    }

    fun consumeRoleRestrictionBanner() {
        uiState = uiState.copy(roleRestrictionBanner = null)
    }

    fun consumeErrorEvent() {
        uiState = uiState.copy(
            errorModel = null
        )
    }
}
