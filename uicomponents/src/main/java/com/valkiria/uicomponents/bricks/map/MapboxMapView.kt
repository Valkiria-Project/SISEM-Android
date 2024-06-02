package com.valkiria.uicomponents.bricks.map

import androidx.appcompat.content.res.AppCompatResources
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons.Outlined
import androidx.compose.material.icons.outlined.Menu
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.DrawerState
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.core.graphics.drawable.toBitmap
import com.mapbox.geojson.Point
import com.mapbox.maps.MapboxExperimental
import com.mapbox.maps.extension.compose.MapboxMap
import com.mapbox.maps.extension.compose.animation.viewport.MapViewportState
import com.valkiria.uicomponents.R
import com.valkiria.uicomponents.action.GenericUiAction.NotificationAction
import com.valkiria.uicomponents.bricks.banner.BannerUiModel
import com.valkiria.uicomponents.bricks.banner.OnBannerHandler
import com.valkiria.uicomponents.bricks.notification.NotificationRowView
import com.valkiria.uicomponents.bricks.notification.NotificationUiModel
import com.valkiria.uicomponents.bricks.notification.OnNotificationHandler
import com.valkiria.uicomponents.bricks.notification.model.NotificationData
import com.valkiria.uicomponents.components.incident.IncidentContent
import com.valkiria.uicomponents.components.incident.model.IncidentUiModel
import kotlinx.coroutines.launch
import timber.log.Timber

@OptIn(MapboxExperimental::class)
@Suppress("LongMethod", "LongParameterList", "MagicNumber")
@Composable
fun MapboxMapView(
    coordinates: Pair<Double, Double>,
    incident: IncidentUiModel?,
    notifications: List<NotificationUiModel>?,
    drawerState: DrawerState,
    notificationData: NotificationData?,
    incidentErrorData: BannerUiModel?,
    modifier: Modifier = Modifier,
    onNotificationAction: (notificationAction: NotificationAction) -> Unit,
    onIncidentErrorAction: () -> Unit,
    onAction: (idAph: Int) -> Unit
) {
    val context = LocalContext.current
    val marker = remember(context) {
        AppCompatResources.getDrawable(context, R.drawable.ic_ambulance_marker)?.toBitmap()
    }

    val locationPoint by remember(coordinates) {
        mutableStateOf(Point.fromLngLat(coordinates.first, coordinates.second))
    }
    val rememberedIncident by remember(incident) { mutableStateOf(incident) }
    val destinationPoint by remember(incident?.longitude to incident?.latitude) {
        val destinationPoint = if (incident?.longitude != null && incident.latitude != null) {
            Point.fromLngLat(incident.longitude, incident.latitude)
        } else {
            null
        }

        mutableStateOf(destinationPoint)
    }

    val scaffoldState = rememberBottomSheetScaffoldState()
    val scope = rememberCoroutineScope()
    var showNotificationsDialog by remember { mutableStateOf(false) }

    BottomSheetScaffold(
        sheetContent = {
            rememberedIncident?.let {
                IncidentContent(incidentUiModel = it, onAction = onAction)
            }
        },
        scaffoldState = scaffoldState,
        sheetPeekHeight = if (rememberedIncident != null) 140.dp else 0.dp,
        sheetSwipeEnabled = false
    ) { innerPadding ->
        Box(modifier.padding(innerPadding)) {
            val accessToken = stringResource(id = R.string.mapbox_access_token)

            MapboxMap(
                modifier = Modifier.fillMaxSize(),
                mapViewportState = MapViewportState().apply {
                    setCameraOptions {
                        zoom(2.0)
                        center(Point.fromLngLat(-98.0, 39.5))
                        pitch(0.0)
                        bearing(0.0)
                    }
                },
            )

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
                    showNotificationsDialog = true
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
                    Timber.d("Navigate to MapScreen")
                }
            }

            NotificationsRenderer(showNotificationsDialog, notifications) {
                showNotificationsDialog = false
            }

            OnBannerHandler(uiModel = incidentErrorData) {
                onIncidentErrorAction()
            }
        }
    }
}

//@Suppress("LongParameterList", "UnusedPrivateMember")
//@Composable
//private fun MapboxNavigationAndroidView(
//    locationPoint: Point,
//    destinationPoint: Point?,
//    marker: Bitmap?,
//    modifier: Modifier,
//    accessToken: String
//) {
//    val pointAnnotationManager: PointAnnotationManager? by remember {
//        mutableStateOf(null)
//    }
//
//    AndroidView(
//        factory = { context ->
//            NavigationView(
//                context = context,
//                accessToken = accessToken
//            ).also { navigationView ->
//                navigationView.customizeViewOptions {
//                    mapStyleUriDay = NAVIGATION_NIGHT_STYLE
//                    mapStyleUriNight = NAVIGATION_NIGHT_STYLE
//                    distanceFormatterOptions = DistanceFormatterOptions.Builder(context)
//                        .unitType(UnitType.METRIC)
//                        .build()
//                    enableMapLongClickIntercept = false
//                    showEndNavigationButton = false
//                }
//
//                navigationView.customizeViewBinders {
//                    maneuverBinder = EmptyBinder()
//                    speedLimitBinder = EmptyBinder()
//                    actionToggleAudioButtonBinder = EmptyBinder()
//                    actionButtonsBinder = EmptyBinder()
//                }
//            }
//        },
//        update = { navigationView ->
//            pointAnnotationManager?.let { annotationManager ->
//                annotationManager.deleteAll()
//                val pointAnnotationOptions = PointAnnotationOptions().apply {
//                    withPoint(locationPoint)
//                    marker?.let { bitmap ->
//                        withIconImage(bitmap)
//                    }
//                }
//
//                annotationManager.create(pointAnnotationOptions)
//            }
//
//            val defaultAnnotations = listOf(
//                DirectionsCriteria.ANNOTATION_DURATION,
//                DirectionsCriteria.ANNOTATION_DISTANCE
//            )
//
//            navigationView.requestRoutes(locationPoint, destinationPoint, defaultAnnotations)
//
//            NoOpUpdate
//        },
//        modifier = modifier
//    )
//}

private const val ES_LANGUAGE = "es"

//private fun NavigationView.requestRoutes(
//    locationPoint: Point,
//    destinationPoint: Point?,
//    defaultAnnotations: List<String> = listOf(DirectionsCriteria.ANNOTATION_DURATION)
//) {
//    if (destinationPoint == null) {
//        api.startFreeDrive()
//    } else {
//        MapboxNavigationApp.current()!!.requestRoutes(
//            routeOptions = RouteOptions
//                .builder()
//                .steps(true)
//                .language(ES_LANGUAGE)
//                .annotationsList(defaultAnnotations)
//                .continueStraight(true)
//                .enableRefresh(true)
//                .profile(DirectionsCriteria.PROFILE_DRIVING_TRAFFIC)
//                .overview(DirectionsCriteria.OVERVIEW_FULL)
//                .roundaboutExits(false)
//                .voiceInstructions(false)
//                .bannerInstructions(false)
//                .applyLanguageAndVoiceUnitOptions(context)
//                .coordinatesList(listOf(locationPoint, destinationPoint))
//                .alternatives(false)
//                .build(),
//            callback = object : NavigationRouterCallback {
//                override fun onCanceled(
//                    routeOptions: RouteOptions,
//                    routerOrigin: RouterOrigin
//                ) {
//                    Timber.d("NavigationRouterCallback onCanceled")
//                    api.startFreeDrive()
//                }
//
//                override fun onFailure(
//                    reasons: List<RouterFailure>,
//                    routeOptions: RouteOptions
//                ) {
//                    Timber.d("NavigationRouterCallback onFailure")
//                    api.startFreeDrive()
//                }
//
//                override fun onRoutesReady(
//                    routes: List<NavigationRoute>,
//                    routerOrigin: RouterOrigin
//                ) {
//                    Timber.d("NavigationRouterCallback onRoutesReady")
//                    api.startActiveGuidance(routes)
//                }
//            }
//        )
//    }
//}

@Composable
private fun NotificationsRenderer(
    showNotificationsDialog: Boolean,
    notifications: List<NotificationUiModel>?,
    onAction: () -> Unit
) {
    if (showNotificationsDialog) {
        Dialog(
            onDismissRequest = { onAction() },
            properties = DialogProperties(usePlatformDefaultWidth = false)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background.copy(alpha = 0.8f))
                    .clickable(
                        enabled = false,
                        onClick = {}
                    )
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.Top
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = ImageVector.vectorResource(id = R.drawable.ic_back),
                        contentDescription = null,
                        modifier = Modifier
                            .clickable { onAction() }
                            .padding(20.dp)
                            .size(32.dp),
                        tint = Color.White
                    )

                    Text(
                        text = stringResource(R.string.notifications_title),
                        modifier = Modifier
                            .weight(1f)
                            .padding(20.dp),
                        color = Color.White,
                        style = MaterialTheme.typography.displayLarge.copy(
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold
                        )
                    )
                }

                notifications?.forEach { notificationUiModel ->
                    NotificationRowView(notificationUiModel)
                }
            }
        }
    }
}
