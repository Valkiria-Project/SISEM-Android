package com.valkiria.uicomponents.bricks.loader

import HideKeyboard
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun OnLoadingHandler(
    isLoading: Boolean,
    modifier: Modifier = Modifier
) {
    if (isLoading) {
        HideKeyboard()
        LoaderView(modifier)
    }
}
