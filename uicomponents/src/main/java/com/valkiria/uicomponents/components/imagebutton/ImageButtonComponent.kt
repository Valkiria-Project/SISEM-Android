package com.valkiria.uicomponents.components.imagebutton

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
import com.valkiria.uicomponents.model.props.TextStyle
import com.valkiria.uicomponents.model.props.toTextStyle
import com.valkiria.uicomponents.model.ui.button.ImageButtonUiModel2
import com.valkiria.uicomponents.utlis.DefType
import com.valkiria.uicomponents.utlis.getResourceIdByName

@Composable
fun ImageButtonComponent(
    uiModel: ImageButtonUiModel2,
    onAction: (id: String) -> Unit
) {
    Row(
        modifier = uiModel.modifier.fillMaxWidth(),
        horizontalArrangement = uiModel.arrangement,
    ) {
        ImageButtonView2(uiModel = uiModel, onAction = onAction)
    }
}

@Suppress("MagicNumber")
@Composable
fun ImageButtonView2(
    uiModel: ImageButtonUiModel2,
    onAction: (id: String) -> Unit,
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
                tint = tint
            )
        }

        uiModel.textStyle?.let {
            Text(
                modifier = Modifier.padding(start = 12.dp, end = 12.dp, bottom = 12.dp),
                text = uiModel.title.orEmpty(),
                textAlign = TextAlign.Center,
                style = it.toTextStyle(),
                color = textColor
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ImageButtonComponentPreview() {
    val uiModel = ImageButtonUiModel2(
        identifier = "1",
        title = "Normal",
        textStyle = TextStyle.HEADLINE_2,
        image = "ic_anisocoria_left_eye",
        selected = false,
        arrangement = Arrangement.Center,
        modifier = Modifier
    )
    ImageButtonComponent(uiModel = uiModel) { }
}
