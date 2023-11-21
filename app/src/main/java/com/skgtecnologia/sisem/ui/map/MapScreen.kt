package com.skgtecnologia.sisem.ui.map

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
import com.skgtecnologia.sisem.commons.communication.NotificationEventHandler
import com.skgtecnologia.sisem.ui.menu.MenuDrawer
import com.skgtecnologia.sisem.ui.navigation.NavigationRoute
import com.valkiria.uicomponents.bricks.map.MapView
import com.valkiria.uicomponents.bricks.notification.model.NotificationData
import timber.log.Timber

@Suppress("MagicNumber")
@Composable
fun MapScreen(
    modifier: Modifier = Modifier,
    onAction: (NavigationRoute) -> Unit,
    onLogout: () -> Unit
) {
    val context = LocalContext.current
    val viewModel = hiltViewModel<MapViewModel>()

    val drawerState: DrawerState = rememberDrawerState(initialValue = DrawerValue.Closed)

    var notificationData by remember { mutableStateOf<NotificationData?>(null) }
    NotificationEventHandler.subscribeNotificationEvent {
        notificationData = it
    }

    BackHandler {
        Timber.d("Close the App")
        (context as ComponentActivity).finish()
    }

    // Observe this, subscribe only once, not every Composable pass
    viewModel.getLocationCoordinates()

    MenuDrawer(
        drawerState = drawerState,
        onClick = { menuNavigationRoute ->
            onAction(menuNavigationRoute)
        },
        onLogout = {
            onLogout()
        }
    ) {
        MapView(
            coordinates = viewModel.uiState.location,
            incident = viewModel.uiState.incident,
            drawerState = drawerState,
            notificationData = notificationData,
            modifier = modifier.fillMaxSize()
        ) {
            notificationData = null
        }
    }
}
