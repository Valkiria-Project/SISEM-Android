package com.skgtecnologia.sisem.ui.menu.items

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.DrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.skgtecnologia.sisem.ui.navigation.MainRoute
import com.skgtecnologia.sisem.ui.navigation.NavRoute
import kotlinx.coroutines.launch

@Composable
fun MenuItemsComponent(
    modifier: Modifier,
    menuItems: List<DrawerMenuItemModel>,
    drawerState: DrawerState,
    onClick: (NavRoute) -> Unit
) {
    var currentPick by remember {
        mutableStateOf<NavRoute>(MainRoute.MapRoute)
    }
    val scope = rememberCoroutineScope()

    Column(
        modifier = modifier
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.Start
    ) {
        menuItems.forEach { item ->
            MenuItem(item = item) { navOption ->
                if (currentPick == navOption) {
                    scope.launch {
                        drawerState.close()
                    }
                    return@MenuItem
                }

                currentPick = navOption

                scope.launch {
                    drawerState.close()
                }

                onClick(navOption)
            }
        }
    }
}
