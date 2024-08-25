@file:Suppress("TooManyFunctions")

package com.skgtecnologia.sisem.ui.navigation

import android.os.Build
import android.os.Bundle
import android.os.Parcelable
import androidx.navigation.NavType
import com.skgtecnologia.sisem.di.operation.OperationRole
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

const val FINDING = "finding"
const val REPORT = "report"

fun getAppStartDestination(model: StartupNavigationModel?): NavGraph {
    return if (model == null) {
        NavGraph.AuthNavGraph
    } else when {
        model.isAdmin && !model.vehicleCode.isNullOrEmpty() -> NavGraph.MainNavGraph
        model.isTurnStarted && !model.requiresPreOperational -> NavGraph.MainNavGraph
        else -> NavGraph.AuthNavGraph
    }
}

fun getAuthStartDestination(model: StartupNavigationModel?): AuthRoute = when {
    model?.isWarning == true -> AuthRoute.ChangePasswordRoute
    model?.isAdmin == true -> AuthRoute.DeviceAuthRoute(DeviceAppState.APP_STARTED.name)
    model?.requiresPreOperational == true -> AuthRoute.PreOperationalRoute(OperationRole.DRIVER)
    else -> AuthRoute.AuthCardsRoute
}

// TODO: Move this to extensions
inline fun <reified T : Parcelable> parcelableType(
    isNullableAllowed: Boolean = false,
    json: Json = Json,
) = object : NavType<T>(isNullableAllowed = isNullableAllowed) {
    override fun get(bundle: Bundle, key: String) =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            bundle.getParcelable(key, T::class.java)
        } else {
            @Suppress("DEPRECATION")
            bundle.getParcelable(key)
        }

    override fun parseValue(value: String): T = json.decodeFromString(value)

    override fun serializeAsValue(value: T): String = json.encodeToString(value)

    override fun put(bundle: Bundle, key: String, value: T) = bundle.putParcelable(key, value)
}
