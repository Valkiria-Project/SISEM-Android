package com.valkiria.uicomponents.components.button

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.valkiria.uicomponents.components.label.toTextStyle
import com.valkiria.uicomponents.mocks.getLoginButtonUiModel
import com.valkiria.uicomponents.mocks.getLoginForgotButtonUiModel
import com.valkiria.uicomponents.utlis.DefType
import com.valkiria.uicomponents.utlis.getResourceIdByName
import timber.log.Timber

@Composable
fun ButtonComponent(
    uiModel: ButtonUiModel,
    onAction: (id: String) -> Unit
) {
    val iconResourceId = LocalContext.current.getResourceIdByName(
        uiModel.leftIcon.orEmpty(), DefType.DRAWABLE
    )

    Row(
        modifier = uiModel.modifier.fillMaxWidth(),
        horizontalArrangement = uiModel.arrangement
    ) {
        Button(
            onClick = {
                onAction(uiModel.identifier)
            },
            colors = uiModel.style.mapToColors(),
            modifier = if (uiModel.size == ButtonSize.FULL_WIDTH) {
                Modifier.fillMaxWidth()
            } else {
                Modifier
            }
        ) {
            iconResourceId?.let {
                Icon(
                    imageVector = ImageVector.vectorResource(id = it),
                    contentDescription = null,
                    modifier = Modifier
                        .padding(end = 8.dp)
                        .size(24.dp),
                    tint = Color.Black
                )
            }

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
