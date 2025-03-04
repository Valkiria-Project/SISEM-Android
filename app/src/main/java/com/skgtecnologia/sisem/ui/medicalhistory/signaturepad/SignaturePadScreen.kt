package com.skgtecnologia.sisem.ui.medicalhistory.signaturepad

import android.graphics.Bitmap
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.skgtecnologia.sisem.R.string
import com.skgtecnologia.sisem.commons.communication.NotificationEventHandler
import com.skgtecnologia.sisem.domain.medicalhistory.model.MedicalHistoryIdentifier
import com.skgtecnologia.sisem.domain.model.header.signaturePadHeader
import com.skgtecnologia.sisem.ui.sections.HeaderSection
import com.valkiria.uicomponents.action.HeaderUiAction
import com.valkiria.uicomponents.bricks.button.ButtonView
import com.valkiria.uicomponents.bricks.notification.OnNotificationHandler
import com.valkiria.uicomponents.bricks.notification.model.NotificationData
import com.valkiria.uicomponents.bricks.signature.ComposeSignature
import com.valkiria.uicomponents.components.button.ButtonSize
import com.valkiria.uicomponents.components.button.ButtonStyle
import com.valkiria.uicomponents.components.button.ButtonUiModel
import com.valkiria.uicomponents.components.button.OnClick
import com.valkiria.uicomponents.components.label.TextStyle
import com.valkiria.uicomponents.extensions.encodeAsBase64
import timber.log.Timber

@Suppress("LongMethod")
@Composable
fun SignaturePadScreen(
    modifier: Modifier = Modifier,
    onNavigation: (signaturePadNavigationModel: SignaturePadNavigationModel) -> Unit
) {
    var notificationData by remember { mutableStateOf<NotificationData?>(null) }
    NotificationEventHandler.subscribeNotificationEvent {
        notificationData = it
    }

    Column(modifier.fillMaxSize()) {
        var bitmap: Bitmap? by remember {
            mutableStateOf(null)
        }

        // TECH-DEBT: The resources should be dynamic
        HeaderSection(
            headerUiModel = signaturePadHeader(
                titleText = stringResource(id = string.signature_pad_title),
                subtitleText = stringResource(id = string.signature_pad_subtitle),
                leftIcon = stringResource(id = string.back_icon)
            )
        ) { uiAction ->
            if (uiAction is HeaderUiAction.GoBack) {
                onNavigation(
                    SignaturePadNavigationModel(goBack = true)
                )
            }
        }

        ComposeSignature(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            signaturePadColor = MaterialTheme.colorScheme.background,
            signatureColor = MaterialTheme.colorScheme.onBackground,
            signatureThickness = 10f,
            completeComponent = { onClick ->
                ButtonView(
                    uiModel = ButtonUiModel(
                        identifier = MedicalHistoryIdentifier.SIGNATURE_PAD_SAVE_BUTTON.name,
                        label = stringResource(id = string.save_cta),
                        style = ButtonStyle.LOUD,
                        textStyle = TextStyle.HEADLINE_5,
                        onClick = OnClick.DISMISS,
                        size = ButtonSize.DEFAULT,
                        arrangement = Arrangement.Center,
                        modifier = Modifier.padding(
                            start = 0.dp,
                            top = 20.dp,
                            end = 0.dp,
                            bottom = 0.dp
                        )
                    )
                ) {
                    onClick()
                }
            },
            clearComponent = { onClick ->
                ButtonView(
                    uiModel = ButtonUiModel(
                        identifier = MedicalHistoryIdentifier.SIGNATURE_PAD_ERASE_BUTTON.name,
                        label = stringResource(id = string.erase_cta),
                        style = ButtonStyle.LOUD,
                        textStyle = TextStyle.HEADLINE_5,
                        onClick = OnClick.DISMISS,
                        size = ButtonSize.DEFAULT,
                        arrangement = Arrangement.Center,
                        modifier = Modifier.padding(
                            start = 0.dp,
                            top = 20.dp,
                            end = 0.dp,
                            bottom = 0.dp
                        )
                    )
                ) {
                    onClick()
                }
            },
            onComplete = { signatureBitmap ->
                if (signatureBitmap != null) {
                    bitmap = signatureBitmap
                    onNavigation(
                        SignaturePadNavigationModel(
                            signature = signatureBitmap.encodeAsBase64()
                        )
                    )
                } else {
                    Timber.d("Signature is null")
                }
            },
            onClear = {
                bitmap = null
            }
        )
    }

    OnNotificationHandler(notificationData) {
        notificationData = null
        if (it.isDismiss.not()) {
            // TECH-DEBT: Navigate to MapScreen if is type INCIDENT_ASSIGNED
            Timber.d("Navigate to MapScreen")
        }
    }
}
