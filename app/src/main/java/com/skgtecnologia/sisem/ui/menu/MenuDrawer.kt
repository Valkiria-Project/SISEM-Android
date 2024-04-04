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
import com.valkiria.uicomponents.bricks.banner.OnBannerHandler
import com.valkiria.uicomponents.bricks.loader.OnLoadingHandler
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
                onMenuItemClick = {
                    viewModel.getOperationConfig()
                    onClick(it)
                },
                onLogout = { viewModel.logout(it.username) }
            )
        },
        drawerState = drawerState,
        gesturesEnabled = drawerState.isOpen
    ) {
        content()
    }

    OnBannerHandler(uiModel = uiState.errorModel) {
        viewModel.consumeErrorEvent()
    }

    OnLoadingHandler(uiState.isLoading)
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
