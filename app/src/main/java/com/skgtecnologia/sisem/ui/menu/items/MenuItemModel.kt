package com.skgtecnologia.sisem.ui.menu.items

import android.content.Context
import androidx.annotation.DrawableRes
import com.skgtecnologia.sisem.R
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

fun getDrawerInfoItemList(context: Context, isAdmin: Boolean?): List<DrawerItemInfo> {
    return if (isAdmin == true) {
        getLeaderDrawerItems(context)
    } else {
        getDrawerItems(context)
    }
}

private fun getDrawerItems(context: Context) = listOf(
    DrawerItemInfo(
        IncidentScreen,
        context.getString(R.string.drawer_incident),
        drawable.ic_incidents
    ),
    DrawerItemInfo(
        InventoryScreen,
        context.getString(R.string.drawer_inventory),
        drawable.ic_inventory
    ),
    DrawerItemInfo(
        NotificationsScreen,
        context.getString(R.string.drawer_notifications),
        drawable.ic_notification
    ),
    DrawerItemInfo(
        DrivingGuideScreen,
        context.getString(R.string.drawer_guides),
        drawable.ic_driver
    ),
    DrawerItemInfo(
        CertificationsScreen,
        context.getString(R.string.drawer_certifications),
        drawable.ic_certifications
    ),
    DrawerItemInfo(
        NewsScreen,
        context.getString(R.string.drawer_novelties),
        drawable.ic_news
    ),
    DrawerItemInfo(
        ShiftScreen,
        context.getString(R.string.drawer_turn_shift),
        drawable.ic_shift
    ),
    DrawerItemInfo(
        PreoperationalMenuScreen,
        context.getString(R.string.drawer_pre_operational),
        drawable.ic_check
    ),
    DrawerItemInfo(
        HCEUDCScreen,
        context.getString(R.string.drawer_hceud),
        drawable.ic_hceud
    )
)

@Suppress("UnusedPrivateMember")
private fun getLeaderDrawerItems(context: Context) = listOf(
    DrawerItemInfo(
        DeviceAuth,
        "Asociar dispositivo al vehículo", // FIXME: Hardcoded data
        drawable.ic_incidents // FIXME: update with figma
    ),
    DrawerItemInfo(
        SignatureAndFingerprint,
        "Registrar firma y huella", // FIXME: Hardcoded data
        drawable.ic_incidents // FIXME: update with figma
    )
)

data class DrawerItemInfo(
    val drawerOption: MenuNavigationRoute,
    val title: String,
    @DrawableRes val drawableId: Int
)
