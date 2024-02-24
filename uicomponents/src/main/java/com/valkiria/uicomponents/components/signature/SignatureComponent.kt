package com.valkiria.uicomponents.components.signature

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.valkiria.uicomponents.components.button.ButtonComponent
import com.valkiria.uicomponents.components.button.ButtonSize
import com.valkiria.uicomponents.components.button.ButtonStyle
import com.valkiria.uicomponents.components.button.ButtonUiModel
import com.valkiria.uicomponents.components.button.OnClick
import com.valkiria.uicomponents.components.label.LabelComponent
import com.valkiria.uicomponents.components.label.LabelUiModel
import com.valkiria.uicomponents.components.label.TextStyle
import com.valkiria.uicomponents.components.label.TextUiModel
import com.valkiria.uicomponents.extensions.decodeAsBase64Bitmap
import timber.log.Timber

@Composable
fun SignatureComponent(
    uiModel: SignatureUiModel,
    onAction: (id: String) -> Unit
) {
    if (uiModel.signature.isNullOrBlank()) {
        LabelComponent(
            uiModel = LabelUiModel(
                identifier = uiModel.identifier.plus(uiModel.signatureLabel.text),
                text = uiModel.signatureLabel.text,
                textStyle = uiModel.signatureLabel.textStyle,
            )
        )

        ButtonComponent(uiModel = uiModel.signatureButton) {
            onAction(uiModel.identifier)
        }
    } else {
        Image(
            bitmap = uiModel.signature
                .decodeAsBase64Bitmap()
                .asImageBitmap(), // FIXME: Temporal hack
            contentDescription = uiModel.signature,
            contentScale = ContentScale.Crop,
            alpha = 1f,
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp)
        )
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
