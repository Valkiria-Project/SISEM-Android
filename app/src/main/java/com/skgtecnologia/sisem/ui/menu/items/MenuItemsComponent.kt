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
import com.skgtecnologia.sisem.ui.navigation.MenuNavigationRoute
import com.skgtecnologia.sisem.ui.navigation.MenuNavigationRoute.MenuScreen
import kotlinx.coroutines.launch

@Composable
fun MenuItemsComponent(
    modifier: Modifier,
    menuItems: List<DrawerMenuItemModel>,
    drawerState: DrawerState,
    onClick: (MenuNavigationRoute) -> Unit
) {
    var currentPick by remember {
        mutableStateOf<MenuNavigationRoute>(MenuScreen)
    }
    val coroutineScope = rememberCoroutineScope()

    Column(
        modifier = modifier
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.Start
    ) {
        menuItems.forEach { item ->
            MenuItem(item = item) { navOption ->
                if (currentPick == navOption) {
                    coroutineScope.launch {
                        drawerState.close()
                    }
                    return@MenuItem
                }

                currentPick = navOption

                coroutineScope.launch {
                    drawerState.close()
                }

                onClick(navOption)
            }
        }
    }
}
