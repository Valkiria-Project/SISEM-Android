package com.skgtecnologia.sisem.data.remote.interceptors

import android.content.Context
import com.skgtecnologia.sisem.commons.resources.StorageProvider
import com.valkiria.uicomponents.utlis.HOURS_MINUTES_SECONDS_24_HOURS_PATTERN
import com.valkiria.uicomponents.utlis.TimeUtils
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

private const val FILE_NAME = "android_networking"

class NetworkInterceptor @Inject constructor(
    private val storageProvider: StorageProvider
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request().run {
            newBuilder().build()
        }

        val requestContent = TimeUtils
            .getFormattedLocalTimeAsString(pattern = HOURS_MINUTES_SECONDS_24_HOURS_PATTERN) +
            "\t" + request.toString() +
            "\n"

        storageProvider.storeContent(
            FILE_NAME,
            Context.MODE_APPEND,
            requestContent.toByteArray()
        )

        val response = chain.proceed(request)

        val responseContent = TimeUtils
            .getFormattedLocalTimeAsString(pattern = HOURS_MINUTES_SECONDS_24_HOURS_PATTERN) +
            "\t" + response.toString() +
            "\n"

        storageProvider.storeContent(
            FILE_NAME,
            Context.MODE_APPEND,
            responseContent.toByteArray()
        )

        return response
    }
}
