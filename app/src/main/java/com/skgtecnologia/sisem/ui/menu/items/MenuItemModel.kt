package com.skgtecnologia.sisem.ui.menu.items

import androidx.annotation.DrawableRes
import com.skgtecnologia.sisem.ui.navigation.MenuNavigationRoute
import com.skgtecnologia.sisem.ui.navigation.MenuNavigationRoute.CertificationsScreen
import com.skgtecnologia.sisem.ui.navigation.MenuNavigationRoute.DrivingGuideScreen
import com.skgtecnologia.sisem.ui.navigation.MenuNavigationRoute.HCEUDCScreen
import com.skgtecnologia.sisem.ui.navigation.MenuNavigationRoute.IncidentScreen
import com.skgtecnologia.sisem.ui.navigation.MenuNavigationRoute.InventoryScreen
import com.skgtecnologia.sisem.ui.navigation.MenuNavigationRoute.NewsScreen
import com.skgtecnologia.sisem.ui.navigation.MenuNavigationRoute.NotificationsScreen
import com.valkiria.uicomponents.R.drawable

// FIXME
object MenuItemModel {
    val drawerItems = arrayListOf(
        DrawerItemInfo(
            IncidentScreen,
            "Incidentes",
            drawable.ic_incidents
        ),
        DrawerItemInfo(
            InventoryScreen,
            "Inventario",
            drawable.ic_inventory
        ),
        DrawerItemInfo(
            NotificationsScreen,
            "Notificacion",
            drawable.ic_notification
        ),
        DrawerItemInfo(
            DrivingGuideScreen,
            "Guia para conducir",
            drawable.ic_driver
        ),
        DrawerItemInfo(
            CertificationsScreen,
            "Certificaciones",
            drawable.ic_certifications
        ),
        DrawerItemInfo(
            NewsScreen,
            "Novedades",
            drawable.ic_news
        ),
        DrawerItemInfo(
            HCEUDCScreen,
            "HCEUD",
            drawable.ic_hceud
        )
    )
}

data class DrawerItemInfo(
    val drawerOption: MenuNavigationRoute,
    val title: String,
    @DrawableRes val drawableId: Int
)
