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
import androidx.compose.foundation.shape.RoundedCornerShape
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

private const val CHIP_CORNER_RADIUS = 12
private const val CHIP_ALPHA = 0.35f
private const val FACE_CIRCLE_SIZE = 180
private const val FACE_ICON_SIZE = 96

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
            fontSize = 15.sp,
            modifier = Modifier.padding(top = 12.dp)
        )

        Spacer(modifier = Modifier.height(24.dp))

        CrewMemberEnrollment(
            crewMember = uiState.crewMember,
            document = uiState.document,
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
    onEnroll: () -> Unit
) {
    if (crewMember != null) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            InfoChip(text = crewMember.name, modifier = Modifier.weight(1f))
            InfoChip(text = crewMember.role)
        }

        Spacer(modifier = Modifier.height(12.dp))
    }

    InfoChip(text = crewMember?.document ?: document, modifier = Modifier.fillMaxWidth())

    Spacer(modifier = Modifier.height(40.dp))

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column(
            modifier = Modifier
                .size(FACE_CIRCLE_SIZE.dp)
                .background(
                    color = MaterialTheme.colorScheme.primary,
                    shape = CircleShape
                )
                .clickable { onEnroll() },
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

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = stringResource(R.string.biometric_enrollment_face_title),
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
            color = MaterialTheme.colorScheme.onSurface,
            textAlign = TextAlign.Center
        )

        Text(
            text = if (crewMember?.isEnrolled == true) {
                stringResource(R.string.biometric_enrollment_enrolled)
            } else {
                stringResource(R.string.biometric_enrollment_face_description)
            },
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = 8.dp, start = 24.dp, end = 24.dp)
        )
    }
}

@Composable
private fun InfoChip(
    text: String,
    modifier: Modifier = Modifier
) {
    Text(
        text = text,
        style = MaterialTheme.typography.bodyMedium,
        color = MaterialTheme.colorScheme.onSurface,
        modifier = modifier
            .background(
                color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = CHIP_ALPHA),
                shape = RoundedCornerShape(CHIP_CORNER_RADIUS.dp)
            )
            .padding(horizontal = 16.dp, vertical = 12.dp)
    )
}
