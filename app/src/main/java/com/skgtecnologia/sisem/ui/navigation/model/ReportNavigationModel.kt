package com.skgtecnologia.sisem.ui.navigation.model

data class ReportNavigationModel(
    val goBackFromReport: Boolean = false,
    val goBackFromImages: Boolean = false,
    val showCamera: Boolean = false,
    val photoTaken: Boolean = false,
    val cancelFinding: Boolean = false,
    val confirmFinding: Boolean = false,
    val closeFinding: Boolean = false,
    val cancelReport: Boolean = false,
    val confirmSendReport: Boolean = false,
    val closeReport: Boolean = false,
    val imagesSize: Int = 0
) : NavigationModel
