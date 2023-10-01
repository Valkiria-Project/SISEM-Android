package com.skgtecnologia.sisem.ui.medicalhistory

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.skgtecnologia.sisem.ui.sections.BodySection

@Suppress("UnusedPrivateMember")
@Composable
fun MedicalHistoryScreen(
    modifier: Modifier = Modifier
) {

    val viewModel = hiltViewModel<MedicalHistoryViewModel>()
    val uiState = viewModel.uiState

    BodySection(body = uiState.screenModel?.body) { _ ->

    }
}
