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

data class DrawerMenuItemModel(
    val drawerOption: MenuNavigationRoute,
    val title: String,
    @DrawableRes val drawableId: Int
)

fun getDrawerMenuItemList(context: Context, isAdmin: Boolean?): List<DrawerMenuItemModel> {
    return if (isAdmin == true) {
        getLeaderDrawerItems(context)
    } else {
        getDrawerItems(context)
    }
}

private fun getDrawerItems(context: Context) = listOf(
    DrawerMenuItemModel(
        IncidentScreen,
        context.getString(R.string.drawer_incident),
        drawable.ic_incidents
    ),
    DrawerMenuItemModel(
        InventoryScreen,
        context.getString(R.string.drawer_inventory),
        drawable.ic_inventory
    ),
    DrawerMenuItemModel(
        NotificationsScreen,
        context.getString(R.string.drawer_notifications),
        drawable.ic_notification
    ),
    DrawerMenuItemModel(
        DrivingGuideScreen,
        context.getString(R.string.drawer_guides),
        drawable.ic_driver
    ),
    DrawerMenuItemModel(
        CertificationsScreen,
        context.getString(R.string.drawer_certifications),
        drawable.ic_certifications
    ),
    DrawerMenuItemModel(
        NewsScreen,
        context.getString(R.string.drawer_novelties),
        drawable.ic_news
    ),
    DrawerMenuItemModel(
        ShiftScreen,
        context.getString(R.string.drawer_turn_shift),
        drawable.ic_shift
    ),
    DrawerMenuItemModel(
        PreoperationalMenuScreen,
        context.getString(R.string.drawer_pre_operational),
        drawable.ic_check
    ),
    DrawerMenuItemModel(
        HCEUDCScreen,
        context.getString(R.string.drawer_hceud),
        drawable.ic_hceud
    )
)

@Suppress("UnusedPrivateMember")
private fun getLeaderDrawerItems(context: Context) = listOf(
    DrawerMenuItemModel(
        DeviceAuth,
        "Asociar dispositivo al vehículo", // FIXME: Hardcoded data
        drawable.ic_incidents // FIXME: update with figma
    ),
    DrawerMenuItemModel(
        SignatureAndFingerprint,
        "Registrar firma y huella", // FIXME: Hardcoded data
        drawable.ic_incidents // FIXME: update with figma
    )
)
