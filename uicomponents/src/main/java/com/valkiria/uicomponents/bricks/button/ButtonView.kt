package com.valkiria.uicomponents.bricks.button

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.valkiria.uicomponents.model.props.ButtonSize
import com.valkiria.uicomponents.model.props.mapToColors
import com.valkiria.uicomponents.model.props.mapToTextColor
import com.valkiria.uicomponents.model.props.toTextStyle
import com.valkiria.uicomponents.model.ui.body.ButtonUiModel

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
