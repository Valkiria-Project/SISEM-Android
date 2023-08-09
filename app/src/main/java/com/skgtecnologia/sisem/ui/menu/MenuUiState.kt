package com.skgtecnologia.sisem.ui.menu

import com.skgtecnologia.sisem.domain.login.model.AccessTokenModel

data class MenuUiState(
    val isLoading: Boolean = false,
    val isLogout: Boolean = false,
    val accessTokenModelList: List<AccessTokenModel>? = null
)
