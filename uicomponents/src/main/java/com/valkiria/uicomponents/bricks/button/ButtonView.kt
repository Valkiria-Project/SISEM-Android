package com.valkiria.uicomponents.bricks.button

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.valkiria.uicomponents.components.button.ButtonSize
import com.valkiria.uicomponents.components.button.mapToColors
import com.valkiria.uicomponents.components.button.mapToTextColor
import com.valkiria.uicomponents.components.label.toTextStyle
import com.valkiria.uicomponents.components.button.ButtonUiModel

@Suppress("UnusedPrivateMember")
@Composable
fun ButtonView(
    uiModel: ButtonUiModel,
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
            color = uiModel.style.mapToTextColor(uiModel.overrideColor),
            style = uiModel.textStyle.toTextStyle()
        )
    }
}
