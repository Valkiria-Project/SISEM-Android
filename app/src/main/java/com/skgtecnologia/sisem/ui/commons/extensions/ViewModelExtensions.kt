package com.skgtecnologia.sisem.ui.commons.extensions

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import com.valkiria.uicomponents.components.BodyRowModel

const val STATE_FLOW_STARTED_TIME = 5000L

@Composable
inline fun <reified T : ViewModel> NavBackStackEntry.sharedViewModel(
    navController: NavController
): T {
    val navGraphRoute = destination.parent?.route ?: hiltViewModel()
    val parentEntry = remember(this) {
        navController.getBackStackEntry(navGraphRoute)
    }

    return hiltViewModel(parentEntry)
}

fun <T : BodyRowModel> updateBodyModel(
    uiModels: List<T>?,
    updater: (T) -> T
): List<T> {
    return uiModels.orEmpty().map {
        updater(it)
    }
}

fun <T : BodyRowModel> updateBodyModel(
    uiModels: List<T>?,
    identifier: String,
    updater: (T) -> T
): List<T> {
    return uiModels.orEmpty().map {
        if (it.identifier == identifier) {
            updater(it)
        } else {
            it
        }
    }
}

fun <T : BodyRowModel> updateBodyModel(
    uiModels: List<T>?,
    identifiers: List<String>,
    updater: (T) -> T
): List<T> {
    return uiModels.orEmpty().map {
        if (identifiers.contains(it.identifier)) {
            updater(it)
        } else {
            it
        }
    }
}
