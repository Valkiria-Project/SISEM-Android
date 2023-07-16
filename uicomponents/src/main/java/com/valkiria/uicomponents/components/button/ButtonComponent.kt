package com.valkiria.uicomponents.components.button

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.valkiria.uicomponents.mocks.getLoginButtonUiModel
import com.valkiria.uicomponents.mocks.getLoginForgotButtonUiModel
import com.valkiria.uicomponents.props.ButtonSize
import com.valkiria.uicomponents.props.mapToColors
import com.valkiria.uicomponents.props.mapToTextColor
import com.valkiria.uicomponents.props.toTextStyle
import com.valkiria.uicomponents.theme.UiComponentsTheme

@Suppress("MaxLineLength")
@Composable
fun ButtonComponent(
    uiModel: ButtonUiModel,
    isTablet: Boolean = false,
    arrangement: Arrangement.Horizontal = Arrangement.Center // BACKEND: This should be part of the ButtonUiModel <--> ButtonResponse
) {
    Row(
        modifier = if (isTablet) {
            uiModel.modifier.width(300.dp)
        } else {
            uiModel.modifier.fillMaxWidth()
        },
        horizontalArrangement = arrangement
    ) {
        Button(
            onClick = { },
            colors = uiModel.style.mapToColors(),
            modifier = if (uiModel.size == ButtonSize.FULL_WIDTH) {
                Modifier.fillMaxWidth()
            } else {
                Modifier
            }
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
            modifier = Modifier
                .fillMaxSize()
                .background(Color.DarkGray)
        ) {
            ButtonComponent(uiModel = getLoginForgotButtonUiModel())
            ButtonComponent(uiModel = getLoginButtonUiModel())
        }
    }
}
