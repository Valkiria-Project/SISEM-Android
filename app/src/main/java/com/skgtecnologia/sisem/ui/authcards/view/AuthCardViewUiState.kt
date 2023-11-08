package com.skgtecnologia.sisem.ui.authcards.view

import com.skgtecnologia.sisem.domain.model.screen.ScreenModel
import com.valkiria.uicomponents.bricks.banner.BannerUiModel

data class AuthCardViewUiState(
    val screenModel: ScreenModel? = null,
    val isLoading: Boolean = false,
    val errorModel: BannerUiModel? = null,
    val navigationModel: AuthCardViewNavigationModel? = null
)
