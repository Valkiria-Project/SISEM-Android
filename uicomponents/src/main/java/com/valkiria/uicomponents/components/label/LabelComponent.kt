package com.valkiria.uicomponents.components.label

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.valkiria.uicomponents.props.TextStyle
import com.valkiria.uicomponents.theme.UiComponentsTheme

// TODO: Finish this component
@Suppress("ForbiddenComment")
@Composable
fun LabelComponent(
    uiModel: LabelUiModel,
    modifier: Modifier = Modifier
) {
    Text(
        text = uiModel.text,
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun LabelComponentPreview() {
    UiComponentsTheme {
        /*
    {
        "text": "Serial dispositivo",
        "text_style": "BUTTON_1",
        "type": "LABEL",
        "margins": {"top": 30,"left": 20,"right": 0,"bottom": 0}
    }
    */
        val labelUiModel = LabelUiModel(
            text = "Serial dispositivo",
            textStyle = TextStyle.BUTTON_1,
            modifier = Modifier
        )
        Column {
            LabelComponent(
                uiModel = labelUiModel,
                modifier = Modifier.padding(16.dp)
            )
            LabelComponent(
                uiModel = labelUiModel,
                modifier = Modifier.padding(0.dp)
            )
        }
    }
}
