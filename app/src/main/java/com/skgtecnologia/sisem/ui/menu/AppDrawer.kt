package com.skgtecnologia.sisem.ui.menu

import android.graphics.Color.parseColor
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Divider
import androidx.compose.material3.DrawerState
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.skgtecnologia.sisem.R
import com.skgtecnologia.sisem.ui.menu.itemsDrawer.MenuItems
import com.skgtecnologia.sisem.ui.menu.itemsDrawer.PersonalItems
import com.skgtecnologia.sisem.ui.navigation.MenuNavigationRoute
import com.valkiria.uicomponents.props.toTextStyle
import kotlinx.coroutines.launch

@Composable
fun AppDrawer(
    drawerState: DrawerState,
    menuItemsPersonal: List<DrawerItemInfoPersonal>,
    menuItems: List<DrawerItemInfo>,
    onClick: (MenuNavigationRoute) -> Unit,
    onLogout: (DrawerItemInfoPersonal) -> Unit
) {
    ModalDrawerSheet {
        ConstraintLayout(
            modifier = Modifier.fillMaxSize()
        ) {
            val (personalComponent, itemsComponent, bottomComponent) = createRefs()

            PersonalComponent(
                modifier = Modifier
                    .constrainAs(personalComponent) {
                        top.linkTo(parent.top)
                        start.linkTo(parent.start)
                    },
                menuItemsPersonal = menuItemsPersonal,
                onLogout = onLogout
            )

            ItemsComponent(
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
                onClick = onClick
            )

            BottomComponent(
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

@Suppress("LongMethod")
@Composable
private fun PersonalComponent(
    modifier: Modifier,
    menuItemsPersonal: List<DrawerItemInfoPersonal>,
    onLogout: (DrawerItemInfoPersonal) -> Unit
) {
    Column(modifier = modifier) {
        Row {
            Icon(
                painter = painterResource(id = R.drawable.ic_ambulance),
                contentDescription = null,
                modifier = Modifier
                    .padding(start = 33.dp, top = 30.dp, bottom = 10.dp)
                    .width(64.096.dp)
                    .height(54.342.dp),
                tint = Color(parseColor("#EB8FEB"))
            )

            Column {
                Text(
                    text = "SMSOO",
                    modifier = Modifier
                        .padding(start = 20.dp, top = 30.dp),
                    style = TextStyle(
                        fontFamily = FontFamily.Default,
                        fontSize = 24.sp,
                        fontStyle = FontStyle.Normal,
                        fontWeight = FontWeight.W700,
                        lineHeight = 28.sp,
                        letterSpacing = (2).sp,
                        color = Color(parseColor("#EB8FEB"))
                    )
                )

                Text(
                    text = "5185",
                    modifier = Modifier.padding(
                        start = 20.dp
                    ),
                    style = com.valkiria.uicomponents.props.TextStyle.HEADLINE_5.toTextStyle(),
                    color = Color(parseColor("#EB8FEB"))
                )
            }
        }

        ConstraintLayout(
            modifier = Modifier
                .padding(start = 32.dp, top = 10.dp, end = 32.dp)
                .fillMaxWidth()
        ) {
            val (subRedText, functionalUnitText, baseText) = createRefs()
            Text(
                "Subred: Norte",
                modifier = Modifier.constrainAs(subRedText) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                },
                style = com.valkiria.uicomponents.props.TextStyle.BUTTON_1.toTextStyle()
            )

            Text(
                "Base:",
                modifier = Modifier.constrainAs(functionalUnitText) {
                    top.linkTo(subRedText.bottom)
                    start.linkTo(parent.start)
                },
                style = com.valkiria.uicomponents.props.TextStyle.BUTTON_1.toTextStyle()
            )

            Text(
                "Unidad funcional",
                modifier = Modifier
                    .constrainAs(baseText) {
                        end.linkTo(parent.end)
                    },
                style = com.valkiria.uicomponents.props.TextStyle.BUTTON_1.toTextStyle(),
                textAlign = TextAlign.End
            )
        }

        Divider(
            modifier = Modifier.padding(start = 32.dp, end = 32.dp, top = 20.dp),
            color = MaterialTheme.colorScheme.primary
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    start = 33.dp, end = 20.dp, top = 15.dp
                ),
            horizontalAlignment = Alignment.Start,
        ) {
            menuItemsPersonal.forEach {
                PersonalItems(item = it) {
                    onLogout(it)
                }
            }
        }

        Divider(
            modifier = Modifier.padding(start = 33.dp, end = 33.dp, top = 15.dp),
            color = MaterialTheme.colorScheme.primary
        )
    }
}

@Composable
private fun ItemsComponent(
    modifier: Modifier,
    menuItems: List<DrawerItemInfo>,
    drawerState: DrawerState,
    onClick: (MenuNavigationRoute) -> Unit
) {
    var currentPick by remember {
        mutableStateOf<MenuNavigationRoute>(MenuNavigationRoute.MenuScreen)
    }
    val coroutineScope = rememberCoroutineScope()

    Column(
        modifier = modifier
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.Start
    ) {
        menuItems.forEach { item ->
            MenuItems(item = item) { navOption ->

                if (currentPick == navOption) {
                    coroutineScope.launch {
                        drawerState.close()
                    }
                    return@MenuItems
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

@Composable
private fun BottomComponent(modifier: Modifier) {
    Row(modifier = modifier) {
        Image(
            painter = painterResource(id = R.drawable.banner_sisem),
            contentDescription = null,
            modifier = Modifier
                .padding(start = 16.dp, end = 16.dp)
        )

        Image(
            painter = painterResource(id = R.drawable.banner_gov),
            contentDescription = null,
            modifier = Modifier
                .padding(start = 25.dp)
        )
    }
}
