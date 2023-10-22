package com.valkiria.uicomponents.components.signature

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.joelkanyi.composesignature.ComposeSignature
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
    onAction: () -> Unit
) {
    var signing by remember { mutableStateOf(false) }

    ButtonComponent(uiModel = uiModel.signatureButton) {
        signing = true
    }

    if (signing) {
        ComposeSignatureView {
            signing = false
        }
    }
}

@Composable
private fun ComposeSignatureView(signingCompleted: () -> Unit) {
    var imageBitmap: ImageBitmap? by remember {
        mutableStateOf(null)
    }

    ComposeSignature(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        signaturePadColor = Color(0xFFEEEEEE),
        signaturePadHeight = 400.dp,
        signatureColor = Color.Black,
        signatureThickness = 10f,
        onComplete = { signatureBitmap ->
            imageBitmap = signatureBitmap.asImageBitmap()
            signingCompleted()
        },
        onClear = {
            imageBitmap = null
        }
    )
}

@Preview(showBackground = true)
@Composable
fun SignatureComponentPreview() {
    SignatureComponent(
        uiModel = SignatureUiModel(
            identifier = "slider",
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
        onAction = {
            Timber.d("Signature is: ")
        }
    )
}
