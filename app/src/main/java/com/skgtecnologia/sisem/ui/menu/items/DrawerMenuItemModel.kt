package com.skgtecnologia.sisem.ui.menu.items

import android.content.Context
import androidx.annotation.DrawableRes
import com.skgtecnologia.sisem.R
import com.skgtecnologia.sisem.ui.navigation.MainNavigationRoute
import com.skgtecnologia.sisem.ui.navigation.MainNavigationRoute.CertificationsScreen
import com.skgtecnologia.sisem.ui.navigation.MainNavigationRoute.DeviceAuth
import com.skgtecnologia.sisem.ui.navigation.MainNavigationRoute.DrivingGuideScreen
import com.skgtecnologia.sisem.ui.navigation.MainNavigationRoute.HCEUDCScreen
import com.skgtecnologia.sisem.ui.navigation.MainNavigationRoute.IncidentScreen
import com.skgtecnologia.sisem.ui.navigation.MainNavigationRoute.InventoryScreen
import com.skgtecnologia.sisem.ui.navigation.MainNavigationRoute.NewsScreen
import com.skgtecnologia.sisem.ui.navigation.MainNavigationRoute.NotificationsScreen
import com.skgtecnologia.sisem.ui.navigation.MainNavigationRoute.PreoperationalMainScreen
import com.skgtecnologia.sisem.ui.navigation.MainNavigationRoute.ShiftScreen
import com.skgtecnologia.sisem.ui.navigation.MainNavigationRoute.SignatureAndFingerprint
import com.valkiria.uicomponents.R.drawable

data class DrawerMenuItemModel(
    val drawerOption: MainNavigationRoute,
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
        PreoperationalMainScreen,
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
