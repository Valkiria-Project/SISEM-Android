package com.skgtecnologia.sisem.ui.menu.items

import androidx.annotation.DrawableRes
import com.skgtecnologia.sisem.ui.navigation.MenuNavigationRoute
import com.skgtecnologia.sisem.ui.navigation.MenuNavigationRoute.CertificationsScreen
import com.skgtecnologia.sisem.ui.navigation.MenuNavigationRoute.DeviceAuth
import com.skgtecnologia.sisem.ui.navigation.MenuNavigationRoute.DrivingGuideScreen
import com.skgtecnologia.sisem.ui.navigation.MenuNavigationRoute.HCEUDCScreen
import com.skgtecnologia.sisem.ui.navigation.MenuNavigationRoute.IncidentScreen
import com.skgtecnologia.sisem.ui.navigation.MenuNavigationRoute.InventoryScreen
import com.skgtecnologia.sisem.ui.navigation.MenuNavigationRoute.NewsScreen
import com.skgtecnologia.sisem.ui.navigation.MenuNavigationRoute.NotificationsScreen
import com.skgtecnologia.sisem.ui.navigation.MenuNavigationRoute.PreoperationalMenuScreen
import com.skgtecnologia.sisem.ui.navigation.MenuNavigationRoute.ShiftScreen
import com.skgtecnologia.sisem.ui.navigation.MenuNavigationRoute.SignatureAndFingerprint
import com.valkiria.uicomponents.R.drawable

// FIXME
object MenuItemModel {

    fun getDrawerInfoItemList(isAdmin: Boolean?): List<DrawerItemInfo> {
        return if (isAdmin == true) {
            drawerLeaderItems
        } else {
            drawerItems
        }
    }

    private val drawerItems = listOf(
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
            ShiftScreen,
            "Entrega de turno",
            drawable.ic_shift
        ),
        DrawerItemInfo(
            PreoperationalMenuScreen,
            "Preoperacional",
            drawable.ic_check
        ),
        DrawerItemInfo(
            HCEUDCScreen,
            "HCEUD",
            drawable.ic_hceud
        )
    )

    private val drawerLeaderItems = listOf(
        DrawerItemInfo(
            DeviceAuth,
            "Asociar dispositivo al vehículo",
            drawable.ic_incidents // FIXME: update with figma
        ),
        DrawerItemInfo(
            SignatureAndFingerprint,
            "Registrar firma y huella",
            drawable.ic_incidents // FIXME: update with figma
        )
    )
}

data class DrawerItemInfo(
    val drawerOption: MenuNavigationRoute,
    val title: String,
    @DrawableRes val drawableId: Int
)
