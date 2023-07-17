package com.valkiria.uicomponents.components.bottomsheet

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.valkiria.uicomponents.R.drawable
import com.valkiria.uicomponents.props.TextStyle
import com.valkiria.uicomponents.props.toTextStyle
import com.valkiria.uicomponents.theme.UiComponentsTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun BottomSheetComponent(
    uiModel: BottomSheetUiModel,
    modifier: Modifier = Modifier,
    sheetState: SheetState = rememberModalBottomSheetState(),
    scope: CoroutineScope = rememberCoroutineScope()
) {
    if (sheetState.isVisible) {
        ModalBottomSheet(
            sheetState = sheetState,
            onDismissRequest = {
                scope.launch {
                    sheetState.hide()
                }
            },
        ) {
            Row(
                modifier = Modifier.padding(bottom = 20.dp),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                uiModel.icon?.let {
                    Icon(
                        painter = it,
                        contentDescription = null,
                        modifier = Modifier
                            .padding(start = 20.dp, end = 16.dp)
                            .size(42.dp),
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
                Text(
                    text = uiModel.title,
                    style = uiModel.titleTextStyle.toTextStyle()
                )
//                CenterAlignedTopAppBar(
//                    navigationIcon = {
//                        IconButton(
//                            onClick = {
//                                scope.launch {
//                                    sheetState.hide()
//                                }
//                            }
//                        ) {
//                            Icon(Icons.Rounded.Close, contentDescription = "Cancel")
//                        }
//                    },
//                    title = {
//                        Text("Content")
//                    },
//                    actions = {
//                        IconButton(
//                            onClick = { })
//                        {
//                            Icon(Icons.Rounded.Check, contentDescription = "Save")
//                        }
//                    })
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
            val sheetState = rememberModalBottomSheetState()
            val scope = rememberCoroutineScope()
            scope.launch {
                sheetState.show()
            }

            BottomSheetComponent(
                uiModel = BottomSheetUiModel(
                    icon = painterResource(id = drawable.ic_message),
                    title = "Title",
                    titleTextStyle = TextStyle.HEADLINE_2,
                    text = "Text",
                    textStyle = TextStyle.BODY_1
                ),
                sheetState = sheetState,
                scope = scope
            )
        }
    }
}
