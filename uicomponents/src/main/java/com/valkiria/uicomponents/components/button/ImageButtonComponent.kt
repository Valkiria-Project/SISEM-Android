package com.valkiria.uicomponents.components.button

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.valkiria.uicomponents.components.label.TextStyle
import com.valkiria.uicomponents.components.label.TextUiModel
import com.valkiria.uicomponents.components.label.toTextStyle
import com.valkiria.uicomponents.utlis.DefType
import com.valkiria.uicomponents.utlis.getResourceIdByName

@Composable
fun ImageButtonComponent(
    uiModel: ImageButtonUiModel,
    onAction: (id: String) -> Unit
) {
    Row(
        modifier = uiModel.modifier.fillMaxWidth(),
        horizontalArrangement = uiModel.arrangement,
    ) {
        ImageButtonView(uiModel = uiModel, onAction = onAction)
    }
}

@Suppress("MagicNumber")
@Composable
fun ImageButtonView(
    uiModel: ImageButtonUiModel,
    isError: Boolean = false,
    onAction: (id: String) -> Unit
) {
    val iconResourceId = LocalContext.current.getResourceIdByName(
        uiModel.image, DefType.DRAWABLE
    )

    val tint = if (uiModel.selected) {
        Color(0xFF3D3F42)
    } else {
        MaterialTheme.colorScheme.primary
    }

    val textColor = if (uiModel.selected) {
        Color(0xFF3D3F42)
    } else {
        Color.White
    }

    Column(
        modifier = uiModel.modifier
            .clickable {
                onAction(uiModel.identifier)
            }
            .clip(RoundedCornerShape(25.dp))
            .background(
                color = if (uiModel.selected) {
                    MaterialTheme.colorScheme.primary
                } else {
                    Color(0xFF3D3F42)
                }
            ),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        iconResourceId?.let {
            Icon(
                modifier = Modifier.padding(vertical = 12.dp),
                painter = painterResource(it),
                contentDescription = null,
                tint = if (isError) {
                    MaterialTheme.colorScheme.error
                } else {
                    tint
                }
            )
        }

        uiModel.title?.let {
            Text(
                modifier = Modifier.padding(start = 12.dp, end = 12.dp, bottom = 12.dp),
                text = it.text,
                textAlign = TextAlign.Center,
                style = it.textStyle.toTextStyle(),
                color = if (isError) {
                    MaterialTheme.colorScheme.error
                } else {
                    textColor
                }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ImageButtonComponentPreview() {
    val uiModel = ImageButtonUiModel(
        identifier = "1",
        title = TextUiModel(
            text = "Normal",
            textStyle = TextStyle.HEADLINE_2
        ),
        image = "ic_anisocoria_left_eye",
        selected = false,
        arrangement = Arrangement.Center,
        modifier = Modifier
    )
    ImageButtonComponent(uiModel = uiModel) { }
}
