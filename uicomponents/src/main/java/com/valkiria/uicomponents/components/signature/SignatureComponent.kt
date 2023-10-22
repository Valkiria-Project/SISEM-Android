package com.valkiria.uicomponents.components.signature

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.valkiria.uicomponents.components.button.ButtonComponent
import com.valkiria.uicomponents.components.button.ButtonSize
import com.valkiria.uicomponents.components.button.ButtonStyle
import com.valkiria.uicomponents.components.button.ButtonUiModel
import com.valkiria.uicomponents.components.button.OnClick
import com.valkiria.uicomponents.components.label.TextStyle
import com.valkiria.uicomponents.components.label.TextUiModel
import timber.log.Timber

@Composable
fun SignatureComponent(
    uiModel: SignatureUiModel,
    onAction: (id: String) -> Unit
) {
    ButtonComponent(uiModel = uiModel.signatureButton) {
        onAction(uiModel.identifier)
    }
}

@Preview(showBackground = true)
@Composable
fun SignatureComponentPreview() {
    SignatureComponent(
        uiModel = SignatureUiModel(
            identifier = "RESPONSIBLE_SIGNATURE",
            signatureLabel = TextUiModel(
                text = "Firma del responsable",
                textStyle = TextStyle.HEADLINE_5
            ),
            signatureButton = ButtonUiModel(
                identifier = "RESPONSIBLE_SIGNATURE",
                label = "FIRMAR",
                leftIcon = "ic_edit",
                style = ButtonStyle.LOUD,
                textStyle = TextStyle.HEADLINE_5,
                onClick = OnClick.DISMISS,
                size = ButtonSize.FULL_WIDTH,
                arrangement = Arrangement.Center,
                modifier = Modifier.padding(
                    start = 0.dp,
                    top = 20.dp,
                    end = 0.dp,
                    bottom = 0.dp
                )
            ),
            modifier = Modifier
        ),
        onAction = { id ->
            Timber.d("Signature identifier is: $id")
        }
    )
}
