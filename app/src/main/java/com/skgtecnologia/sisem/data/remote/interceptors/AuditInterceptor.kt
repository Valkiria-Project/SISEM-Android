package com.skgtecnologia.sisem.data.remote.interceptors

import com.google.android.gms.location.FusedLocationProviderClient
import com.skgtecnologia.sisem.BuildConfig
import com.skgtecnologia.sisem.ui.commons.extensions.locationFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withTimeout
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

const val HTTP_CLIENT_VERSION_HEADER = "User-Agent"
const val CLIENT_VERSION = "sisem/Android/" + BuildConfig.VERSION_NAME
const val HTTP_LOCATION_HEADER = "geolocation"
const val UNAVAILABLE_LOCATION = "UNAVAILABLE_LOCATION"
const val LOCATION_TIMEOUT = 1000L

@Singleton
class AuditInterceptor @Inject constructor(
    private val fusedLocationClient: FusedLocationProviderClient
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
            .newBuilder()
            .addHeader(HTTP_CLIENT_VERSION_HEADER, CLIENT_VERSION)
            .withCurrentLocation()
            .build()

        return chain.proceed(request)
    }

    private fun Request.Builder.withCurrentLocation(): Request.Builder = runBlocking {
        val location = runCatching {
            withTimeout(LOCATION_TIMEOUT) {
                fusedLocationClient.locationFlow()
                    .catch { throwable ->
                        Timber.d("Unable to get location $throwable")
                    }
                    .first()
            }
        }.getOrNull()

        val formattedLocation = if (location != null) {
            "${location.latitude}, ${location.longitude}"
        } else {
            UNAVAILABLE_LOCATION
        }

        this@withCurrentLocation.addHeader(
            HTTP_LOCATION_HEADER, formattedLocation
        )
    }
}
