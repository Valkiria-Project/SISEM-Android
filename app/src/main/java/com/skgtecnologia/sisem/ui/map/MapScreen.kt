package com.skgtecnologia.sisem.ui.map

import android.annotation.SuppressLint
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import com.skgtecnologia.sisem.commons.communication.IncidentEventHandler
import com.skgtecnologia.sisem.commons.communication.NotificationEventHandler
import com.skgtecnologia.sisem.ui.menu.MenuDrawer
import com.skgtecnologia.sisem.ui.navigation.AphRoute
import com.skgtecnologia.sisem.ui.navigation.NavRoute
import com.valkiria.uicomponents.bricks.banner.BannerUiModel
import com.valkiria.uicomponents.bricks.map.MapboxMapView
import com.valkiria.uicomponents.bricks.notification.model.NotificationData
import timber.log.Timber

@SuppressLint("MissingPermission")
@Suppress("MagicNumber")
@Composable
fun MapScreen(
    modifier: Modifier = Modifier,
    viewModel: MapViewModel = hiltViewModel(),
    onMenuAction: (NavRoute) -> Unit,
    onAction: (aphRoute: AphRoute) -> Unit,
    onLogout: () -> Unit
) {
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsState()

    val drawerState: DrawerState = rememberDrawerState(initialValue = DrawerValue.Closed)

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
        onClick = { menuNavigationRoute ->
            onMenuAction(menuNavigationRoute)
        },
        onLogout = {
            onLogout()
        }
    ) {
        if (uiState.lastLocation != null) {
            MapboxMapView(
                location = uiState.lastLocation,
                incident = uiState.incident,
                notifications = uiState.notifications,
                drawerState = drawerState,
                notificationData = notificationData,
                incidentErrorData = incidentErrorData,
                modifier = modifier.fillMaxSize(),
                onNotificationAction = {
                    notificationData = null
                },
                onIncidentErrorAction = {
                    incidentErrorData = null
                }
            ) { idAph ->
                Timber.d("Navigate to APH with Id APH $idAph")
                onAction(AphRoute.MedicalHistoryRoute(idAph.toString()))
            }
        }
    }
}
