package com.skgtecnologia.sisem.data.remote.interceptors

import com.google.android.gms.location.FusedLocationProviderClient
import com.skgtecnologia.sisem.BuildConfig
import com.skgtecnologia.sisem.ui.commons.extensions.locationFlow
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeout
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import timber.log.Timber

private const val HTTP_CLIENT_VERSION_HEADER = "User-Agent"
private const val CLIENT_VERSION = "sisem/Android/" + BuildConfig.VERSION_NAME
private const val HTTP_LOCATION_HEADER = "geolocation"
private const val UNAVAILABLE_LOCATION = "UNAVAILABLE_LOCATION"
private const val AUDIT_TIMEOUT = 1000L
private const val IP_ADDRESS_URL = "https://api.ipify.org/"
private const val IP_ADDRESS_HEADER = "x-ip"
private const val UNAVAILABLE_IP_ADDRESS = "UNAVAILABLE_LOCATION"

@Singleton
class AuditInterceptor @Inject constructor(
    private val fusedLocationClient: FusedLocationProviderClient
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
            .newBuilder()
            .addHeader(HTTP_CLIENT_VERSION_HEADER, CLIENT_VERSION)
            .withCurrentLocation()
            .withCurrentIPAddress()
            .build()

        return chain.proceed(request)
    }

    private fun Request.Builder.withCurrentLocation(): Request.Builder = runBlocking {
        val location = runCatching {
            withTimeout(AUDIT_TIMEOUT) {
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

    private fun Request.Builder.withCurrentIPAddress(): Request.Builder = runBlocking {
        val ipAddress = runCatching {
            withTimeout(AUDIT_TIMEOUT) {
                val client = OkHttpClient()
                val response = withContext(Dispatchers.IO) {
                    val request: Request = Request.Builder()
                        .url(IP_ADDRESS_URL)
                        .build()
                    client.newCall(request).execute()
                }
                response.body?.string().orEmpty().also {
                    response.body?.close()
                }
            }
        }.getOrNull() ?: UNAVAILABLE_IP_ADDRESS

        this@withCurrentIPAddress.addHeader(
            IP_ADDRESS_HEADER, ipAddress
        )
    }
}
