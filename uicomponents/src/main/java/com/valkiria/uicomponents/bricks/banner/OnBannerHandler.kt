package com.valkiria.uicomponents.bricks.banner

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import com.valkiria.uicomponents.action.UiAction

@Composable
fun OnBannerHandler(
    uiModel: BannerUiModel?,
    onAction: (actionInput: UiAction) -> Unit
) {
    val keyboardController = LocalSoftwareKeyboardController.current

    LaunchedEffect(uiModel) {
        if (uiModel != null) keyboardController?.hide()
    }

    uiModel?.let { bannerUiModel ->
        BannerView(
            uiModel = bannerUiModel,
            onAction = onAction
        )
    }
}
