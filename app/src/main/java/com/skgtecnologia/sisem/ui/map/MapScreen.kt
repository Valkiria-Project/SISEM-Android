package com.skgtecnologia.sisem.ui.map

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.skgtecnologia.sisem.ui.menu.MenuDrawer
import com.skgtecnologia.sisem.ui.navigation.MainNavigationRoute
import com.valkiria.uicomponents.components.map.MapComponent

@Suppress("MagicNumber")
@Composable
fun MapScreen(
    modifier: Modifier = Modifier,
    onClick: (MainNavigationRoute) -> Unit,
    onLogout: () -> Unit
) {
    Box(
        modifier = modifier.fillMaxSize(),
    ) {
        MapComponent(
            modifier = Modifier.fillMaxSize(),
            coordinates = -75.5657751 to 6.2082622
        )

        MenuDrawer(
            onClick = { menuNavigationRoute ->
                onClick(menuNavigationRoute)
            },
            onLogout = {
                onLogout()
            }
        )
    }
}
