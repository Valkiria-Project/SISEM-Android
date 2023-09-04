package com.valkiria.uicomponents.components.loader

import HideKeyboard
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun OnLoadingHandler(
    isLoading: Boolean,
    modifier: Modifier
) {
    if (isLoading) {
        HideKeyboard()
        LoaderComponent(modifier)
    }
}
