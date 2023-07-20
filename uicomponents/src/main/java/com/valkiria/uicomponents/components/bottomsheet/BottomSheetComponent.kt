package com.valkiria.uicomponents.components.bottomsheet

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.valkiria.uicomponents.mocks.getLoginTermsBottomSheetUiModel
import com.valkiria.uicomponents.props.toTextStyle
import com.valkiria.uicomponents.theme.UiComponentsTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import timber.log.Timber
import androidx.compose.ui.text.TextStyle.Companion as ComposeTextStyle

@Composable
fun BottomSheetComponent(
    uiModel: BottomSheetUiModel,
    modifier: Modifier = Modifier,
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
            Row(
                modifier = Modifier.padding(20.dp),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                uiModel.icon?.let {
                    Icon(
                        painter = it,
                        contentDescription = null,
                        modifier = Modifier
                            .padding(end = 16.dp)
                            .size(42.dp),
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
                Text(
                    text = uiModel.title,
                    style = uiModel.titleTextStyle.toTextStyle()
                )
            }
            uiModel.subtitle?.let {
                Text(
                    text = it,
                    modifier = Modifier.padding(20.dp),
                    style = uiModel.subtitleTextStyle?.toTextStyle() ?: ComposeTextStyle.Default
                )
            }
            Column(
                modifier = Modifier
                    .verticalScroll(rememberScrollState())
                    .fillMaxSize()
            ) {
                Text(
                    text = uiModel.text,
                    modifier = Modifier.padding(20.dp),
                    style = uiModel.textStyle.toTextStyle()
                )
            }
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
            scope.launch {
                sheetState.show()
            }

            BottomSheetComponent(
                uiModel = getLoginTermsBottomSheetUiModel(),
                sheetState = sheetState,
                scope = scope
            ) {
                Timber.d("Dismissed")
            }
        }
    }
}
