package com.skgtecnologia.sisem.ui.menu

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DrawerState
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.skgtecnologia.sisem.ui.menu.footer.MenuFooterComponent
import com.skgtecnologia.sisem.ui.menu.header.DrawerItemInfoPersonal
import com.skgtecnologia.sisem.ui.menu.header.MenuHeaderComponent
import com.skgtecnologia.sisem.ui.menu.items.DrawerItemInfo
import com.skgtecnologia.sisem.ui.menu.items.MenuItemsComponent
import com.skgtecnologia.sisem.ui.navigation.MenuNavigationRoute

@Composable
fun DrawerContent(
    drawerState: DrawerState,
    menuItemsPersonal: List<DrawerItemInfoPersonal>,
    menuItems: List<DrawerItemInfo>,
    onMenuItemClick: (MenuNavigationRoute) -> Unit,
    onLogout: (DrawerItemInfoPersonal) -> Unit
) {
    ModalDrawerSheet {
        ConstraintLayout(
            modifier = Modifier.fillMaxSize()
        ) {
            val (personalComponent, itemsComponent, bottomComponent) = createRefs()

            MenuHeaderComponent(
                modifier = Modifier
                    .constrainAs(personalComponent) {
                        top.linkTo(parent.top)
                        start.linkTo(parent.start)
                    },
                menuItemsPersonal = menuItemsPersonal,
                onLogout = onLogout
            )
            MenuItemsComponent(
                modifier = Modifier
                    .padding(
                        start = 32.dp,
                        top = 10.dp,
                        end = 32.dp
                    )
                    .constrainAs(itemsComponent) {
                        top.linkTo(personalComponent.bottom)
                        start.linkTo(parent.start)
                        bottom.linkTo(bottomComponent.top)
                        height = Dimension.fillToConstraints
                    },
                menuItems = menuItems,
                drawerState = drawerState,
                onClick = onMenuItemClick
            )
            MenuFooterComponent(
                modifier = Modifier
                    .constrainAs(bottomComponent) {
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        bottom.linkTo(parent.bottom)
                    }
                    .padding(vertical = 16.dp)
            )
        }
    }
}

