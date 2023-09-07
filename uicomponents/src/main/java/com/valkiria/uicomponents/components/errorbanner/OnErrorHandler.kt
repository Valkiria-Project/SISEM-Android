package com.valkiria.uicomponents.components.errorbanner

import androidx.compose.runtime.Composable
import com.valkiria.uicomponents.action.UiAction
import com.valkiria.uicomponents.model.ui.errorbanner.ErrorUiModel

@Composable
fun OnErrorHandler(
    errorModel: ErrorUiModel?,
    onAction: () -> Unit,
    onFooterAction: (actionInput: UiAction) -> Unit = {}
) {
    errorModel?.let { errorUiModel ->
        ErrorBannerComponent(
            uiModel = errorUiModel,
            onAction = onAction,
            onFooterAction = { uiAction ->
                onFooterAction(uiAction)
            }
        )
    }
}
