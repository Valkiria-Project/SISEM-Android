package com.skgtecnologia.sisem.ui.menu

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Menu
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.skgtecnologia.sisem.ui.menu.header.toCrewMemberItemModel
import com.skgtecnologia.sisem.ui.menu.items.getDrawerMenuItemList
import com.skgtecnologia.sisem.ui.navigation.MainNavigationRoute
import com.valkiria.uicomponents.components.banner.OnErrorHandler
import com.valkiria.uicomponents.components.loader.LoaderComponent
import com.valkiria.uicomponents.components.map.MapComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun MenuDrawer(
    modifier: Modifier = Modifier,
    onClick: (MainNavigationRoute) -> Unit,
    onLogout: () -> Unit,
    content: @Composable () -> Unit
) {
    val viewModel = hiltViewModel<MenuViewModel>()
    val coroutineScope: CoroutineScope = rememberCoroutineScope()
    val drawerState: DrawerState = rememberDrawerState(initialValue = DrawerValue.Closed)

    val uiState = viewModel.uiState

    val crewMenuItems = uiState.accessTokenModelList?.let { list ->
        list.map { accessTokenModel ->
            accessTokenModel.toCrewMemberItemModel()
        }
    } ?: emptyList()

    val isAdmin = uiState.accessTokenModelList?.first()?.isAdmin

    LaunchedEffect(uiState) {
        launch {
            if (uiState.isLogout) {
                onLogout()
            }
        }
    }

    ModalNavigationDrawer(
        drawerContent = {
            DrawerContent(
                drawerState = drawerState,
                vehicleConfig = uiState.vehicleConfig,
                crewMenuItems = crewMenuItems,
                menuItems = getDrawerMenuItemList(LocalContext.current, isAdmin),
                onMenuItemClick = { onClick(it) },
                onLogout = { viewModel.logout(it.username) }
            )
        },
        drawerState = drawerState,
        gesturesEnabled = drawerState.isOpen
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

    OnErrorHandler(uiModel = uiState.errorModel) {
        viewModel.handleShownError()
    }

    if (uiState.isLoading) {
        LoaderComponent()
    }
}

@Preview
@Composable
fun MenuDrawerPreview() {
    MenuDrawer(
        onClick = {},
        onLogout = {}
    ) {

    }
}
