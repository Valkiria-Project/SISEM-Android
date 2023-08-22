package com.valkiria.uicomponents.components.button

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
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
        uiModel.iconResId?.let { iconResId ->
            Icon(
                painter = painterResource(id = iconResId),
                contentDescription = null,
                tint = uiModel.style.mapToTextColor()
            )
        }

        uiModel.label?.let { text ->
            Text(
                text = text,
                color = uiModel.style.mapToTextColor(),
                style = uiModel.textStyle.toTextStyle()
            )
        }
    }
}
