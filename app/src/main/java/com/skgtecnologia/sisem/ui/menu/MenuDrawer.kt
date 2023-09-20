package com.skgtecnologia.sisem.ui.menu

import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.skgtecnologia.sisem.ui.menu.header.toCrewMemberItemModel
import com.skgtecnologia.sisem.ui.menu.items.getDrawerMenuItemList
import com.skgtecnologia.sisem.ui.navigation.NavigationRoute
import com.valkiria.uicomponents.components.banner.OnErrorHandler
import com.valkiria.uicomponents.components.loader.LoaderComponent
import kotlinx.coroutines.launch

@Suppress("LongMethod", "LongParameterList")
@Composable
fun MenuDrawer(
    drawerState: DrawerState,
    onClick: (NavigationRoute) -> Unit,
    onLogout: () -> Unit,
    content: @Composable () -> Unit
) {
    val viewModel = hiltViewModel<MenuViewModel>()

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
        content()
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
        rememberDrawerState(initialValue = DrawerValue.Closed),
        onClick = {},
        onLogout = {}
    ) {}
}
