package com.skgtecnologia.sisem.ui.map

import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Menu
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.skgtecnologia.sisem.ui.menu.MenuDrawer
import com.skgtecnologia.sisem.ui.navigation.NavigationRoute
import com.valkiria.uicomponents.components.map.MapComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import timber.log.Timber

@Suppress("MagicNumber")
@Composable
fun MapScreen(
    modifier: Modifier = Modifier,
    onClick: (NavigationRoute) -> Unit,
    onLogout: () -> Unit
) {
    val viewModel = hiltViewModel<MapViewModel>()

    val context = LocalContext.current

    val coroutineScope: CoroutineScope = rememberCoroutineScope()
    val drawerState: DrawerState = rememberDrawerState(initialValue = DrawerValue.Closed)

    BackHandler {
        Timber.d("Close the App")
        (context as ComponentActivity).finish()
    }

    // Observe this, subscribe only once, not every Composable pass
    viewModel.getLocationCoordinates()

    MenuDrawer(
        drawerState = drawerState,
        onClick = { menuNavigationRoute ->
            onClick(menuNavigationRoute)
        },
        onLogout = {
            onLogout()
        }
    ) {
        Box(
            modifier = modifier.fillMaxSize(),
        ) {
            MapComponent(
                modifier = Modifier.fillMaxSize(),
                coordinates = viewModel.uiState.location
            )
            IconButton(
                onClick = {
                    coroutineScope.launch {
                        drawerState.open()
                    }
                },
                modifier = Modifier.padding(12.dp)
            ) {
                Icon(
                    imageVector = Icons.Outlined.Menu,
                    contentDescription = null,
                    modifier = Modifier
                        .background(color = MaterialTheme.colorScheme.primary)
                        .size(72.dp)
                        .padding(5.dp),
                    tint = Color.Black
                )
            }
        }
    }
}
