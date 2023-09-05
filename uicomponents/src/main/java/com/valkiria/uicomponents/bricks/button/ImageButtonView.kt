package com.valkiria.uicomponents.bricks.button

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import com.valkiria.uicomponents.components.label.LabelComponent
import com.valkiria.uicomponents.components.label.LabelUiModel
import com.valkiria.uicomponents.model.props.TextStyle
import com.valkiria.uicomponents.model.ui.button.ImageButtonUiModel

@Composable
fun ImageButtonView(
    uiModel: ImageButtonUiModel,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier.clickable { onClick() },
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = uiModel.iconResId),
            contentDescription = null,
            alignment = Alignment.Center,
            colorFilter = ColorFilter.tint(Color.White)
        )

        uiModel.label?.let {
            LabelComponent(
                uiModel = getImageButtonLabelModel(uiModel.label),
            )
        }
    }
}

private fun getImageButtonLabelModel(label: String) = LabelUiModel(
    text = label,
    textStyle = TextStyle.HEADLINE_4,
    arrangement = Arrangement.Center
)
