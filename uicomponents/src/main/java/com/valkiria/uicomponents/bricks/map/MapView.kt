package com.valkiria.uicomponents.bricks.map

import androidx.appcompat.content.res.AppCompatResources
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons.Outlined
import androidx.compose.material.icons.outlined.Menu
import androidx.compose.material3.DrawerState
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import com.valkiria.uicomponents.bricks.notification.NotificationView
import com.valkiria.uicomponents.mocks.getAssignedIncidentNotificationUiModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import timber.log.Timber

@Suppress("LongMethod", "MagicNumber")
@Composable
fun MapView(
    coordinates: Pair<Double, Double>,
    drawerState: DrawerState,
    hasNotification: Boolean,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val coroutineScope: CoroutineScope = rememberCoroutineScope()

    val point = Point.fromLngLat(coordinates.first, coordinates.second)
    val marker = remember(context) {
        AppCompatResources.getDrawable(context, R.drawable.ic_marker)?.toBitmap()
    }
    var pointAnnotationManager: PointAnnotationManager? by remember {
        mutableStateOf(null)
    }

    Box(modifier = modifier) {
        AndroidView(
            factory = {
                MapView(it).also { mapView ->
                    mapView.getMapboxMap().loadStyleUri(Style.MAPBOX_STREETS)
                    val annotationApi = mapView.annotations
                    pointAnnotationManager = annotationApi.createPointAnnotationManager()
                }
            },
            update = { mapView ->
                if (point != null) {
                    pointAnnotationManager?.let {
                        it.deleteAll()
                        val pointAnnotationOptions = PointAnnotationOptions().apply {
                            withPoint(point)
                            marker?.let { bitmap ->
                                withIconImage(bitmap)
                            }
                        }

                        it.create(pointAnnotationOptions)
                        mapView.getMapboxMap()
                            .flyTo(CameraOptions.Builder().zoom(16.0).center(point).build())
                    }
                }
                NoOpUpdate
            },
            modifier = modifier
        )

        IconButton(
            onClick = {
                coroutineScope.launch {
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

        if (hasNotification) {
            NotificationView(uiModel = getAssignedIncidentNotificationUiModel()) {
                Timber.d("Notification action $it")
            }
        }
    }
}
