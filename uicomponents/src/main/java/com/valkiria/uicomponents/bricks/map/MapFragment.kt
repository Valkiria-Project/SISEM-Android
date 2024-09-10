package com.valkiria.uicomponents.bricks.map

import android.annotation.SuppressLint
import android.content.res.Resources
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.mapbox.api.directions.v5.models.Bearing
import com.mapbox.api.directions.v5.models.DirectionsRoute
import com.mapbox.api.directions.v5.models.RouteOptions
import com.mapbox.common.location.Location
import com.mapbox.geojson.Point
import com.mapbox.maps.EdgeInsets
import com.mapbox.maps.ImageHolder
import com.mapbox.maps.plugin.LocationPuck2D
import com.mapbox.maps.plugin.animation.camera
import com.mapbox.maps.plugin.gestures.gestures
import com.mapbox.maps.plugin.locationcomponent.location
import com.mapbox.navigation.base.ExperimentalPreviewMapboxNavigationAPI
import com.mapbox.navigation.base.extensions.applyDefaultNavigationOptions
import com.mapbox.navigation.base.extensions.applyLanguageAndVoiceUnitOptions
import com.mapbox.navigation.base.options.NavigationOptions
import com.mapbox.navigation.base.route.NavigationRoute
import com.mapbox.navigation.base.route.NavigationRouterCallback
import com.mapbox.navigation.base.route.RouterFailure
import com.mapbox.navigation.core.MapboxNavigation
import com.mapbox.navigation.core.directions.session.RoutesObserver
import com.mapbox.navigation.core.lifecycle.MapboxNavigationApp
import com.mapbox.navigation.core.lifecycle.MapboxNavigationObserver
import com.mapbox.navigation.core.lifecycle.requireMapboxNavigation
import com.mapbox.navigation.core.replay.route.ReplayProgressObserver
import com.mapbox.navigation.core.replay.route.ReplayRouteMapper
import com.mapbox.navigation.core.trip.session.LocationMatcherResult
import com.mapbox.navigation.core.trip.session.LocationObserver
import com.mapbox.navigation.core.trip.session.RouteProgressObserver
import com.mapbox.navigation.ui.maps.NavigationStyles
import com.mapbox.navigation.ui.maps.camera.NavigationCamera
import com.mapbox.navigation.ui.maps.camera.data.MapboxNavigationViewportDataSource
import com.mapbox.navigation.ui.maps.camera.lifecycle.NavigationBasicGesturesHandler
import com.mapbox.navigation.ui.maps.camera.state.NavigationCameraState.FOLLOWING
import com.mapbox.navigation.ui.maps.camera.state.NavigationCameraState.IDLE
import com.mapbox.navigation.ui.maps.camera.state.NavigationCameraState.OVERVIEW
import com.mapbox.navigation.ui.maps.camera.state.NavigationCameraState.TRANSITION_TO_FOLLOWING
import com.mapbox.navigation.ui.maps.camera.state.NavigationCameraState.TRANSITION_TO_OVERVIEW
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
import com.valkiria.uicomponents.R
import com.valkiria.uicomponents.databinding.FragmentMapBinding
import java.util.Date

private const val BUTTON_ANIMATION_DURATION = 1500L
private const val PADDING_TOP_SMALL = 140.0
private const val PADDING_TOP_LARGE = 180.0
private const val PADDING_HORIZONTAL = 40.0
private const val PADDING_BOTTOM_SMALL = 120.0
private const val PADDING_BOTTOM_LARGE = 150.0
private const val BEARING_DEGREES = 45.0

@Suppress("TooManyFunctions")
@OptIn(ExperimentalPreviewMapboxNavigationAPI::class)
class MapFragment : Fragment(R.layout.fragment_map) {

    private lateinit var binding: FragmentMapBinding

    private var lastLocation: Location? = null
    private var destinationPoint: Point? = null

    private lateinit var navigationCamera: NavigationCamera
    private lateinit var replayProgressObserver: ReplayProgressObserver
    private lateinit var routeArrowView: MapboxRouteArrowView
    private lateinit var routeLineApi: MapboxRouteLineApi
    private lateinit var routeLineView: MapboxRouteLineView
    private lateinit var viewportDataSource: MapboxNavigationViewportDataSource

    private val routeCalloutApiOptions: MapboxRouteCalloutApiOptions by lazy {
        MapboxRouteCalloutApiOptions.Builder()
            .routeCalloutType(RouteCalloutType.RouteDurations)
            .build()
    }

    private val routeCalloutViewOptions: MapboxRouteCalloutViewOptions by lazy {
        MapboxRouteCalloutViewOptions.Builder()
            .selectedBackgroundColor(R.color.sisem_route_callout_background)
            .selectedTextColor(android.R.color.white)
            .backgroundColor(android.R.color.darker_gray)
            .build()
    }

    private val routeCalloutApi by lazy {
        MapboxRouteCalloutApi(routeCalloutApiOptions)
    }

    private val routeCalloutView by lazy {
        MapboxRouteCalloutView(binding.mapView, routeCalloutViewOptions)
    }

    private val navigationLocationProvider = NavigationLocationProvider()
    private val replayRouteMapper = ReplayRouteMapper()
    private val routeArrowApi: MapboxRouteArrowApi = MapboxRouteArrowApi()

    @OptIn(ExperimentalPreviewMapboxNavigationAPI::class)
    private val mapboxNavigation: MapboxNavigation by requireMapboxNavigation(
        onResumedObserver = object : MapboxNavigationObserver {
            @SuppressLint("MissingPermission")
            override fun onAttached(mapboxNavigation: MapboxNavigation) {
                mapboxNavigation.registerRoutesObserver(routesObserver)
                mapboxNavigation.registerLocationObserver(locationObserver)
                mapboxNavigation.registerRouteProgressObserver(routeProgressObserver)

                replayProgressObserver = ReplayProgressObserver(mapboxNavigation.mapboxReplayer)
                mapboxNavigation.registerRouteProgressObserver(replayProgressObserver)

                // Start the trip session to being receiving location updates in free drive
                // and later when a route is set also receiving route progress updates.
                // In case of `startReplayTripSession`,
                // location events are emitted by the `MapboxReplayer`
                mapboxNavigation.startReplayTripSession()
            }

            override fun onDetached(mapboxNavigation: MapboxNavigation) {
                mapboxNavigation.unregisterRoutesObserver(routesObserver)
                mapboxNavigation.unregisterLocationObserver(locationObserver)
                mapboxNavigation.unregisterRouteProgressObserver(routeProgressObserver)
                mapboxNavigation.unregisterRouteProgressObserver(replayProgressObserver)
                mapboxNavigation.mapboxReplayer.finish()
            }
        }
    )

    private val routesObserver = RoutesObserver { result ->
        if (result.navigationRoutes.isNotEmpty()) {
            // generate route geometries asynchronously and render them
            routeLineApi.setNavigationRoutes(
                result.navigationRoutes
            ) { value ->
                binding.mapView.mapboxMap.style?.apply {
                    routeLineView.renderRouteDrawData(this, value)
                }
            }

            val alternativesMetadata = mapboxNavigation.getAlternativeMetadataFor(
                result.navigationRoutes
            )
            routeCalloutApi.setNavigationRoutes(
                result.navigationRoutes,
                alternativesMetadata
            ).also { value ->
                routeCalloutView.renderCallouts(value)
            }

            // update the camera position to account for the new route
            viewportDataSource.onRouteChanged(result.navigationRoutes.first())
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
            lastLocation = locationMatcherResult.enhancedLocation
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
        // update the camera position to account for the progressed fragment of the route
        viewportDataSource.onRouteProgressChanged(routeProgress)
        viewportDataSource.evaluate()

        // draw the upcoming maneuver arrow on the map
        val style = binding.mapView.mapboxMap.style
        if (style != null) {
            val maneuverArrowResult = routeArrowApi.addUpcomingManeuverArrow(routeProgress)
            routeArrowView.renderManeuverUpdate(style, maneuverArrowResult)
        }
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (savedInstanceState != null) {
            getMapFragmentArguments(savedInstanceState)
        } else {
            arguments?.let { getMapFragmentArguments(it) }
        }
    }

    private fun getMapFragmentArguments(bundle: Bundle) {
        lastLocation = Location.Builder()
            .longitude(bundle.getDouble(LOCATION_POINT_LONGITUDE))
            .latitude(bundle.getDouble(LOCATION_POINT_LATITUDE))
            .build()

        destinationPoint = Point.fromLngLat(
            bundle.getDouble(DESTINATION_POINT_LONGITUDE),
            bundle.getDouble(DESTINATION_POINT_LATITUDE)
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

        // initialize route line, the routeLineBelowLayerId is specified to place
        // the route line below road labels layer on the map
        // the value of this option will depend on the style that you are using
        // and under which layer the route line should be placed on the map layers stack
        val mapboxRouteLineViewOptions = MapboxRouteLineViewOptions.Builder(requireActivity())
            .routeLineBelowLayerId("road-label-navigation")
            .build()

        routeLineApi = MapboxRouteLineApi(MapboxRouteLineApiOptions.Builder().build())
        routeLineView = MapboxRouteLineView(mapboxRouteLineViewOptions)

        // initialize maneuver arrow view to draw arrows on the map
        val routeArrowOptions = RouteArrowOptions.Builder(requireActivity()).build()
        routeArrowView = MapboxRouteArrowView(routeArrowOptions)

        // load map style
        binding.mapView.mapboxMap.loadStyle(NavigationStyles.NAVIGATION_NIGHT_STYLE) {
            // Ensure that the route line related layers are present before the route arrow
            routeLineView.initializeLayers(it)

            // add long click listener that search for a route to the clicked destination
            binding.mapView.gestures.addOnMapLongClickListener { point ->
                findRoute(point)
                true
            }
        }

        initViewInteractions()
        initNavigation()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        lastLocation?.longitude?.let { outState.putDouble(LOCATION_POINT_LONGITUDE, it) }
        lastLocation?.latitude?.let { outState.putDouble(LOCATION_POINT_LATITUDE, it) }
        destinationPoint?.longitude()?.let { outState.putDouble(DESTINATION_POINT_LONGITUDE, it) }
        destinationPoint?.latitude()?.let { outState.putDouble(DESTINATION_POINT_LATITUDE, it) }
    }

    private fun initViewInteractions() {
        // initialize view interactions
        binding.mapView.camera.addCameraAnimationsLifecycleListener(
            NavigationBasicGesturesHandler(navigationCamera)
        )

        navigationCamera.registerNavigationCameraStateChangeObserver { navigationCameraState ->
            // shows/hide the recenter button depending on the camera state
            when (navigationCameraState) {
                TRANSITION_TO_FOLLOWING,
                FOLLOWING -> binding.recenter.visibility = View.INVISIBLE

                TRANSITION_TO_OVERVIEW,
                OVERVIEW,
                IDLE -> binding.recenter.visibility = View.VISIBLE
            }
        }

        binding.recenter.setOnClickListener {
            navigationCamera.requestNavigationCameraToFollowing()
            binding.routeOverview.showTextAndExtend(BUTTON_ANIMATION_DURATION)
        }
        binding.routeOverview.setOnClickListener {
            navigationCamera.requestNavigationCameraToOverview()
            binding.recenter.showTextAndExtend(BUTTON_ANIMATION_DURATION)
        }
    }

    private fun initNavigation() {
        MapboxNavigationApp.setup {
            NavigationOptions.Builder(requireActivity())
                .build()
        }

        // initialize location puck
        binding.mapView.location.apply {
            setLocationProvider(navigationLocationProvider)
            this.locationPuck = LocationPuck2D(
                bearingImage = ImageHolder.Companion.from(
                    R.drawable.ic_ambulance_marker
                )
            )
            puckBearingEnabled = true
            enabled = true
        }

        lastLocation?.let { location ->
            replayOriginLocation(location)
        }

        destinationPoint?.let { destination ->
            findRoute(destination)
        }
    }

    @OptIn(ExperimentalPreviewMapboxNavigationAPI::class)
    private fun replayOriginLocation(location: Location) {
        with(mapboxNavigation.mapboxReplayer) {
            play()
            pushEvents(
                listOf(
                    ReplayRouteMapper.mapToUpdateLocation(
                        Date().time.toDouble(),
                        location
                    )
                )
            )
            playFirstLocation()
        }
    }

    private fun findRoute(destination: Point) {
        val originLocation = navigationLocationProvider.lastLocation ?: lastLocation
        val originPoint = originLocation?.let {
            Point.fromLngLat(it.longitude, originLocation.latitude)
        }

        // execute a route request
        // it's recommended to use the
        // applyDefaultNavigationOptions and applyLanguageAndVoiceUnitOptions
        // that make sure the route request is optimized
        // to allow for support of all of the Navigation SDK features
        mapboxNavigation.requestRoutes(
            RouteOptions.builder()
                .applyDefaultNavigationOptions()
                .applyLanguageAndVoiceUnitOptions(requireActivity())
                .coordinatesList(listOf(originPoint, destination))
                .alternatives(true)
                .apply {
                    // provide the bearing for the origin of the request to ensure
                    // that the returned route faces in the direction of the current user movement
                    originLocation?.bearing?.let { bearing ->
                        bearingsList(
                            listOf(
                                Bearing.builder()
                                    .angle(bearing)
                                    .degrees(BEARING_DEGREES)
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
        // set routes, where the first route in the list is the primary route that
        // will be used for active guidance
        mapboxNavigation.setNavigationRoutes(routes)

        // show UI elements
        binding.routeOverview.visibility = View.VISIBLE

        // move the camera to overview when new route is available
        navigationCamera.requestNavigationCameraToOverview()

        // start simulation
        startSimulation(routes.first().directionsRoute)
    }

    @OptIn(ExperimentalPreviewMapboxNavigationAPI::class)
    private fun startSimulation(route: DirectionsRoute) {
        mapboxNavigation.mapboxReplayer.stop()
        mapboxNavigation.mapboxReplayer.clearEvents()
        val replayData = replayRouteMapper.mapDirectionsRouteGeometry(route)
        mapboxNavigation.mapboxReplayer.pushEvents(replayData)
        mapboxNavigation.mapboxReplayer.seekTo(replayData[0])
        mapboxNavigation.mapboxReplayer.play()
    }

    companion object {
        const val LOCATION_POINT_LONGITUDE = "location_point_longitude"
        const val LOCATION_POINT_LATITUDE = "location_point_latitude"
        const val DESTINATION_POINT_LONGITUDE = "destination_point_longitude"
        const val DESTINATION_POINT_LATITUDE = "destination_point_latitude"
    }
}
