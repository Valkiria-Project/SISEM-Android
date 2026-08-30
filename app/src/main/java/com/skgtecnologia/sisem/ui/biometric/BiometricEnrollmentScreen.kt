package com.skgtecnologia.sisem.ui.biometric

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.skgtecnologia.sisem.R
import com.valkiria.uicomponents.bricks.banner.OnBannerHandler
import com.valkiria.uicomponents.bricks.loader.OnLoadingHandler
import com.valkiria.uicomponents.R as UiR

private const val FACE_CIRCLE_SIZE = 180
private const val FACE_ICON_SIZE = 96
private const val FIELD_SPACING = 20

@Composable
fun BiometricEnrollmentScreen(
    modifier: Modifier = Modifier,
    viewModel: BiometricEnrollmentViewModel = hiltViewModel(),
    onEnroll: () -> Unit,
    onBack: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(20.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = ImageVector.vectorResource(id = UiR.drawable.ic_back),
                contentDescription = null,
                modifier = Modifier
                    .clickable(onClick = onBack)
                    .padding(end = 12.dp)
                    .size(24.dp),
                tint = MaterialTheme.colorScheme.primary
            )
            Text(
                text = stringResource(R.string.biometric_enrollment_title),
                style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onSurface,
                fontSize = 24.sp
            )
        }
        Text(
            text = stringResource(R.string.biometric_enrollment_subtitle),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            fontWeight = FontWeight.Normal,
            fontSize = 15.sp,
            modifier = Modifier.padding(top = 20.dp)
        )

        Spacer(modifier = Modifier.height(24.dp))

        CrewMemberEnrollment(
            crewMember = uiState.crewMember,
            document = uiState.document,
            isRegistered = uiState.isRegistered,
            onEnroll = onEnroll
        )
    }

    OnBannerHandler(uiModel = uiState.errorModel) {
        viewModel.consumeError()
    }
    OnLoadingHandler(uiState.isLoading)
}

@Composable
private fun CrewMemberEnrollment(
    crewMember: CrewBiometricStatus?,
    document: String,
    isRegistered: Boolean,
    onEnroll: () -> Unit
) {
    if (crewMember != null) {
        LabeledValue(
            label = stringResource(R.string.biometric_enrollment_name_label),
            value = crewMember.name
        )

        Spacer(modifier = Modifier.height(FIELD_SPACING.dp))

        LabeledValue(
            label = stringResource(R.string.biometric_enrollment_document_label),
            value = crewMember.document
        )

        Spacer(modifier = Modifier.height(FIELD_SPACING.dp))

        LabeledValue(
            label = stringResource(R.string.biometric_enrollment_role_label),
            value = crewMember.role
        )
    } else {
        LabeledValue(
            label = stringResource(R.string.biometric_enrollment_document_label),
            value = document
        )
    }

    Spacer(modifier = Modifier.height(40.dp))

    BiometricRegistrationAction(
        isRegistered = isRegistered,
        onEnroll = onEnroll
    )
}

@Composable
private fun LabeledValue(
    label: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            fontWeight = FontWeight.Medium,
            fontSize = 13.sp
        )
        Text(
            text = value,
            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
            color = MaterialTheme.colorScheme.onSurface,
            fontWeight = FontWeight.SemiBold,
            fontSize = 16.sp,
            modifier = Modifier.padding(top = 4.dp)
        )
    }
}

@Composable
private fun BiometricRegistrationAction(
    isRegistered: Boolean,
    onEnroll: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        FaceIllustration()

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = if (isRegistered) {
                stringResource(R.string.biometric_enrollment_update)
            } else {
                stringResource(R.string.biometric_enrollment_register)
            },
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
            color = MaterialTheme.colorScheme.onSurface,
            textAlign = TextAlign.Center
        )

        Text(
            text = if (isRegistered) {
                stringResource(R.string.biometric_enrollment_update_description)
            } else {
                stringResource(R.string.biometric_enrollment_face_description)
            },
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = 8.dp, start = 24.dp, end = 24.dp)
        )

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = onEnroll,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = if (isRegistered) {
                    stringResource(R.string.biometric_enrollment_update)
                } else {
                    stringResource(R.string.biometric_enrollment_register)
                },
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onPrimary
            )
        }
    }
}

@Composable
private fun FaceIllustration() {
    Column(
        modifier = Modifier
            .size(FACE_CIRCLE_SIZE.dp)
            .background(
                color = MaterialTheme.colorScheme.primary,
                shape = CircleShape
            ),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = ImageVector.vectorResource(id = UiR.drawable.ic_biometric),
            contentDescription = stringResource(R.string.biometric_enrollment_face_title),
            tint = MaterialTheme.colorScheme.onPrimary,
            modifier = Modifier.size(FACE_ICON_SIZE.dp)
        )
    }
}
