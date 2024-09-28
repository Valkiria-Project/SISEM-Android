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
import com.mapbox.navigation.core.MapboxNavigation
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
import com.skgtecnologia.sisem.commons.extensions.biLet
import com.skgtecnologia.sisem.databinding.FragmentMapBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import timber.log.Timber
import kotlin.time.Duration.Companion.minutes

private const val ACTIVE_INCIDENT_OBSERVATION_DELAY = 3000L
private const val PADDING_TOP_SMALL = 140.0
private const val PADDING_TOP_LARGE = 180.0
private const val PADDING_HORIZONTAL = 40.0
private const val PADDING_BOTTOM_SMALL = 120.0
private const val PADDING_BOTTOM_LARGE = 150.0

@Suppress("TooManyFunctions")
@OptIn(ExperimentalPreviewMapboxNavigationAPI::class)
@AndroidEntryPoint
class MapFragment : Fragment(R.layout.fragment_map) {

    private lateinit var binding: FragmentMapBinding
    val viewModel: MapFragmentViewModel by viewModels()

    private var destinationLocation: Location? = null

    private lateinit var navigationCamera: NavigationCamera
    private lateinit var routeArrowView: MapboxRouteArrowView
    private lateinit var tripProgressApi: MapboxTripProgressApi
    private lateinit var viewportDataSource: MapboxNavigationViewportDataSource

    private val navigationLocationProvider = NavigationLocationProvider()

    private val onPositionChangedListener = OnIndicatorPositionChangedListener { point ->
        val result = routeLineApi.updateTraveledRouteLine(point)
        binding.mapView.mapboxMap.style?.apply {
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

    private val routeCalloutView by lazy {
        MapboxRouteCalloutView(binding.mapView, routeCalloutViewOptions)
    }

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
                mapboxNavigation.registerRoutesObserver(routesObserver)
                mapboxNavigation.registerLocationObserver(locationObserver)
                mapboxNavigation.registerRouteProgressObserver(routeProgressObserver)
                mapboxNavigation.startTripSession()
            }

            override fun onDetached(mapboxNavigation: MapboxNavigation) {
                mapboxNavigation.unregisterRoutesObserver(routesObserver)
                mapboxNavigation.unregisterLocationObserver(locationObserver)
                mapboxNavigation.unregisterRouteProgressObserver(routeProgressObserver)
            }
        },
        onInitialize = this::initNavigation
    )

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
                routeCalloutView.renderCallouts(this)
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
        binding = FragmentMapBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewportDataSource = MapboxNavigationViewportDataSource(binding.mapView.mapboxMap)
        navigationCamera = NavigationCamera(
            binding.mapView.mapboxMap,
            binding.mapView.camera,
            viewportDataSource
        )

        // set the padding values
        viewportDataSource.overviewPadding = overviewPadding
        viewportDataSource.followingPadding = followingPadding

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

        // initialize maneuver arrow view to draw arrows on the map
        val routeArrowOptions = RouteArrowOptions.Builder(requireActivity()).build()
        routeArrowView = MapboxRouteArrowView(routeArrowOptions)

        // load map style

        binding.mapView.mapboxMap.loadStyle(Style.DARK) {
            // Ensure that the route line related layers are present before the route arrow
            routeLineView.initializeLayers(it)

            binding.mapView.compass.updateSettings {
                enabled = false
            }

            // add long click listener that search for a route to the clicked destination
            binding.mapView.gestures.addOnMapLongClickListener { point ->
                findRoute(
                    Location.Builder().longitude(point.longitude()).latitude(point.latitude())
                        .build()
                )
                true
            }
        }

        initViewInteractions()
        initObservers()
    }

    private fun initViewInteractions() {
        // initialize view interactions
        binding.mapView.camera.addCameraAnimationsLifecycleListener(
            NavigationBasicGesturesHandler(navigationCamera)
        )

        navigationCamera.registerNavigationCameraStateChangeObserver { navigationCameraState ->
            // shows/hide the recenter button depending on the camera state
            when (navigationCameraState) {
                NavigationCameraState.TRANSITION_TO_FOLLOWING,
                NavigationCameraState.FOLLOWING -> binding.recenter.visibility = View.INVISIBLE

                NavigationCameraState.TRANSITION_TO_OVERVIEW,
                NavigationCameraState.OVERVIEW,
                NavigationCameraState.IDLE -> binding.recenter.visibility = View.VISIBLE
            }
        }

        binding.recenter.setOnClickListener {
            navigationCamera.requestNavigationCameraToFollowing()
        }
    }

    private fun initObservers() {
        lifecycleScope.launch {
            delay(ACTIVE_INCIDENT_OBSERVATION_DELAY)
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { mapFragmentUiState ->
                    val incident = mapFragmentUiState.incident

                    biLet(incident?.longitude, incident?.latitude) { longitude, latitude ->
                        destinationLocation = Location.Builder()
                            .longitude(longitude)
                            .latitude(latitude)
                            .build()

                        destinationLocation?.let { findRoute(it) }
                    }
                }
            }
        }
    }

    private fun initNavigation() {
        Timber.d("initNavigation")
        lifecycleScope.launch {
            delay(ACTIVE_INCIDENT_OBSERVATION_DELAY)
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

    private fun findRoute(destinationLocation: Location) {
        Timber.d("findRoute with location ${destinationLocation.latitude} and ${destinationLocation.longitude}")
        val originLocation = navigationLocationProvider.lastLocation
        val originPoint = originLocation?.let {
            Point.fromLngLat(it.longitude, it.latitude)
        }
        val destinationPoint = Point.fromLngLat(
            destinationLocation.longitude,
            destinationLocation.latitude
        )

        // execute a route request
        // it's recommended to use the
        // applyDefaultNavigationOptions and applyLanguageAndVoiceUnitOptions
        // that make sure the route request is optimized
        // to allow for support of all of the Navigation SDK features
        mapboxNavigation.requestRoutes(
            RouteOptions.builder()
                .applyDefaultNavigationOptions()
                .applyLanguageAndVoiceUnitOptions(requireActivity())
                .coordinatesList(listOf(originPoint, destinationPoint))
                .alternatives(true)
                .apply {
                    // provide the bearing for the origin of the request to ensure
                    // that the returned route faces in the direction of the current user movement
                    originLocation?.bearing?.let { bearing ->
                        bearingsList(
                            listOf(
                                Bearing.builder()
                                    .angle(bearing)
                                    .build(),
                                null
                            )
                        )
                    }
                }
                .layersList(listOf(mapboxNavigation.getZLevel(), null))
                .build(),
            object : NavigationRouterCallback {
                override fun onCanceled(routeOptions: RouteOptions, routerOrigin: String) {
                    // no impl
                }

                override fun onFailure(reasons: List<RouterFailure>, routeOptions: RouteOptions) {
                    // no impl
                }

                override fun onRoutesReady(
                    routes: List<NavigationRoute>,
                    routerOrigin: String
                ) {
                    setRouteAndStartNavigation(routes)
                }
            }
        )
    }

    private fun setRouteAndStartNavigation(routes: List<NavigationRoute>) {
        mapboxNavigation.setNavigationRoutes(routes)
        binding.tripProgressCard.visibility = View.VISIBLE
        navigationCamera.requestNavigationCameraToFollowing()
    }
}
