package com.skgtecnologia.sisem.ui.map

// Bogota default location expressed in terms of longitude and latitude
const val BOGOTA_DEFAULT_LNG = -74.1063086
const val BOGOTA_DEFAULT_LAT = 4.7593809

data class MapUiState(
    val location: Pair<Double, Double> = BOGOTA_DEFAULT_LNG to BOGOTA_DEFAULT_LAT
)
