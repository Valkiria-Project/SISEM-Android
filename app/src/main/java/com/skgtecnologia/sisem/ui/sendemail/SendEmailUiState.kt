package com.skgtecnologia.sisem.ui.sendemail

import com.valkiria.uicomponents.bricks.banner.BannerUiModel

data class SendEmailUiState(
    val infoEvent: BannerUiModel? = null,
    val validateFields: Boolean = false,
    val navigationModel: SendEmailNavigationModel? = null,
    val isLoading: Boolean = false,
    val errorEvent: BannerUiModel? = null
)
