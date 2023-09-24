package com.skgtecnologia.sisem.ui.clinichistory

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.skgtecnologia.sisem.ui.sections.BodySection

@Composable
fun ClinicHistoryScreen(
    modifier: Modifier = Modifier
) {

    val viewModel = hiltViewModel<ClinicHistoryViewModel>()
    val uiState = viewModel.uiState

    BodySection(body = uiState.screenModel?.body) { _ ->

    }
}
