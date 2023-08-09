package com.skgtecnologia.sisem.ui.menu

import androidx.annotation.DrawableRes
import com.skgtecnologia.sisem.ui.navigation.MenuNavigationRoute
import com.valkiria.uicomponents.R.drawable

object DrawerItem {
    val drawerItems = arrayListOf(
        DrawerItemInfo(
            MenuNavigationRoute.IncidentScreen,
            "Incidentes",
            drawable.ic_incidents
        ),
        DrawerItemInfo(
            MenuNavigationRoute.InventoryScreen,
            "inventario",
            drawable.ic_inventory
        ),
        DrawerItemInfo(
            MenuNavigationRoute.NotificationsScreen,
            "Notificacion",
            drawable.ic_notification
        ),
        DrawerItemInfo(
            MenuNavigationRoute.DrivingGuideScreen,
            "Guia para conducir",
            drawable.ic_driver
        ),
        DrawerItemInfo(
            MenuNavigationRoute.CertificationsScreen,
            "Certificaciones",
            drawable.ic_certifications
        ),
        DrawerItemInfo(
            MenuNavigationRoute.NewsScreen,
            "Novedades",
            drawable.ic_news
        ),
        DrawerItemInfo(
            MenuNavigationRoute.HCEUDCScreen,
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
