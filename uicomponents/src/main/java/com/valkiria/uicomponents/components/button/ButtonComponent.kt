package com.valkiria.uicomponents.components.button

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.valkiria.uicomponents.mocks.getLoginForgotButtonUiModel
import com.valkiria.uicomponents.props.mapToColors
import com.valkiria.uicomponents.props.mapToTextColor
import com.valkiria.uicomponents.props.toTextStyle
import com.valkiria.uicomponents.theme.UiComponentsTheme

@Suppress("MaxLineLength")
@Composable
fun ButtonComponent(
    uiModel: ButtonUiModel,
    arrangement: Arrangement.Horizontal = Arrangement.Center // BACKEND: This should be part of the ButtonUiModel <--> ButtonResponse
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = arrangement
    ) {
        Button(
            onClick = { },
            colors = uiModel.style.mapToColors(),
            modifier = uiModel.modifier
        ) {
            Text(
                text = uiModel.label,
                color = uiModel.style.mapToTextColor(),
                style = uiModel.textStyle.toTextStyle()
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ButtonComponentPreview() {
    UiComponentsTheme {
        Column(
            modifier = Modifier.background(Color.DarkGray)
        ) {
            ButtonComponent(uiModel = getLoginForgotButtonUiModel())
        }
    }
}
