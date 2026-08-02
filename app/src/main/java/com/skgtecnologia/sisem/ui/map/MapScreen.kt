package com.skgtecnologia.sisem.ui.map

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.fragment.compose.FragmentState
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.skgtecnologia.sisem.R
import com.skgtecnologia.sisem.commons.communication.IncidentEventHandler
import com.skgtecnologia.sisem.commons.communication.NotificationEventHandler
import com.skgtecnologia.sisem.commons.location.ACTION_START
import com.skgtecnologia.sisem.commons.location.LocationService
import com.skgtecnologia.sisem.ui.menu.MenuDrawer
import com.skgtecnologia.sisem.ui.navigation.AphRoute
import com.skgtecnologia.sisem.ui.navigation.NavRoute
import com.valkiria.uicomponents.bricks.banner.BannerUiModel
import com.valkiria.uicomponents.bricks.notification.model.NotificationData
import timber.log.Timber

@OptIn(ExperimentalPermissionsApi::class)
@SuppressLint("MissingPermission")
@Suppress("LongParameterList", "MagicNumber")
@Composable
fun MapScreen(
    modifier: Modifier = Modifier,
    fragmentState: FragmentState,
    viewModel: MapViewModel = hiltViewModel(),
    onMenuAction: (NavRoute) -> Unit,
    onAction: (aphRoute: AphRoute) -> Unit,
    onLogout: (loggedOutRole: String?) -> Unit
) {
    val currentMenuAction by rememberUpdatedState(onMenuAction)
    val currentOnAction by rememberUpdatedState(onAction)
    val currentOnLogout by rememberUpdatedState(onLogout)

    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    val drawerState: DrawerState = rememberDrawerState(initialValue = DrawerValue.Closed)

    LocationPermissionHandler()

    var notificationData by remember { mutableStateOf<NotificationData?>(null) }
    NotificationEventHandler.subscribeNotificationEvent {
        notificationData = it
    }

    var incidentErrorData by remember { mutableStateOf<BannerUiModel?>(null) }
    IncidentEventHandler.subscribeIncidentErrorEvent {
        incidentErrorData = it
    }

    BackHandler {
        Timber.d("Close the App")
        (context as ComponentActivity).finish()
    }

    MenuDrawer(
        drawerState = drawerState,
        onClick = currentMenuAction,
        onLogout = currentOnLogout
    ) {
        MapboxMapView(
            incident = uiState.incident,
            notifications = uiState.notifications,
            drawerState = drawerState,
            notificationData = notificationData,
            incidentErrorData = incidentErrorData,
            modifier = modifier.fillMaxSize(),
            fragmentState = fragmentState,
            onNotificationAction = {
                notificationData = null
            },
            onIncidentErrorAction = {
                incidentErrorData = null
            }
        ) { idAph ->
            Timber.d("Navigate to APH with Id APH $idAph")

            currentOnAction(AphRoute.MedicalHistoryRoute(idAph.toString()))
        }
    }
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
private fun LocationPermissionHandler() {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    val fineLocation = rememberPermissionState(Manifest.permission.ACCESS_FINE_LOCATION)
    val backgroundLocation = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        rememberPermissionState(Manifest.permission.ACCESS_BACKGROUND_LOCATION)
    } else {
        null
    }

    var showBackgroundRationale by remember { mutableStateOf(false) }

    // Increment on every ON_RESUME so LaunchedEffect re-runs and reads
    // the fresh permission state after the user returns from Settings.
    var resumeTick by remember { mutableIntStateOf(0) }
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) resumeTick++
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose { lifecycleOwner.lifecycle.removeObserver(observer) }
    }

    // Runs when permission states change OR when the screen resumes.
    LaunchedEffect(fineLocation.status, backgroundLocation?.status, resumeTick) {
        when {
            !fineLocation.status.isGranted -> {
                fineLocation.launchPermissionRequest()
            }

            backgroundLocation != null && !backgroundLocation.status.isGranted -> {
                showBackgroundRationale = true
            }

            else -> {
                Intent(context.applicationContext, LocationService::class.java).apply {
                    action = ACTION_START
                    context.startService(this)
                }
            }
        }
    }

    if (showBackgroundRationale) {
        AlertDialog(
            onDismissRequest = { showBackgroundRationale = false },
            title = { Text(stringResource(R.string.location_bg_permission_title)) },
            text = { Text(stringResource(R.string.location_bg_permission_description)) },
            confirmButton = {
                TextButton(onClick = {
                    showBackgroundRationale = false
                    context.startActivity(
                        Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                            data = Uri.fromParts("package", context.packageName, null)
                        }
                    )
                }) { Text(stringResource(R.string.location_bg_permission_go_to_settings)) }
            },
            dismissButton = {
                TextButton(onClick = { showBackgroundRationale = false }) {
                    Text(stringResource(R.string.location_bg_permission_dismiss))
                }
            }
        )
    }
}
