package com.valkiria.uicomponents.components.bottomsheet

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.valkiria.uicomponents.R.drawable
import com.valkiria.uicomponents.props.TextStyle
import com.valkiria.uicomponents.theme.UiComponentsTheme

@Composable
fun BottomSheetComponent(
    uiModel: BottomSheetUiModel,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxSize(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = uiModel.text
        )
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
