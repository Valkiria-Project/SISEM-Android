package com.skgtecnologia.sisem.ui.map

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
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
import com.skgtecnologia.sisem.ui.menu.MenuDrawer
import com.skgtecnologia.sisem.ui.navigation.MainNavigationRoute
import com.valkiria.uicomponents.components.map.MapComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Suppress("MagicNumber")
@Composable
fun MapScreen(
    modifier: Modifier = Modifier,
    onClick: (MainNavigationRoute) -> Unit,
    onLogout: () -> Unit
) {
    val coroutineScope: CoroutineScope = rememberCoroutineScope()
    val drawerState: DrawerState = rememberDrawerState(initialValue = DrawerValue.Closed)

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
                coordinates = -75.5657751 to 6.2082622
            )
            IconButton(
                onClick = {
                    coroutineScope.launch {
                        drawerState.open()
                    }
                }
            ) {
                Icon(
                    imageVector = Icons.Outlined.Menu,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}
