package com.skgtecnologia.sisem.ui.menu

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.skgtecnologia.sisem.ui.menu.header.toCrewMemberItemModel
import com.skgtecnologia.sisem.ui.menu.items.MenuItemModel
import com.skgtecnologia.sisem.ui.navigation.MenuNavigationRoute
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun MenuDrawer(
    onClick: (MenuNavigationRoute) -> Unit,
    onLogout: () -> Unit
) {

    val viewModel = hiltViewModel<MenuViewModel>()
    val coroutineScope: CoroutineScope = rememberCoroutineScope()
    val drawerState: DrawerState = rememberDrawerState(initialValue = DrawerValue.Closed)

    val uiState = viewModel.uiState // FIXME: loading ???

    val menuItemsPersonal = uiState.accessTokenModelList?.let { list ->
        list.map { accessTokenModel ->
            accessTokenModel.toCrewMemberItemModel()
        }
    } ?: emptyList()

    // FIXME: It is possible that the leader can authenticate when there are another users logged in?
    val isAdmin = uiState.accessTokenModelList?.let { list ->
        list.first().isAdmin
    }

    LaunchedEffect(uiState) {
        launch {
            if (uiState.isLogout) {
                onLogout()
            }
        }
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            DrawerContent(
                drawerState = drawerState,
                menuItemsPersonal = menuItemsPersonal,
                menuItems = MenuItemModel.getDrawerInfoItemList(isAdmin),
                onMenuItemClick = { onClick(it) },
                onLogout = { viewModel.logout(it.username) }
            )
        }
    ) {
        IconButton(onClick = {
            coroutineScope.launch { drawerState.open() }
        }, content = {
            Icon(
                imageVector = Icons.Filled.Menu,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary
            )
        })
    }
}

@Preview
@Composable
fun MenuRoot() {
    MenuDrawer(
        onClick = {},
        onLogout = {}
    )
}
