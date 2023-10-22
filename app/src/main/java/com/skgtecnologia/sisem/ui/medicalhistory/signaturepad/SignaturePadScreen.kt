package com.skgtecnologia.sisem.ui.medicalhistory.signaturepad

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.ui.unit.dp
import com.joelkanyi.composesignature.ComposeSignature
import com.skgtecnologia.sisem.ui.navigation.model.NavigationModel

@Composable
fun SignaturePadScreen(
    modifier: Modifier = Modifier,
    onNavigation: (medicineNavigationModel: NavigationModel?) -> Unit
) {
    Column(modifier.fillMaxSize()) {
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
            },
            onClear = {
                imageBitmap = null
            }
        )
    }
}
