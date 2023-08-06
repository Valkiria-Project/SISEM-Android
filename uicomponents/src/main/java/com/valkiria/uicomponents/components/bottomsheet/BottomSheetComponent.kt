package com.valkiria.uicomponents.components.bottomsheet

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.valkiria.uicomponents.theme.UiComponentsTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import timber.log.Timber

@Composable
fun BottomSheetComponent(
    content: @Composable () -> Unit,
    sheetState: SheetState = rememberModalBottomSheetState(),
    scope: CoroutineScope = rememberCoroutineScope(),
    onAction: () -> Unit
) {
    if (sheetState.isVisible) {
        ModalBottomSheet(
            onDismissRequest = {
                scope.launch {
                    sheetState.hide()
                }
                onAction()
            },
            sheetState = sheetState,
            containerColor = Color.DarkGray,
            scrimColor = MaterialTheme.colorScheme.background.copy(alpha = 0.8f)
        ) {
            content()
        }
    }
}

@Preview(showBackground = true)
@Composable
fun BottomSheetComponentPreview() {
    UiComponentsTheme {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.DarkGray)
        ) {
            val sheetState = rememberModalBottomSheetState(
                skipPartiallyExpanded = true
            )
            val scope = rememberCoroutineScope()

            LaunchedEffect(sheetState) {
                scope.launch {
                    sheetState.show()
                }
            }

            BottomSheetComponent(
                content = {
                    Text(
                        text = """1. INTRODUCCIÓN\n\nLa Política de Seguridad de la Información del 
            |sitio web y términos de uso del Sitio Web de la Secretaría Distrital de 
            |Salud y el Fondo Financiero ...""".trimMargin()
                    )
                },
                sheetState = sheetState,
                scope = scope
            ) {
                Timber.d("Dismissed")
            }
        }
    }
}
