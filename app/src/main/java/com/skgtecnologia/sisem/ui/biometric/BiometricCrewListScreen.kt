package com.skgtecnologia.sisem.ui.biometric

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.skgtecnologia.sisem.R
import com.valkiria.uicomponents.bricks.banner.OnBannerHandler
import com.valkiria.uicomponents.bricks.loader.OnLoadingHandler
import com.valkiria.uicomponents.extensions.shadow
import com.valkiria.uicomponents.utlis.DefType
import com.valkiria.uicomponents.utlis.getResourceIdByName

private const val GREEN_HEX = 0xFF4CAF50
private const val ORANGE_HEX = 0xFFFF9800
private const val CARD_RADIUS = 20
private const val SHADOW_OFFSET = 15
private const val SHADOW_SPREAD = 10
private const val SHADOW_BLUR = 10
private const val ICON_SIZE = 40
private const val PILL_RADIUS = 25

@Suppress("LongMethod")
@Composable
fun BiometricCrewListScreen(
    modifier: Modifier = Modifier,
    viewModel: BiometricRegistrationViewModel = hiltViewModel(),
    onEnroll: (username: String) -> Unit,
    onBack: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val lifecycleOwner = LocalLifecycleOwner.current

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                viewModel.loadCrewBiometricStatus()
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose { lifecycleOwner.lifecycle.removeObserver(observer) }
    }

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
                imageVector = ImageVector.vectorResource(id = com.valkiria.uicomponents.R.drawable.ic_back),
                contentDescription = null,
                modifier = Modifier
                    .clickable(onClick = onBack)
                    .padding(end = 12.dp)
                    .size(24.dp),
                tint = MaterialTheme.colorScheme.primary
            )
            Text(
                text = stringResource(R.string.biometric_crew_list_title),
                style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onSurface,
                fontSize = 24.sp
            )
        }
        Text(
            text = stringResource(R.string.biometric_crew_list_subtitle),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            fontSize = 15.sp,
            modifier = Modifier.padding(top = 12.dp)
        )

    Spacer(modifier = Modifier.height(20.dp))

    val pendingList = uiState.crewStatuses.filter { !it.isEnrolled }
    val enrolledList = uiState.crewStatuses.filter { it.isEnrolled }

    if (uiState.crewStatuses.isNotEmpty() && pendingList.isEmpty()) {
        AllEnrolledMessage()
    } else {
        LazyColumn(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(pendingList, key = { it.username }) { status ->
                CrewMemberInfoCard(
                    status = status,
                    onClick = { onEnroll(status.username) }
                )
            }
            items(enrolledList, key = { it.username }) { status ->
                CrewMemberInfoCard(status = status, onClick = null)
            }
        }
    }
}

OnBannerHandler(uiModel = uiState.errorModel) {
    viewModel.consumeError()
}
OnLoadingHandler(uiState.isLoading)
}

@Composable
private fun CrewMemberInfoCard(
    status: CrewBiometricStatus,
    onClick: (() -> Unit)?
) {
    val context = LocalContext.current
    val drawableName = status.role.toCrewDrawable()
    val iconResourceId = context.getResourceIdByName(drawableName, DefType.DRAWABLE)

    val brush = Brush.horizontalGradient(
        colors = listOf(Color.Black, MaterialTheme.colorScheme.background)
    )

    val badgeColor = if (status.isEnrolled) Color(GREEN_HEX) else Color(ORANGE_HEX)

    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(
                borderRadius = CARD_RADIUS.dp,
                offsetX = SHADOW_OFFSET.dp,
                offsetY = SHADOW_OFFSET.dp,
                spread = SHADOW_SPREAD.dp,
                blurRadius = SHADOW_BLUR.dp
            ),
        shape = RoundedCornerShape(CARD_RADIUS.dp)
    ) {
        Box(
            modifier = Modifier
                .then(
                    if (onClick != null) Modifier.clickable(onClick = onClick) else Modifier
                )
                .background(brush = brush)
                .fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(horizontal = 32.dp, vertical = 24.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    iconResourceId?.let {
                        Icon(
                            imageVector = ImageVector.vectorResource(id = it),
                            contentDescription = null,
                            modifier = Modifier.size(ICON_SIZE.dp),
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }

                    Column(
                        modifier = Modifier
                            .padding(start = 12.dp)
                            .weight(1f)
                    ) {
                        Text(
                            text = status.role.uppercase(),
                            style = MaterialTheme.typography.headlineMedium.copy(
                                fontWeight = FontWeight.Bold
                            )
                        )

                        Row(
                            modifier = Modifier
                                .padding(top = 5.dp)
                                .background(
                                    color = badgeColor,
                                    shape = RoundedCornerShape(PILL_RADIUS.dp)
                                )
                                .padding(horizontal = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = status.name,
                                style = MaterialTheme.typography.labelLarge,
                                color = Color.Black,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }
                }

                Text(
                    text = status.document,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }
        }
    }
}

@Composable
private fun AllEnrolledMessage() {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = ImageVector.vectorResource(
                id = com.valkiria.uicomponents.R.drawable.ic_fingerprint
            ),
            contentDescription = null,
            tint = Color(GREEN_HEX),
            modifier = Modifier.size(64.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = stringResource(R.string.biometric_registration_all_enrolled),
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

private fun String.toCrewDrawable() = when (this) {
    "Auxiliar" -> "ic_aux"
    "Conductor" -> "ic_driver"
    "Médico" -> "ic_doctor"
    else -> "ic_lead"
}
