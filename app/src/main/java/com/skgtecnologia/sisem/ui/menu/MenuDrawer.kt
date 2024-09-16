package com.skgtecnologia.sisem.ui.menu

import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.skgtecnologia.sisem.ui.menu.header.toCrewMemberItemModel
import com.skgtecnologia.sisem.ui.menu.items.getDrawerMenuItemList
import com.skgtecnologia.sisem.ui.navigation.NavRoute
import com.valkiria.uicomponents.bricks.banner.OnBannerHandler
import com.valkiria.uicomponents.bricks.loader.OnLoadingHandler
import kotlinx.coroutines.launch

@Suppress("LongMethod", "LongParameterList")
@Composable
fun MenuDrawer(
    viewModel: MenuViewModel = hiltViewModel(),
    drawerState: DrawerState,
    onClick: (NavRoute) -> Unit,
    onLogout: () -> Unit,
    content: @Composable () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val crewMenuItems = uiState.accessTokenModelList?.let { list ->
        list.map { accessTokenModel ->
            accessTokenModel.toCrewMemberItemModel()
        }
    } ?: emptyList()

    val isAdmin by remember { derivedStateOf { uiState.accessTokenModelList?.first()?.isAdmin } }

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
                onMenuItemClick = onClick,
                onLogout = {
                    if (isAdmin == true) {
                        viewModel.logoutAdmin(it.username)
                    } else {
                        viewModel.logout(it.username)
                    }
                }
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
        drawerState = rememberDrawerState(initialValue = DrawerValue.Closed),
        onClick = {},
        onLogout = {}
    ) {}
}
