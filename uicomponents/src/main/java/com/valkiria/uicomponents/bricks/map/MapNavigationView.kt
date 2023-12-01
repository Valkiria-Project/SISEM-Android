package com.valkiria.uicomponents.bricks.map

import android.graphics.Bitmap
import androidx.appcompat.content.res.AppCompatResources
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons.Outlined
import androidx.compose.material.icons.outlined.Menu
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.viewinterop.NoOpUpdate
import androidx.core.graphics.drawable.toBitmap
import com.mapbox.geojson.Point
import com.mapbox.maps.CameraOptions
import com.mapbox.maps.MapView
import com.mapbox.maps.Style
import com.mapbox.maps.plugin.animation.flyTo
import com.mapbox.maps.plugin.annotation.annotations
import com.mapbox.maps.plugin.annotation.generated.PointAnnotationManager
import com.mapbox.maps.plugin.annotation.generated.PointAnnotationOptions
import com.mapbox.maps.plugin.annotation.generated.createPointAnnotationManager
import com.valkiria.uicomponents.R
import com.valkiria.uicomponents.action.GenericUiAction.NotificationAction
import com.valkiria.uicomponents.bricks.notification.OnNotificationHandler
import com.valkiria.uicomponents.bricks.notification.model.NotificationData
import com.valkiria.uicomponents.components.incident.IncidentContent
import com.valkiria.uicomponents.components.incident.model.IncidentUiModel
import kotlinx.coroutines.launch
import timber.log.Timber

private const val MAP_ZOOM = 16.0

@Suppress("LongParameterList")
@Composable
fun MapNavigationView(
    coordinates: Pair<Double, Double>,
    incident: IncidentUiModel?,
    drawerState: DrawerState,
    notificationData: NotificationData?,
    modifier: Modifier = Modifier,
    onNotificationAction: (notificationAction: NotificationAction) -> Unit,
    onAction: (idAph: Int) -> Unit
) {
    val context = LocalContext.current

    val point = Point.fromLngLat(coordinates.first, coordinates.second)
    val marker = remember(context) {
        AppCompatResources.getDrawable(context, R.drawable.ic_marker)?.toBitmap()
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
            MapboxAndroidView(point, marker, modifier)

            IconButton(
                onClick = {
                    scope.launch {
                        drawerState.open()
                    }
                },
                modifier = Modifier.padding(12.dp)
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
private fun MapboxAndroidView(
    point: Point,
    marker: Bitmap?,
    modifier: Modifier
) {
    var pointAnnotationManager: PointAnnotationManager? by remember {
        mutableStateOf(null)
    }

    AndroidView(
        factory = {
            MapView(it).also { mapView ->
                mapView.getMapboxMap().loadStyleUri(Style.MAPBOX_STREETS)
                val annotationApi = mapView.annotations
                pointAnnotationManager = annotationApi.createPointAnnotationManager()
            }
        },
        update = { mapView ->
            pointAnnotationManager?.let {
                it.deleteAll()
                val pointAnnotationOptions = PointAnnotationOptions().apply {
                    withPoint(point)
                    marker?.let { bitmap ->
                        withIconImage(bitmap)
                    }
                }

                it.create(pointAnnotationOptions)
                mapView.getMapboxMap().flyTo(
                    CameraOptions.Builder()
                        .zoom(MAP_ZOOM)
                        .center(point)
                        .build()
                )
            }
            NoOpUpdate
        },
        modifier = modifier
    )
}
