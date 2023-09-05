package com.skgtecnologia.sisem.ui.navigation.model

data class ReportNavigationModel(
    val goBack: Boolean = false,
    val showCamera: Boolean = false,
    val photoTaken: Boolean = false,
    val confirmMedia: Boolean = false,
    val saveFinding: Boolean = false
) : NavigationModel
