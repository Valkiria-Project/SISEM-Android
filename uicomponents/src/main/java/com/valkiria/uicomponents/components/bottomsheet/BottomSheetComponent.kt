package com.valkiria.uicomponents.components.bottomsheet

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.Button
import androidx.compose.material3.SheetValue.Hidden
import androidx.compose.material3.Text
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.material3.rememberStandardBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.valkiria.uicomponents.R.drawable
import com.valkiria.uicomponents.props.TextStyle
import com.valkiria.uicomponents.theme.UiComponentsTheme
import kotlinx.coroutines.launch

@Composable
fun BottomSheetComponent(
    uiModel: BottomSheetUiModel,
    modifier: Modifier = Modifier
) {
    val scope = rememberCoroutineScope()
    val scaffoldState = rememberBottomSheetScaffoldState()
    BottomSheetScaffold(
        scaffoldState = scaffoldState,
        sheetPeekHeight = 128.dp,
        sheetContent = {
            Box(
                Modifier
                    .fillMaxWidth()
                    .height(128.dp),
                contentAlignment = Alignment.Center
            ) {
                Text("Swipe up to expand sheet")
            }
            Column(
                Modifier
                    .fillMaxWidth()
                    .padding(64.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("Sheet content")
                Spacer(Modifier.height(20.dp))
                Button(
                    onClick = {
                        scope.launch {
                            scaffoldState.bottomSheetState.partialExpand()
                        }
                    }
                ) {
                    Text("Click to collapse sheet")
                }
            }
        }
    ) { innerPadding ->
        Box(Modifier.padding(innerPadding)) {
            Text("Scaffold Content")
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
            BottomSheetComponent(
                BottomSheetUiModel(
                    icon = painterResource(id = drawable.ic_message),
                    title = "Title",
                    titleTextStyle = TextStyle.HEADLINE_2,
                    text = "Text",
                    textStyle = TextStyle.BODY_1
                )
            )
        }
    }
}
