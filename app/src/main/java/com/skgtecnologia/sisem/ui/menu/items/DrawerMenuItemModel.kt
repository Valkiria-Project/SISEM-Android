package com.skgtecnologia.sisem.ui.menu.items

import android.content.Context
import androidx.annotation.DrawableRes
import com.skgtecnologia.sisem.R
import com.skgtecnologia.sisem.ui.navigation.MainRoute
import com.skgtecnologia.sisem.ui.navigation.NavRoute
import com.skgtecnologia.sisem.ui.navigation.ReportRoute
import com.valkiria.uicomponents.R.drawable

data class DrawerMenuItemModel(
    val drawerOption: NavRoute,
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
        MainRoute.IncidentsRoute,
        context.getString(R.string.drawer_incident),
        drawable.ic_incidents
    ),
    DrawerMenuItemModel(
        MainRoute.InventoryRoute,
        context.getString(R.string.drawer_inventory),
        drawable.ic_inventory
    ),
    /* TECH-DEBT: Revert later
    DrawerMenuItemModel(
        MainRoute.NotificationsRoute,
        context.getString(R.string.drawer_notifications),
        drawable.ic_notification
    ),
    DrawerMenuItemModel(
        MainRoute.DrivingGuideRoute,
        context.getString(R.string.drawer_guides),
        drawable.ic_folder
    ),
    DrawerMenuItemModel(
        MainRoute.CertificationsRoute,
        context.getString(R.string.drawer_certifications),
        drawable.ic_certifications
    ),
     */
    DrawerMenuItemModel(
        ReportRoute.AddReportRoleRoute,
        context.getString(R.string.drawer_novelties),
        drawable.ic_news
    ),
    /* TECH-DEBT: Revert later
    DrawerMenuItemModel(
        MainRoute.ShiftRoute,
        context.getString(R.string.drawer_turn_shift),
        drawable.ic_shift
    ),
     */
    DrawerMenuItemModel(
        MainRoute.PreoperationalMainRoute,
        context.getString(R.string.drawer_pre_operational),
        drawable.ic_check
    ),
    /* TECH-DEBT: Revert later
    DrawerMenuItemModel(
        MainRoute.HCEUDCRoute,
        context.getString(R.string.drawer_hceud),
        drawable.ic_hceud
    ),
     */
    DrawerMenuItemModel(
        MainRoute.PreStretcherRetentionRoute,
        context.getString(R.string.drawer_stretcher_retention),
        drawable.ic_stretcher
    )
)

private fun getLeaderDrawerItems(context: Context) = listOf(
    DrawerMenuItemModel(
        MainRoute.DeviceAuthMainRoute,
        context.getString(R.string.drawer_device_auth),
        drawable.ic_ambulance
    ),
    DrawerMenuItemModel(
        MainRoute.InitSignatureRoute,
        context.getString(R.string.drawer_signature_and_fingerprint),
        drawable.ic_fingerprint
    )
)
