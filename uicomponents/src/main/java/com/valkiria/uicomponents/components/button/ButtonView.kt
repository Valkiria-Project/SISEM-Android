package com.valkiria.uicomponents.components.button

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.valkiria.uicomponents.props.ButtonSize
import com.valkiria.uicomponents.props.mapToColors
import com.valkiria.uicomponents.props.mapToTextColor
import com.valkiria.uicomponents.props.toTextStyle

@Suppress("UnusedPrivateMember")
@Composable
fun ButtonView(
    uiModel: ButtonUiModel,
    isTablet: Boolean,
    onClick: () -> Unit
) {
    Button(
        onClick = { onClick() },
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
