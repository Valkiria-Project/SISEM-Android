package com.valkiria.uicomponents.bricks.button

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import com.valkiria.uicomponents.components.label.toTextStyle

@Composable
fun ImageButtonView(
    uiModel: ImageButtonUiModel,
    onAction: (id: String) -> Unit
) {
    Column(
        modifier = uiModel.modifier.clickable { onAction(uiModel.identifier) },
        horizontalAlignment = uiModel.alignment
    ) {
        Image(
            imageVector = ImageVector.vectorResource(id = uiModel.iconResId),
            contentDescription = null,
            alignment = Alignment.Center,
            colorFilter = ColorFilter.tint(Color.White)
        )

        uiModel.label?.let {
            Text(
                text = uiModel.label,
                textAlign = TextAlign.Center,
                style = uiModel.textStyle.toTextStyle()
            )
        }
    }
}
