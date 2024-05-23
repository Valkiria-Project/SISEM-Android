package com.skgtecnologia.sisem.ui.map

import android.annotation.SuppressLint
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
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
import com.skgtecnologia.sisem.ui.navigation.AphNavigationRoute
import com.skgtecnologia.sisem.ui.navigation.NavigationRoute
import com.valkiria.uicomponents.bricks.banner.BannerUiModel
import com.valkiria.uicomponents.bricks.map.MapboxMapView
import com.valkiria.uicomponents.bricks.notification.model.NotificationData
import timber.log.Timber

@SuppressLint("MissingPermission")
@Suppress("MagicNumber")
@Composable
fun MapScreen(
    modifier: Modifier = Modifier,
    onMenuAction: (NavigationRoute) -> Unit,
    onAction: (aphRoute: String) -> Unit,
    onLogout: () -> Unit
) {
    val context = LocalContext.current
    val viewModel = hiltViewModel<MapViewModel>()
    val uiState = viewModel.uiState

    val drawerState: DrawerState = rememberDrawerState(initialValue = DrawerValue.Closed)

    var notificationData by remember { mutableStateOf<NotificationData?>(null) }
    NotificationEventHandler.subscribeNotificationEvent {
        notificationData = it
    }

    var incidentErrorData by remember { mutableStateOf<BannerUiModel?>(null) }
    IncidentEventHandler.subscribeIncidentErrorEvent {
        incidentErrorData = it
    }

    val incident by remember(uiState.incident) {
        mutableStateOf(viewModel.uiState.incident)
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
        MapboxMapView(
            coordinates = viewModel.uiState.location,
            incident = incident,
            notifications = viewModel.uiState.notifications,
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
            onAction(
                AphNavigationRoute.MedicalHistoryScreen.route + "/$idAph"
            )
        }
    }
}
