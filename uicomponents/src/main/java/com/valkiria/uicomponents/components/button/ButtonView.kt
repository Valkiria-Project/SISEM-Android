package com.valkiria.uicomponents.components.button

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.valkiria.uicomponents.props.ButtonSize
import com.valkiria.uicomponents.props.mapToColors
import com.valkiria.uicomponents.props.mapToTextColor
import com.valkiria.uicomponents.props.toTextStyle

@Suppress("UnusedPrivateMember")
@Composable
fun ButtonView(
    uiModel: ButtonUiModel,
    isTablet: Boolean = false,
    onClick: () -> Unit
) {
    Button(
        onClick = { onClick() },
        colors = uiModel.style.mapToColors(),
        modifier = if (uiModel.size == ButtonSize.FULL_WIDTH) {
            uiModel.modifier.fillMaxWidth()
        } else {
            uiModel.modifier
        }
    ) {
        Text(
            text = uiModel.label,
            color = uiModel.style.mapToTextColor(),
            style = uiModel.textStyle.toTextStyle()
        )
    }
}
