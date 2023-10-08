package com.valkiria.uicomponents.components.banner

import androidx.compose.runtime.Composable
import com.valkiria.uicomponents.action.UiAction

@Composable
fun OnBannerHandler(
    uiModel: BannerUiModel?,
    onAction: (actionInput: UiAction) -> Unit
) {
    uiModel?.let { bannerUiModel ->
        BannerComponent(
            uiModel = bannerUiModel,
            onAction = onAction
        )
    }
}
