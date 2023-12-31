package com.skgtecnologia.sisem.ui.report

import com.skgtecnologia.sisem.domain.preoperational.model.Novelty
import com.skgtecnologia.sisem.ui.navigation.NavigationModel

data class ReportNavigationModel(
    val isFromPreOperational: Boolean = true,
    val goBackFromReport: Boolean = false,
    val goBackFromImages: Boolean = false,
    val showCamera: Boolean = false,
    val photoTaken: Boolean = false,
    val cancelFinding: Boolean = false,
    val closeFinding: Boolean = false,
    val cancelReport: Boolean = false,
    val closeReport: Boolean = false,
    val imagesSize: Int = 0,
    val novelty: Novelty? = null
) : NavigationModel
