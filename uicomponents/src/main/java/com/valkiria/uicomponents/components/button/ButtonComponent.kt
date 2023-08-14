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
import com.valkiria.uicomponents.mocks.getLoginButtonUiModel
import com.valkiria.uicomponents.mocks.getLoginForgotButtonUiModel
import com.valkiria.uicomponents.props.ButtonSize
import com.valkiria.uicomponents.props.TabletWidth
import com.valkiria.uicomponents.props.mapToColors
import com.valkiria.uicomponents.props.mapToTextColor
import com.valkiria.uicomponents.props.toTextStyle
import timber.log.Timber

@Suppress("MaxLineLength")
@Composable
fun ButtonComponent(
    uiModel: ButtonUiModel,
    isTablet: Boolean = false,
    // BACKEND: This should be part of the ButtonUiModel <--> ButtonResponse
    arrangement: Arrangement.Horizontal = Arrangement.Center,
    onAction: () -> Unit
) {
    Row(
        modifier = if (isTablet) {
            uiModel.modifier.width(TabletWidth)
        } else {
            uiModel.modifier.fillMaxWidth()
        },
        horizontalArrangement = arrangement
    ) {
        Button(
            onClick = {
                onAction()
            },
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
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.DarkGray)
    ) {
        ButtonComponent(uiModel = getLoginForgotButtonUiModel()) {
            Timber.d("Button clicked")
        }
        ButtonComponent(uiModel = getLoginButtonUiModel()) {
            Timber.d("Button clicked")
        }
    }
}
