package com.skgtecnologia.sisem.ui.map

import android.annotation.SuppressLint
import android.content.res.Resources
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.mapbox.api.directions.v5.models.Bearing
import com.mapbox.api.directions.v5.models.RouteOptions
import com.mapbox.common.location.Location
import com.mapbox.geojson.Point
import com.mapbox.maps.EdgeInsets
import com.mapbox.maps.ImageHolder
import com.mapbox.maps.Style
import com.mapbox.maps.plugin.LocationPuck2D
import com.mapbox.maps.plugin.animation.camera
import com.mapbox.maps.plugin.compass.compass
import com.mapbox.maps.plugin.gestures.gestures
import com.mapbox.maps.plugin.locationcomponent.OnIndicatorPositionChangedListener
import com.mapbox.maps.plugin.locationcomponent.location
import com.mapbox.navigation.base.ExperimentalPreviewMapboxNavigationAPI
import com.mapbox.navigation.base.TimeFormat
import com.mapbox.navigation.base.extensions.applyDefaultNavigationOptions
import com.mapbox.navigation.base.extensions.applyLanguageAndVoiceUnitOptions
import com.mapbox.navigation.base.formatter.DistanceFormatterOptions
import com.mapbox.navigation.base.formatter.UnitType
import com.mapbox.navigation.base.route.NavigationRoute
import com.mapbox.navigation.base.route.NavigationRouterCallback
import com.mapbox.navigation.base.route.RouterFailure
import com.mapbox.navigation.base.trip.model.RouteLegProgress
import com.mapbox.navigation.base.trip.model.RouteProgress
import com.mapbox.navigation.core.MapboxNavigation
import com.mapbox.navigation.core.arrival.ArrivalObserver
import com.mapbox.navigation.core.directions.session.RoutesObserver
import com.mapbox.navigation.core.lifecycle.MapboxNavigationObserver
import com.mapbox.navigation.core.lifecycle.requireMapboxNavigation
import com.mapbox.navigation.core.trip.session.LocationMatcherResult
import com.mapbox.navigation.core.trip.session.LocationObserver
import com.mapbox.navigation.core.trip.session.RouteProgressObserver
import com.mapbox.navigation.tripdata.progress.api.MapboxTripProgressApi
import com.mapbox.navigation.tripdata.progress.model.DistanceRemainingFormatter
import com.mapbox.navigation.tripdata.progress.model.EstimatedTimeToArrivalFormatter
import com.mapbox.navigation.tripdata.progress.model.PercentDistanceTraveledFormatter
import com.mapbox.navigation.tripdata.progress.model.TimeRemainingFormatter
import com.mapbox.navigation.tripdata.progress.model.TripProgressUpdateFormatter
import com.mapbox.navigation.ui.maps.camera.NavigationCamera
import com.mapbox.navigation.ui.maps.camera.data.MapboxNavigationViewportDataSource
import com.mapbox.navigation.ui.maps.camera.lifecycle.NavigationBasicGesturesHandler
import com.mapbox.navigation.ui.maps.camera.state.NavigationCameraState
import com.mapbox.navigation.ui.maps.camera.transition.NavigationCameraTransitionOptions
import com.mapbox.navigation.ui.maps.location.NavigationLocationProvider
import com.mapbox.navigation.ui.maps.route.arrow.api.MapboxRouteArrowApi
import com.mapbox.navigation.ui.maps.route.arrow.api.MapboxRouteArrowView
import com.mapbox.navigation.ui.maps.route.arrow.model.RouteArrowOptions
import com.mapbox.navigation.ui.maps.route.callout.api.MapboxRouteCalloutApi
import com.mapbox.navigation.ui.maps.route.callout.api.MapboxRouteCalloutView
import com.mapbox.navigation.ui.maps.route.callout.model.MapboxRouteCalloutApiOptions
import com.mapbox.navigation.ui.maps.route.callout.model.MapboxRouteCalloutViewOptions
import com.mapbox.navigation.ui.maps.route.callout.model.RouteCalloutType
import com.mapbox.navigation.ui.maps.route.line.api.MapboxRouteLineApi
import com.mapbox.navigation.ui.maps.route.line.api.MapboxRouteLineView
import com.mapbox.navigation.ui.maps.route.line.model.MapboxRouteLineApiOptions
import com.mapbox.navigation.ui.maps.route.line.model.MapboxRouteLineViewOptions
import com.skgtecnologia.sisem.R
import com.skgtecnologia.sisem.databinding.FragmentMapBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import timber.log.Timber
import kotlin.time.Duration.Companion.minutes

private const val PADDING_TOP_SMALL = 140.0
private const val PADDING_TOP_LARGE = 180.0
private const val PADDING_HORIZONTAL = 40.0
private const val PADDING_BOTTOM_SMALL = 120.0
private const val PADDING_BOTTOM_LARGE = 150.0
private const val ROUTE_CLICK_PADDING_DP = 80f
private const val INCIDENT_SHEET_PEEK_DP = 140f

@Suppress("TooManyFunctions")
@OptIn(ExperimentalPreviewMapboxNavigationAPI::class)
@AndroidEntryPoint
class MapFragment : Fragment(R.layout.fragment_map) {

    // Only valid between onCreateView and onDestroyView. Callbacks that can outlive
    // the view must read [mapBinding] defensively instead of assuming it.
    private var mapBinding: FragmentMapBinding? = null
    private val binding
        get() = requireNotNull(mapBinding) { "Accessed binding outside the view lifecycle" }
    val viewModel: MapFragmentViewModel by viewModels()

    private var destinationLocation: Location? = null

    // Last coordinates used for a successful route request. findRoute() is skipped
    // when the incoming destination matches this value to avoid resetting navigation
    // on every uiState emission that carries the same incident location.
    private var lastRoutedDestination: Pair<Double, Double>? = null

    private lateinit var navigationCamera: NavigationCamera
    private lateinit var routeArrowView: MapboxRouteArrowView
    private lateinit var tripProgressApi: MapboxTripProgressApi
    private lateinit var viewportDataSource: MapboxNavigationViewportDataSource

    private val navigationLocationProvider = NavigationLocationProvider()

    private val onPositionChangedListener = OnIndicatorPositionChangedListener { point ->
        val result = routeLineApi.updateTraveledRouteLine(point)
        mapBinding?.mapView?.mapboxMap?.style?.apply {
            routeLineView.renderRouteLineUpdate(this, result)
        }
    }

    private val routeArrowApi: MapboxRouteArrowApi = MapboxRouteArrowApi()

    private val routeCalloutApiOptions: MapboxRouteCalloutApiOptions by lazy {
        MapboxRouteCalloutApiOptions.Builder()
            .routeCalloutType(RouteCalloutType.RouteDurations)
            .similarDurationDelta(1.minutes)
            .build()
    }

    private val routeCalloutApi by lazy {
        MapboxRouteCalloutApi(routeCalloutApiOptions)
    }

    private val routeCalloutViewOptions: MapboxRouteCalloutViewOptions by lazy {
        MapboxRouteCalloutViewOptions.Builder()
            .selectedBackgroundColor(R.color.sisem_route_callout_background)
            .selectedTextColor(android.R.color.white)
            .backgroundColor(android.R.color.darker_gray)
            .build()
    }

    // Bound to the MapView, so it must be rebuilt whenever the view is recreated —
    // a lazy here would keep rendering into the destroyed MapView.
    private var routeCalloutView: MapboxRouteCalloutView? = null

    private var isNavigationInitializedForView = false

    private val routeLineApiOptions: MapboxRouteLineApiOptions by lazy {
        MapboxRouteLineApiOptions.Builder()
            .vanishingRouteLineEnabled(true)
            .build()
    }

    private val routeLineApi: MapboxRouteLineApi by lazy {
        MapboxRouteLineApi(routeLineApiOptions)
    }

    private val routeLineViewOptions: MapboxRouteLineViewOptions by lazy {
        MapboxRouteLineViewOptions.Builder(requireActivity())
            .routeLineBelowLayerId("road-label-navigation")
            .build()
    }

    private val routeLineView by lazy {
        MapboxRouteLineView(routeLineViewOptions)
    }

    private val mapboxNavigation: MapboxNavigation by requireMapboxNavigation(
        onResumedObserver = object : MapboxNavigationObserver {
            @SuppressLint("MissingPermission")
            override fun onAttached(mapboxNavigation: MapboxNavigation) {
                mapboxNavigation.registerArrivalObserver(arrivalObserver)
                mapboxNavigation.registerRoutesObserver(routesObserver)
                mapboxNavigation.registerLocationObserver(locationObserver)
                mapboxNavigation.registerRouteProgressObserver(routeProgressObserver)
                // Guard against SecurityException on Android 14+ when location
                // permission is absent or the app is not yet in eligible FGS state.
                val hasLocation = androidx.core.content.ContextCompat.checkSelfPermission(
                    requireContext(),
                    android.Manifest.permission.ACCESS_FINE_LOCATION
                ) == android.content.pm.PackageManager.PERMISSION_GRANTED
                if (hasLocation) {
                    mapboxNavigation.startTripSession()
                } else {
                    // Explicitly stop so Android does not restart NavigationNotificationService.
                    mapboxNavigation.stopTripSession()
                    Timber.w("Skipping startTripSession — location permission not granted")
                }
            }

            override fun onDetached(mapboxNavigation: MapboxNavigation) {
                mapboxNavigation.unregisterArrivalObserver(arrivalObserver)
                mapboxNavigation.unregisterRoutesObserver(routesObserver)
                mapboxNavigation.unregisterLocationObserver(locationObserver)
                mapboxNavigation.unregisterRouteProgressObserver(routeProgressObserver)
            }
        },
        onInitialize = this::initNavigation
    )

    private val arrivalObserver: ArrivalObserver = object : ArrivalObserver {
        override fun onFinalDestinationArrival(routeProgress: RouteProgress) {
            Timber.d("onFinalDestinationArrival")
        }

        override fun onNextRouteLegStart(routeLegProgress: RouteLegProgress) {
            Timber.d("onNextRouteLegStart")
        }

        override fun onWaypointArrival(routeProgress: RouteProgress) {
            Timber.d("onWaypointArrival")
        }
    }

    private val routesObserver = RoutesObserver { routesResult ->
        if (routesResult.navigationRoutes.isNotEmpty()) {
            // generate route geometries asynchronously and render them
            routeLineApi.setNavigationRoutes(
                routesResult.navigationRoutes
            ) { value ->
                binding.mapView.mapboxMap.style?.apply {
                    routeLineView.renderRouteDrawData(this, value)
                }
            }

            val metadata = mapboxNavigation.getAlternativeMetadataFor(routesResult.navigationRoutes)
            routeCalloutApi.setNavigationRoutes(
                newRoutes = routesResult.navigationRoutes,
                alternativeRoutesMetadata = metadata,
            ).apply {
                routeCalloutView?.renderCallouts(this)
            }

            // update the camera position to account for the new route
            viewportDataSource.onRouteChanged(routesResult.navigationRoutes.first())
            viewportDataSource.evaluate()
        } else {
            // remove the route line and route arrow from the map
            val style = binding.mapView.mapboxMap.style
            if (style != null) {
                routeLineApi.clearRouteLine { value ->
                    routeLineView.renderClearRouteLineValue(
                        style,
                        value
                    )
                }
                routeArrowView.render(style, routeArrowApi.clearArrows())
            }

            // remove the route reference from camera position evaluations
            viewportDataSource.clearRouteData()
            viewportDataSource.evaluate()
        }
    }

    private val locationObserver = object : LocationObserver {
        var firstLocationUpdateReceived = false

        override fun onNewRawLocation(rawLocation: Location) {
            // not handled
        }

        override fun onNewLocationMatcherResult(locationMatcherResult: LocationMatcherResult) {
            val enhancedLocation = locationMatcherResult.enhancedLocation
            // update location puck's position on the map
            navigationLocationProvider.changePosition(
                location = enhancedLocation,
                keyPoints = locationMatcherResult.keyPoints,
            )

            // update camera position to account for new location
            viewportDataSource.onLocationChanged(enhancedLocation)
            viewportDataSource.evaluate()

            // if this is the first location update the activity has received,
            // it's best to immediately move the camera to the current user location
            if (!firstLocationUpdateReceived) {
                firstLocationUpdateReceived = true
                navigationCamera.requestNavigationCameraToOverview(
                    stateTransitionOptions = NavigationCameraTransitionOptions.Builder()
                        .maxDuration(0) // instant transition
                        .build()
                )
                // If the process was killed the Mapbox routes are gone. Re-request
                // the route now that we have a valid origin location.
                if (mapboxNavigation.getNavigationRoutes().isEmpty()) {
                    destinationLocation?.let { findRoute(it) }
                }
            }
        }
    }

    private val routeProgressObserver = RouteProgressObserver { routeProgress ->
        routeLineApi.updateWithRouteProgress(routeProgress) { result ->
            binding.mapView.mapboxMap.style?.apply {
                routeLineView.renderRouteLineUpdate(this, result)
            }
        }

        // update the camera position to account for the progressed fragment of the route
        viewportDataSource.onRouteProgressChanged(routeProgress)
        viewportDataSource.evaluate()

        // draw the upcoming maneuver arrow on the map
        val style = binding.mapView.mapboxMap.style
        if (style != null) {
            val maneuverArrowResult = routeArrowApi.addUpcomingManeuverArrow(routeProgress)
            routeArrowView.renderManeuverUpdate(style, maneuverArrowResult)
        }

        // update bottom trip progress summary
        binding.tripProgressView.render(
            tripProgressApi.getTripProgress(routeProgress)
        )
    }

    private val pixelDensity = Resources.getSystem().displayMetrics.density
    private val overviewPadding: EdgeInsets by lazy {
        EdgeInsets(
            PADDING_TOP_SMALL * pixelDensity,
            PADDING_HORIZONTAL * pixelDensity,
            PADDING_BOTTOM_SMALL * pixelDensity,
            PADDING_HORIZONTAL * pixelDensity
        )
    }
    private val followingPadding: EdgeInsets by lazy {
        EdgeInsets(
            PADDING_TOP_LARGE * pixelDensity,
            PADDING_HORIZONTAL * pixelDensity,
            PADDING_BOTTOM_LARGE * pixelDensity,
            PADDING_HORIZONTAL * pixelDensity
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mapBinding = FragmentMapBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()

        // Everything below is tied to the MapView being destroyed. Without this the
        // observers kept rendering into a detached map when the app came back from
        // the background, leaving the map frozen and the incident invisible (SMA-755).
        mapBinding?.mapView?.location?.removeOnIndicatorPositionChangedListener(
            onPositionChangedListener
        )
        routeCalloutView = null
        isNavigationInitializedForView = false
        mapBinding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        routeCalloutView = MapboxRouteCalloutView(binding.mapView, routeCalloutViewOptions)
        viewportDataSource = MapboxNavigationViewportDataSource(binding.mapView.mapboxMap)
        navigationCamera = NavigationCamera(
            binding.mapView.mapboxMap,
            binding.mapView.camera,
            viewportDataSource
        )

        // set the padding values
        viewportDataSource.overviewPadding = overviewPadding
        viewportDataSource.followingPadding = followingPadding

        setupTripProgressApi()

        // initialize maneuver arrow view to draw arrows on the map
        val routeArrowOptions = RouteArrowOptions.Builder(requireActivity()).build()
        routeArrowView = MapboxRouteArrowView(routeArrowOptions)

        setupRouteSelectionOverlay()

        binding.mapView.mapboxMap.loadStyle(Style.DARK) { style ->
            routeLineView.initializeLayers(style)
            binding.mapView.compass.updateSettings { enabled = false }
            binding.mapView.gestures.addOnMapLongClickListener { point ->
                findRoute(
                    Location.Builder().longitude(point.longitude()).latitude(point.latitude())
                        .build()
                )
                true
            }
            restoreRouteIfNeeded(style)
        }

        initViewInteractions()
        initObservers()
        // No-op when Mapbox already initialized navigation for this view.
        initNavigation()
    }

    private fun setupTripProgressApi() {
        val distanceFormatterOptions = DistanceFormatterOptions.Builder(requireActivity())
            .unitType(UnitType.METRIC)
            .build()

        tripProgressApi = MapboxTripProgressApi(
            TripProgressUpdateFormatter.Builder(requireActivity())
                .distanceRemainingFormatter(
                    DistanceRemainingFormatter(distanceFormatterOptions)
                )
                .timeRemainingFormatter(
                    TimeRemainingFormatter(requireActivity())
                )
                .percentRouteTraveledFormatter(
                    PercentDistanceTraveledFormatter()
                )
                .estimatedTimeToArrivalFormatter(
                    EstimatedTimeToArrivalFormatter(
                        requireActivity(),
                        TimeFormat.TWELVE_HOURS
                    )
                )
                .build()
        )
    }

    private fun setupRouteSelectionOverlay() {
        // routeSelectionOverlay (XML) sits above MapView but below all buttons,
        // so it captures taps on Mapbox ViewAnnotation callout bubbles (which live
        // inside MapView and consume touches before MapView's own listeners fire).
        // Every event is forwarded to MapView so pan/zoom/long-press still work.
        binding.routeSelectionOverlay.setOnTouchListener { _, event ->
            binding.mapView.dispatchTouchEvent(event) // preserve all map gestures
            if (event.action == android.view.MotionEvent.ACTION_UP) {
                val screenCoord = com.mapbox.maps.ScreenCoordinate(
                    event.x.toDouble(),
                    event.y.toDouble()
                )
                val geoPoint = binding.mapView.mapboxMap.coordinateForPixel(screenCoord)
                selectAlternativeRouteIfClicked(geoPoint)
            }
            true // consumed — already dispatched to MapView manually
        }
    }

    private fun restoreRouteIfNeeded(style: com.mapbox.maps.Style) {
        val existingRoutes = mapboxNavigation.getNavigationRoutes()
        if (existingRoutes.isNotEmpty()) {
            routeLineApi.setNavigationRoutes(existingRoutes) { value ->
                routeLineView.renderRouteDrawData(style, value)
            }
            viewportDataSource.onRouteChanged(existingRoutes.first())
            viewportDataSource.evaluate()
        }
    }

    private fun initViewInteractions() {
        // initialize view interactions
        binding.mapView.camera.addCameraAnimationsLifecycleListener(
            NavigationBasicGesturesHandler(navigationCamera)
        )

        navigationCamera.registerNavigationCameraStateChangeObserver { navigationCameraState ->
            // Guard against animations completing after onDestroyView nulls mapBinding.
            val b = mapBinding ?: return@registerNavigationCameraStateChangeObserver
            when (navigationCameraState) {
                NavigationCameraState.TRANSITION_TO_FOLLOWING,
                NavigationCameraState.FOLLOWING -> b.recenter.visibility = View.INVISIBLE

                NavigationCameraState.TRANSITION_TO_OVERVIEW,
                NavigationCameraState.OVERVIEW,
                NavigationCameraState.IDLE -> b.recenter.visibility = View.VISIBLE
            }
        }

        binding.recenter.setOnClickListener {
            navigationCamera.requestNavigationCameraToFollowing()
        }
    }

    private fun initObservers() {
        // viewLifecycleOwner, not the fragment: onViewCreated runs again every time the
        // view is recreated, and a fragment-scoped collector would survive and stack up.
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { mapFragmentUiState ->
                    val incident = mapFragmentUiState.incident

                    if (incident?.longitude != null && incident.latitude != null) {
                        val incomingKey = incident.longitude!! to incident.latitude!!

                        // Skip redundant findRoute() calls: if the destination coordinates
                        // haven't changed since the last successful request, the active
                        // navigation session already covers this location.
                        if (incomingKey != lastRoutedDestination) {
                            destinationLocation = Location.Builder()
                                .longitude(incident.longitude!!)
                                .latitude(incident.latitude!!)
                                .build()

                            destinationLocation?.let { findRoute(it) }
                        }
                    } else {
                        mapboxNavigation.setNavigationRoutes(emptyList())
                        val card = binding.tripProgressCard
                        val lp = card.layoutParams as
                            androidx.constraintlayout.widget.ConstraintLayout.LayoutParams
                        lp.bottomMargin = 0
                        card.layoutParams = lp
                        card.visibility = View.GONE
                        destinationLocation = null
                        lastRoutedDestination = null
                    }
                }
            }
        }
    }

    /**
     * Invoked both by Mapbox's `onInitialize` callback and by [onViewCreated], since the
     * SDK can initialize before the view exists. Guarded so the puck is wired exactly
     * once per view, on whichever of the two happens last.
     */
    private fun initNavigation() {
        Timber.d("initNavigation")
        if (view == null || isNavigationInitializedForView) return
        isNavigationInitializedForView = true

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                // initialize location puck
                binding.mapView.location.apply {
                    setLocationProvider(navigationLocationProvider)
                    addOnIndicatorPositionChangedListener(onPositionChangedListener)
                    locationPuck = LocationPuck2D(
                        bearingImage = ImageHolder.from(
                            R.drawable.ic_ambulance_marker
                        )
                    )
                    puckBearingEnabled = true
                    enabled = true
                }

                destinationLocation?.let { destination ->
                    findRoute(destination)
                }
            }
        }
    }

    /**
     * Checks whether the tap is close enough to an alternative route line and, if so,
     * promotes that route to primary.
     *
     * We avoid [MapboxRouteLineApi.findClosestRoute] because it is private in the current
     * Mapbox Navigation beta. Instead we project sampled route coordinates to screen space
     * and check their pixel distance to the tap — the same geometry the SDK renders.
     */
    @Suppress("MagicNumber")
    private fun selectAlternativeRouteIfClicked(point: com.mapbox.geojson.Point) {
        val currentRoutes = mapboxNavigation.getNavigationRoutes()
        Timber.d("[RouteSelect] routes=${currentRoutes.size}")
        if (currentRoutes.size < 2) return

        val paddingPx = ROUTE_CLICK_PADDING_DP * resources.displayMetrics.density
        val tapScreen = binding.mapView.mapboxMap.pixelForCoordinate(point)
        Timber.d(
            "[RouteSelect] tap screen=(${tapScreen.x.toInt()}, ${tapScreen.y.toInt()}) " +
                "padding=${paddingPx.toInt()}px"
        )

        currentRoutes.drop(1).forEachIndexed { idx, route ->
            val near = isTapNearRoute(route, tapScreen, paddingPx)
            Timber.d("[RouteSelect] alt[$idx] near=$near")
            if (near) {
                val reordered = currentRoutes.toMutableList().apply {
                    remove(route)
                    add(0, route)
                }
                Timber.d("[RouteSelect] → promoting alt[$idx] to primary")
                mapboxNavigation.setNavigationRoutes(reordered)
                return
            }
        }
        Timber.d("[RouteSelect] no alternative matched")
    }

    private fun isTapNearRoute(
        route: com.mapbox.navigation.base.route.NavigationRoute,
        tapScreen: com.mapbox.maps.ScreenCoordinate,
        paddingPx: Float
    ): Boolean {
        val encodedGeometry = route.directionsRoute.geometry()
        if (encodedGeometry.isNullOrEmpty()) {
            Timber.w("[RouteSelect] route geometry is null/empty")
            return false
        }

        val coords = com.mapbox.geojson.LineString
            .fromPolyline(encodedGeometry, com.mapbox.core.constants.Constants.PRECISION_6)
            .coordinates()

        return if (coords.isEmpty()) {
            false
        } else {
            checkDistanceToTap(coords, tapScreen, paddingPx)
        }
    }

    private fun checkDistanceToTap(
        coords: List<com.mapbox.geojson.Point>,
        tapScreen: com.mapbox.maps.ScreenCoordinate,
        paddingPx: Float
    ): Boolean {
        val strideSize = 80
        val stride = maxOf(1, coords.size / strideSize)
        val paddingSq = paddingPx * paddingPx
        var minDistSq = Double.MAX_VALUE

        coords.filterIndexed { i, _ -> i % stride == 0 }.forEach { coord ->
            val screen = binding.mapView.mapboxMap.pixelForCoordinate(
                com.mapbox.geojson.Point.fromLngLat(coord.longitude(), coord.latitude())
            )
            val dx = screen.x - tapScreen.x
            val dy = screen.y - tapScreen.y
            val dSq = dx * dx + dy * dy
            if (dSq < minDistSq) minDistSq = dSq
        }

        Timber.d(
            "[RouteSelect] minDist=${kotlin.math.sqrt(minDistSq).toInt()}px " +
                "threshold=${paddingPx.toInt()}px pts=${coords.size}"
        )
        return minDistSq <= paddingSq
    }

    private fun findRoute(destinationLocation: Location) {
        Timber.d("findRoute → lat=${destinationLocation.latitude} lng=${destinationLocation.longitude}")

        val originLocation = navigationLocationProvider.lastLocation
        if (originLocation == null) {
            // GPS fix not yet available. The locationObserver already has a retry guard
            // (`firstLocationUpdateReceived`) that calls findRoute() on the first valid
            // location — nothing more to do here.
            Timber.w("findRoute: no GPS fix yet, will retry on first location update")
            return
        }

        val originPoint = Point.fromLngLat(originLocation.longitude, originLocation.latitude)
        val destinationPoint = Point.fromLngLat(
            destinationLocation.longitude,
            destinationLocation.latitude
        )

        mapboxNavigation.requestRoutes(
            RouteOptions.builder()
                .applyDefaultNavigationOptions()
                .applyLanguageAndVoiceUnitOptions(requireActivity())
                .coordinatesList(listOf(originPoint, destinationPoint))
                .alternatives(true)
                .apply {
                    originLocation.bearing?.let { bearing ->
                        bearingsList(
                            listOf(
                                Bearing.builder().angle(bearing).build(),
                                null
                            )
                        )
                    }
                }
                .layersList(listOf(mapboxNavigation.getZLevel(), null))
                .build(),
            object : NavigationRouterCallback {
                override fun onCanceled(routeOptions: RouteOptions, routerOrigin: String) {
                    Timber.w("findRoute: request cancelled")
                }

                override fun onFailure(reasons: List<RouterFailure>, routeOptions: RouteOptions) {
                    // Log the reasons so we know if this is a network error, API key issue, etc.
                    // Reset lastRoutedDestination so the next uiState emission retries the request.
                    Timber.e("findRoute failed: $reasons")
                    lastRoutedDestination = null
                }

                override fun onRoutesReady(
                    routes: List<NavigationRoute>,
                    routerOrigin: String
                ) {
                    lastRoutedDestination =
                        destinationLocation.longitude to destinationLocation.latitude
                    setRouteAndStartNavigation(routes)
                }
            }
        )
    }

    private fun setRouteAndStartNavigation(routes: List<NavigationRoute>) {
        mapboxNavigation.setNavigationRoutes(routes)
        val card = binding.tripProgressCard
        val lp = card.layoutParams as androidx.constraintlayout.widget.ConstraintLayout.LayoutParams
        lp.bottomMargin = (INCIDENT_SHEET_PEEK_DP * Resources.getSystem().displayMetrics.density).toInt()
        card.layoutParams = lp
        card.visibility = View.VISIBLE
        navigationCamera.requestNavigationCameraToFollowing()
    }
}
