package com.skgtecnologia.sisem.ui.menu

import com.skgtecnologia.sisem.domain.auth.model.AccessTokenModel

data class MenuUiState(
    val accessTokenModelList: List<AccessTokenModel>? = null,
    val isLogout: Boolean = false,
    val isLoading: Boolean = false
)
