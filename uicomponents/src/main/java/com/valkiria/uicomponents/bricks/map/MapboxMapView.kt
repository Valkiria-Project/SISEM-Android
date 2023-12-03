package com.valkiria.uicomponents.bricks.map

import android.graphics.Bitmap
import androidx.appcompat.content.res.AppCompatResources
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons.Outlined
import androidx.compose.material.icons.outlined.Menu
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.DrawerState
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.viewinterop.NoOpUpdate
import androidx.core.graphics.drawable.toBitmap
import com.mapbox.api.directions.v5.models.RouteOptions
import com.mapbox.geojson.Point
import com.mapbox.maps.CameraOptions
import com.mapbox.maps.MapView
import com.mapbox.maps.Style
import com.mapbox.maps.plugin.animation.flyTo
import com.mapbox.maps.plugin.annotation.annotations
import com.mapbox.maps.plugin.annotation.generated.PointAnnotationManager
import com.mapbox.maps.plugin.annotation.generated.PointAnnotationOptions
import com.mapbox.maps.plugin.annotation.generated.createPointAnnotationManager
import com.mapbox.navigation.base.extensions.applyDefaultNavigationOptions
import com.mapbox.navigation.base.route.NavigationRoute
import com.mapbox.navigation.base.route.NavigationRouterCallback
import com.mapbox.navigation.base.route.RouterFailure
import com.mapbox.navigation.base.route.RouterOrigin
import com.mapbox.navigation.core.MapboxNavigation
import com.mapbox.navigation.core.lifecycle.MapboxNavigationApp
import com.mapbox.navigation.dropin.NavigationView
import com.valkiria.uicomponents.R
import com.valkiria.uicomponents.action.GenericUiAction.NotificationAction
import com.valkiria.uicomponents.bricks.notification.OnNotificationHandler
import com.valkiria.uicomponents.bricks.notification.model.NotificationData
import com.valkiria.uicomponents.components.incident.IncidentContent
import com.valkiria.uicomponents.components.incident.model.IncidentUiModel
import kotlinx.coroutines.launch
import timber.log.Timber

private const val MAP_ZOOM = 16.0

@Suppress("LongMethod", "LongParameterList")
@Composable
fun MapboxMapView(
    mapBoxNavigation: MapboxNavigation?,
    coordinates: Pair<Double, Double>,
    incident: IncidentUiModel?,
    drawerState: DrawerState,
    notificationData: NotificationData?,
    modifier: Modifier = Modifier,
    onNotificationAction: (notificationAction: NotificationAction) -> Unit,
    onAction: (idAph: Int) -> Unit
) {
    val context = LocalContext.current

    val locationPoint = Point.fromLngLat(coordinates.first, coordinates.second)
    val marker = remember(context) {
        AppCompatResources.getDrawable(context, R.drawable.ic_ambulance_marker)?.toBitmap()
    }

    val scope = rememberCoroutineScope()
    val scaffoldState = rememberBottomSheetScaffoldState()

    BottomSheetScaffold(
        sheetContent = {
            incident?.let {
                IncidentContent(it, onAction)
            }
        },
        scaffoldState = scaffoldState,
        sheetPeekHeight = if (incident != null) 140.dp else 0.dp
    ) { innerPadding ->
        Box(modifier.padding(innerPadding)) {
            val destinationPoint = Point.fromLngLat(-74.0711485, 4.6963453)
            if (incident != null) {
                MapboxNavigationAndroidView(
                    incident, locationPoint, destinationPoint, marker, modifier
                )
            } else {
                MapboxMapAndroidView(
                    incident, locationPoint, destinationPoint, marker, modifier
                )
            }

            IconButton(
                onClick = {
                    scope.launch {
                        drawerState.open()
                    }
                },
                modifier = Modifier
                    .padding(12.dp)
                    .align(Alignment.TopStart)
            ) {
                Icon(
                    imageVector = Outlined.Menu,
                    contentDescription = null,
                    modifier = Modifier
                        .background(color = MaterialTheme.colorScheme.primary)
                        .size(72.dp)
                        .padding(5.dp),
                    tint = Color.Black
                )
            }

            IconButton(
                onClick = {
                    // FIXME: Navigate to notifications screen / Drawer
                },
                modifier = Modifier
                    .padding(12.dp)
                    .align(Alignment.TopEnd)
            ) {
                Icon(
                    imageVector = Outlined.Notifications,
                    contentDescription = null,
                    modifier = Modifier
                        .background(color = MaterialTheme.colorScheme.primary)
                        .size(72.dp)
                        .padding(5.dp),
                    tint = Color.Black
                )
            }

            OnNotificationHandler(notificationData) {
                onNotificationAction(it)
                if (it.isDismiss.not()) {
                    // FIXME: Navigate to MapScreen if is type INCIDENT_ASSIGNED
                    Timber.d("Navigate to MapScreen")
                }
            }
        }
    }
}

@Composable
private fun MapboxMapAndroidView(
    incident: IncidentUiModel?,
    locationPoint: Point,
    destinationPoint: Point,
    marker: Bitmap?,
    modifier: Modifier
) {
    var pointAnnotationManager: PointAnnotationManager? by remember {
        mutableStateOf(null)
    }

    AndroidView(
        factory = {
            MapView(it).also { mapView ->
                mapView.getMapboxMap().loadStyleUri(Style.DARK)
                val annotationApi = mapView.annotations
                pointAnnotationManager = annotationApi.createPointAnnotationManager()
            }
        },
        update = { mapView ->
            pointAnnotationManager?.let {
                it.deleteAll()
                val pointAnnotationOptions = PointAnnotationOptions().apply {
                    withPoint(locationPoint)
                    marker?.let { bitmap ->
                        withIconImage(bitmap)
                    }
                }

                it.create(pointAnnotationOptions)
                mapView.getMapboxMap().flyTo(
                    CameraOptions.Builder()
                        .zoom(MAP_ZOOM)
                        .center(locationPoint)
                        .build()
                )
            }
            NoOpUpdate
        },
        modifier = modifier
    )
}

@Composable
private fun MapboxNavigationAndroidView(
    incident: IncidentUiModel?,
    locationPoint: Point,
    destinationPoint: Point,
    marker: Bitmap?,
    modifier: Modifier
) {
    var pointAnnotationManager: PointAnnotationManager? by remember {
        mutableStateOf(null)
    }

    AndroidView(
        factory = { context ->
            NavigationView(
                context = context,
                accessToken = "sk.eyJ1Ijoic2lzZW0iLCJhIjoiY2xwa2l5bmxmMDB0NDJrankxeG4yZWowMSJ9.o6RJNwx7MDzeUDturbT1LA"
            ).also { navigationView ->
                MapboxNavigationApp.current()!!.requestRoutes(
                    routeOptions = RouteOptions
                        .builder()
                        .applyDefaultNavigationOptions()
//                        .applyLanguageAndVoiceUnitOptions(this)
                        .coordinatesList(listOf(locationPoint, destinationPoint))
                        .alternatives(true)
                        .build(),
                    callback = object : NavigationRouterCallback {
                        override fun onCanceled(
                            routeOptions: RouteOptions,
                            routerOrigin: RouterOrigin
                        ) {
                            Timber.d("NavigationRouterCallback onCanceled")
                        }

                        override fun onFailure(
                            reasons: List<RouterFailure>,
                            routeOptions: RouteOptions
                        ) {
                            Timber.d("NavigationRouterCallback onFailure")
                        }

                        override fun onRoutesReady(
                            routes: List<NavigationRoute>,
                            routerOrigin: RouterOrigin
                        ) {
                            Timber.d("NavigationRouterCallback onRoutesReady")

                            navigationView.api.routeReplayEnabled(true)
                            navigationView.api.startActiveGuidance(routes)
                        }
                    }
                )
            }
        },
        update = { navigationView ->
            pointAnnotationManager?.let {
                it.deleteAll()
                val pointAnnotationOptions = PointAnnotationOptions().apply {
                    withPoint(locationPoint)
                    marker?.let { bitmap ->
                        withIconImage(bitmap)
                    }
                }

                it.create(pointAnnotationOptions)
            }
            NoOpUpdate
        },
        modifier = modifier
    )
}