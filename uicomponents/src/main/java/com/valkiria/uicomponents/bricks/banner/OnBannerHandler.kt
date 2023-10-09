package com.valkiria.uicomponents.bricks.banner

import androidx.compose.runtime.Composable
import com.valkiria.uicomponents.action.UiAction

@Composable
fun OnBannerHandler(
    uiModel: BannerUiModel?,
    onAction: (actionInput: UiAction) -> Unit
) {
    uiModel?.let { bannerUiModel ->
        BannerView(
            uiModel = bannerUiModel,
            onAction = onAction
        )
    }
}
