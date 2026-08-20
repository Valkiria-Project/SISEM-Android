package com.skgtecnologia.sisem.ui.authcards.create

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.outlined.CameraAlt
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.ShareLocation
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.google.accompanist.permissions.PermissionState
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.shouldShowRationale
import com.skgtecnologia.sisem.R
import com.skgtecnologia.sisem.ui.authcards.create.report.PagerIndicator

@Composable
internal fun PermissionCarousel(
    notificationsPermissionState: PermissionState?,
    fineLocationPermissionState: PermissionState,
    cameraPermissionState: PermissionState,
    backgroundLocationPermissionState: PermissionState?,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val states = PermissionStates(
        fineLocation = fineLocationPermissionState,
        camera = cameraPermissionState,
        notifications = notificationsPermissionState,
        backgroundLocation = backgroundLocationPermissionState
    )
    val pages = rememberPermissionPages(states)
    val pagerState = rememberPagerState(initialPage = 0) { pages.size }

    AutoAdvanceOnGrant(pages, pagerState, states)

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        PermissionCarouselHeader()

        Spacer(modifier = Modifier.height(48.dp))

        PermissionPager(pages, pagerState, states, context)

        Spacer(modifier = Modifier.height(24.dp))

        if (pages.size > 1) {
            PagerIndicator(
                pageCount = pages.size,
                currentPage = pagerState.currentPage,
                selectedColor = MaterialTheme.colorScheme.primary,
                inactiveColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.3f)
            )
        }
    }
}

@Composable
private fun AutoAdvanceOnGrant(
    pages: List<PermissionPage>,
    pagerState: PagerState,
    states: PermissionStates
) {
    LaunchedEffect(
        states.fineLocation.status.isGranted,
        states.camera.status.isGranted,
        states.notifications?.status?.isGranted,
        states.backgroundLocation?.status?.isGranted
    ) {
        val currentPage = pages.getOrNull(pagerState.currentPage)
            ?: return@LaunchedEffect
        if (currentPage.isGranted(states) && pagerState.currentPage < pages.lastIndex) {
            pagerState.animateScrollToPage(pagerState.currentPage + 1)
        }
    }
}

@Composable
private fun PermissionCarouselHeader() {
    Text(
        text = stringResource(R.string.permission_carousel_title),
        style = MaterialTheme.typography.headlineMedium,
        color = Color.White,
        textAlign = TextAlign.Center
    )

    Spacer(modifier = Modifier.height(8.dp))

    Text(
        text = stringResource(R.string.permission_carousel_subtitle),
        style = MaterialTheme.typography.bodyMedium,
        color = Color.White.copy(alpha = 0.7f),
        textAlign = TextAlign.Center
    )
}

@Composable
private fun PermissionPager(
    pages: List<PermissionPage>,
    pagerState: PagerState,
    states: PermissionStates,
    context: Context
) {
    HorizontalPager(
        state = pagerState,
        modifier = Modifier.fillMaxWidth()
    ) { pageIndex ->
        val page = pages[pageIndex]
        PermissionPageContent(
            page = page,
            isGranted = page.isGranted(states),
            showRationale = page.hasRationale(states),
            onAction = { page.performAction(states, context) }
        )
    }
}

@Suppress("LongMethod")
@Composable
private fun PermissionPageContent(
    page: PermissionPage,
    isGranted: Boolean,
    showRationale: Boolean,
    onAction: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = page.icon,
            contentDescription = null,
            modifier = Modifier.size(80.dp),
            tint = if (isGranted) {
                MaterialTheme.colorScheme.tertiary
            } else {
                MaterialTheme.colorScheme.primary
            }
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = stringResource(page.titleRes),
            style = MaterialTheme.typography.titleLarge,
            color = Color.White,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = stringResource(page.descriptionRes),
            style = MaterialTheme.typography.bodyMedium,
            color = Color.White.copy(alpha = 0.7f),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(32.dp))

        if (isGranted) {
            Icon(
                imageVector = Icons.Filled.CheckCircle,
                contentDescription = stringResource(R.string.permission_granted_label),
                modifier = Modifier.size(48.dp),
                tint = MaterialTheme.colorScheme.tertiary
            )
        } else {
            Button(
                onClick = onAction,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            ) {
                Text(text = stringResource(page.actionLabelRes))
            }

            if (showRationale && !page.settingsOnly) {
                Spacer(modifier = Modifier.height(8.dp))

                TextButton(onClick = onAction) {
                    Text(
                        text = stringResource(R.string.permission_go_to_settings_cta),
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }
    }
}

@Composable
private fun rememberPermissionPages(
    states: PermissionStates
): List<PermissionPage> = remember(
    states.notifications,
    states.backgroundLocation
) {
    buildList {
        add(PermissionPage.Location)
        add(PermissionPage.Camera)
        if (states.notifications != null) {
            add(PermissionPage.Notifications)
        }
        if (states.backgroundLocation != null) {
            add(PermissionPage.BackgroundLocation)
        }
    }
}

internal data class PermissionStates(
    val fineLocation: PermissionState,
    val camera: PermissionState,
    val notifications: PermissionState?,
    val backgroundLocation: PermissionState?
)

private enum class PermissionPage(
    @StringRes val titleRes: Int,
    @StringRes val descriptionRes: Int,
    @StringRes val actionLabelRes: Int,
    val icon: ImageVector,
    val settingsOnly: Boolean = false
) {
    Location(
        titleRes = R.string.permission_location_title,
        descriptionRes = R.string.permission_location_description,
        actionLabelRes = R.string.permission_allow_cta,
        icon = Icons.Outlined.LocationOn
    ),
    Camera(
        titleRes = R.string.permission_camera_title,
        descriptionRes = R.string.permission_camera_description,
        actionLabelRes = R.string.permission_allow_cta,
        icon = Icons.Outlined.CameraAlt
    ),
    Notifications(
        titleRes = R.string.permission_notifications_title,
        descriptionRes = R.string.permission_notifications_description,
        actionLabelRes = R.string.permission_allow_cta,
        icon = Icons.Outlined.Notifications
    ),
    BackgroundLocation(
        titleRes = R.string.permission_bg_location_title,
        descriptionRes = R.string.permission_bg_location_description,
        actionLabelRes = R.string.permission_go_to_settings_cta,
        icon = Icons.Outlined.ShareLocation,
        settingsOnly = true
    );

    fun isGranted(states: PermissionStates): Boolean = when (this) {
        Location -> states.fineLocation.status.isGranted
        Camera -> states.camera.status.isGranted
        Notifications -> states.notifications?.status?.isGranted == true
        BackgroundLocation -> states.backgroundLocation?.status?.isGranted == true
    }

    fun hasRationale(states: PermissionStates): Boolean = when (this) {
        Location -> states.fineLocation.status.shouldShowRationale
        Camera -> states.camera.status.shouldShowRationale
        Notifications -> states.notifications?.status?.shouldShowRationale == true
        BackgroundLocation -> false
    }

    fun performAction(states: PermissionStates, context: Context) {
        when (this) {
            Location -> states.fineLocation.launchPermissionRequest()
            Camera -> states.camera.launchPermissionRequest()
            Notifications -> states.notifications?.launchPermissionRequest()
            BackgroundLocation -> openAppSettings(context)
        }
    }
}

private fun openAppSettings(context: Context) {
    Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
        data = Uri.fromParts("package", context.packageName, null)
        context.startActivity(this)
    }
}
