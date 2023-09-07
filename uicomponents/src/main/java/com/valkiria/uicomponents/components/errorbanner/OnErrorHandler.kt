package com.valkiria.uicomponents.components.errorbanner

import androidx.compose.runtime.Composable
import com.valkiria.uicomponents.action.UiAction
import com.valkiria.uicomponents.model.ui.banner.BannerUiModel

@Composable
fun OnErrorHandler(
    errorModel: BannerUiModel?,
    onAction: (actionInput: UiAction) -> Unit
) {
    errorModel?.let { errorUiModel ->
        BannerComponent(
            uiModel = errorUiModel,
            onAction = onAction
        )
    }
}
