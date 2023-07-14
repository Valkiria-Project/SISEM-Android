package com.valkiria.uicomponents.props.button

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.valkiria.uicomponents.props.MarginsUiModel

@Composable
fun ButtonComponent(
    model: ButtonUiModel,
    modifier: Modifier = Modifier
) {
    when (model.style) {
        ButtonStyle.LOUD -> ButtonLoudComponent(model, modifier)
        ButtonStyle.TRANSPARENT -> ButtonTransparentComponent(model, modifier)
    }
}

@Composable
private fun ButtonLoudComponent(
    model: ButtonUiModel,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = { },
        colors = ButtonDefaults.buttonColors(
            containerColor = Color(0xFF42a4fa)
        ),
        modifier = modifier.fillMaxWidth()
    ) {
        Text(text = model.label)
    }
}

@Composable
private fun ButtonTransparentComponent(
    model: ButtonUiModel,
    modifier: Modifier = Modifier
) {
    TextButton(
        onClick = {  },
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Transparent
        ),
        modifier = modifier.fillMaxWidth()
    ) {
        Text(text = model.label)
    }
}

@Preview(showBackground = true)
@Composable
fun ButtonLoudComponentPreview() {
    ButtonLoudComponent(
        model = ButtonUiModel(
            label = "Button",
            style = ButtonStyle.LOUD,
            onClick = OnClick.LOGIN,
            size = ButtonSize.DEFAULT,
            margins = MarginsUiModel(
                top = 8,
                bottom = 8,
                right = 8,
                left = 8
            )
        )
    )
}

@Preview(showBackground = true)
@Composable
fun ButtonTransparentComponentPreview() {
    ButtonTransparentComponent(
        model = ButtonUiModel(
            label = "Button",
            style = ButtonStyle.TRANSPARENT,
            onClick = OnClick.FORGOT_PASSWORD,
            size = ButtonSize.FULL_WIDTH,
            margins = MarginsUiModel(
                top = 8,
                bottom = 8,
                right = 8,
                left = 8
            )
        )
    )
}
